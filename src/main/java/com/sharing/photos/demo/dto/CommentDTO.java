package com.sharing.photos.demo.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDTO {

  private Long id;
  @NotEmpty
  private String message;
  private String username;

}
