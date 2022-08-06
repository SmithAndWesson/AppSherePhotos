package com.sharing.photos.demo.facade;

import com.sharing.photos.demo.dto.CommentDTO;
import com.sharing.photos.demo.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {

  public CommentDTO commentToCommentDTO(Comment comment){
    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setId(comment.getId());
    commentDTO.setMessage(comment.getMessage());
    commentDTO.setUsername(comment.getUsername());

    return commentDTO;
  }
}
