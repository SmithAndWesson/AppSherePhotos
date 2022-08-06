package com.sharing.photos.demo.service;

import com.sharing.photos.demo.dto.PostDTO;
import com.sharing.photos.demo.entity.ImageModel;
import com.sharing.photos.demo.entity.Post;
import com.sharing.photos.demo.entity.User;
import com.sharing.photos.demo.exceptions.PostNotFoundException;
import com.sharing.photos.demo.repository.ImageRepository;
import com.sharing.photos.demo.repository.PostRepository;
import com.sharing.photos.demo.repository.UserRepository;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PostService {

  public static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final ImageRepository imageRepository;

  @Autowired
  public PostService(PostRepository postRepository,
      UserRepository userRepository,
      ImageRepository imageRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.imageRepository = imageRepository;
  }

  public Post createPost(PostDTO postDTO, Principal principal) {
    User user = getUserByPrincipal(principal);
    Post post = new Post();
    post.setUser(user);
    post.setCaption(postDTO.getCaption());
    post.setTitle(postDTO.getTitle());
    post.setLocation(postDTO.getLocation());
    LOGGER.info("Saving post got user : {}" + user.getEmail());

    return postRepository.save(post);
  }

  public List<Post> getAllPosts() {
    return postRepository.findAllByOrderByCurrentDateDesc();
  }

  public Post getPostById(Long id, Principal principal) {
    User user = getUserByPrincipal(principal);
    return postRepository.findPostByIdAndUser(id, user)
        .orElseThrow(() -> new PostNotFoundException(
            "Post can not be found for username: " + user.getEmail()));
  }

  public List<Post> getAllPostForUser(Principal principal) {
    User user = getUserByPrincipal(principal);
    return postRepository.findAllByUserOrderByCurrentDateDesc(user);
  }

  public Post likePost(Long postId, String username) {
    Post post = postRepository.findById(postId).
        orElseThrow(() -> new PostNotFoundException("Post can not be found"));

    Optional<String> userLiked = post.getLikedUsers()
        .stream()
        .filter(u -> u.equals(username))
        .findAny();
    if (userLiked.isPresent()) {
      post.setLikes(post.getLikes() - 1);
      post.getLikedUsers().remove(username);
    } else {
      post.setLikes(post.getLikes() + 1);
      post.getLikedUsers().add(username);
    }

    return postRepository.save(post);
  }

  public void deletePost(Long postId, Principal principal){
    Post post = getPostById(postId, principal);
    Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId());
    postRepository.delete(post);

    imageModel.ifPresent(imageRepository::delete);
  }

  private User getUserByPrincipal(Principal principal) {
    String username = principal.getName();
    return userRepository.findUserByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("Username not found with username " + username));
  }
}
