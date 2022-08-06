package com.sharing.photos.demo.facade;

import com.sharing.photos.demo.dto.PostDTO;
import com.sharing.photos.demo.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {

  public PostDTO postToPostDTO(Post post){
    PostDTO postDTO = new PostDTO();
    postDTO.setUsername(post.getUser().getUsername());
    postDTO.setId(post.getId());
    postDTO.setCaption(post.getCaption());
    postDTO.setLikes(post.getLikes());
    postDTO.setUsersLiked(post.getLikedUsers());
    postDTO.setLocation(post.getLocation());
    postDTO.setTitle(post.getTitle());

    return postDTO;

  }

}
