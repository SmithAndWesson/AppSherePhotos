package com.sharing.photos.demo.service;

import com.sharing.photos.demo.entity.ImageModel;
import com.sharing.photos.demo.entity.Post;
import com.sharing.photos.demo.entity.User;
import com.sharing.photos.demo.exceptions.ImageNotFoundException;
import com.sharing.photos.demo.repository.ImageRepository;
import com.sharing.photos.demo.repository.PostRepository;
import com.sharing.photos.demo.repository.UserRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUploadService {

  public static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);

  private final ImageRepository imageRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Autowired
  public ImageUploadService(ImageRepository imageRepository,
      PostRepository postRepository, UserRepository userRepository) {
    this.imageRepository = imageRepository;
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  public ImageModel uploadImageToUser(MultipartFile file, Principal principal) throws IOException {

    User user = getUserByPrincipal(principal);
    LOGGER.info("Uploading image to user {}" + user.getUsername());

    ImageModel userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
    if (!ObjectUtils.isEmpty(userProfileImage)) {
      imageRepository.delete(userProfileImage);
    }

    ImageModel imageModel = new ImageModel();
    imageModel.setName(file.getOriginalFilename());
    imageModel.setUserId(user.getId());
    imageModel.setImageBytes(compressBytes(file.getBytes()));

    return imageRepository.save(imageModel);
  }

  public ImageModel uploadImageToPost(MultipartFile file, Principal principal, Long postId)
      throws IOException {
    User user = getUserByPrincipal(principal);
    LOGGER.info("Uploading image to post {}" + postId);

    Post post = user.getPosts()
        .stream()
        .filter(p -> p.getId().equals(postId))
        .collect(toSinglePostCollector());
    ImageModel imageModel = new ImageModel();
    imageModel.setPostId(post.getId());
    imageModel.setName(file.getOriginalFilename());
    imageModel.setImageBytes(compressBytes(file.getBytes()));
    return imageRepository.save(imageModel);

  }

  public ImageModel getImageToUser(Principal principal){
    User user = getUserByPrincipal(principal);

    ImageModel imageModel = imageRepository.findByUserId(user.getId()).orElse(null);
    if(!ObjectUtils.isEmpty(imageModel)){
      imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
    }
    return imageModel;
  }

  public ImageModel getImageToPost (Long postId){
    ImageModel imageModel = imageRepository.findByPostId(postId)
        .orElseThrow(() -> new ImageNotFoundException("Cannot find image to Post: " + postId));
    if(!ObjectUtils.isEmpty(imageModel)){
      imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
    }

    return imageModel;
  }
  private User getUserByPrincipal(Principal principal) {
    String username = principal.getName();
    return userRepository.findUserByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("Username not found with username " + username));
  }

  private byte[] compressBytes(byte[] data) {
    Deflater deflater = new Deflater();
    deflater.setInput(data);
    deflater.finish();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] buffer = new byte[1024];
    while (!deflater.finished()) {
      int count = deflater.deflate(buffer);
      outputStream.write(buffer, 0, count);
    }
    try {
      outputStream.close();
    } catch (IOException e) {
      LOGGER.error("Cannot compress Bytes");
    }
    System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
    return outputStream.toByteArray();
  }

  private static byte[] decompressBytes(byte[] data) {
    Inflater inflater = new Inflater();
    inflater.setInput(data);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] buffer = new byte[1024];
    try {
      while (!inflater.finished()) {
        int count = inflater.inflate(buffer);
        outputStream.write(buffer, 0, count);
      }
      outputStream.close();
    } catch (IOException | DataFormatException e) {
      LOGGER.error("Cannot decompress Bytes");
    }
    return outputStream.toByteArray();
  }

  private <T> Collector<T, ?, T> toSinglePostCollector() {
    return Collectors.collectingAndThen(
        Collectors.toList(),
        list -> {
          if (list.size() != 1) {
            throw new IllegalArgumentException();
          }
          return list.get(0);
        }
    );

  }
}
