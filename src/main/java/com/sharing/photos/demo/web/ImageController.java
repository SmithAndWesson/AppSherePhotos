package com.sharing.photos.demo.web;

import com.sharing.photos.demo.entity.ImageModel;
import com.sharing.photos.demo.payload.response.MessageResponse;
import com.sharing.photos.demo.service.ImageUploadService;
import com.sharing.photos.demo.validations.ResponseErrorValidation;
import java.io.IOException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@CrossOrigin
public class ImageController {

  @Autowired
  private ImageUploadService imageUploadService;

  @PostMapping("/upload")
  public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile file,
      Principal principal)
      throws IOException {
    imageUploadService.uploadImageToUser(file, principal);

    return new ResponseEntity<>(new MessageResponse("Image uploaded succefully."), HttpStatus.OK);
  }

  @PostMapping("/{postId}/upload")//postId
  public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") String postId,
      @RequestParam("file") MultipartFile file, Principal principal) throws IOException {
    imageUploadService.uploadImageToPost(file, principal, Long.parseLong(postId));

    return new ResponseEntity<>(new MessageResponse("Image uploaded succefully."), HttpStatus.OK);
  }

  @GetMapping("/profileImage")
  public ResponseEntity<ImageModel> getImageForUser(Principal principal){
    ImageModel userImageModel = imageUploadService.getImageToUser(principal);

    return new ResponseEntity<>(userImageModel, HttpStatus.OK);
  }

  @GetMapping("/{postId}/image")
  public ResponseEntity<ImageModel> getImageForPost(@PathVariable("postId") String postId){
    ImageModel imageModel = imageUploadService.getImageToPost(Long.parseLong(postId));

    return new ResponseEntity<>(imageModel, HttpStatus.OK);
  }
}
