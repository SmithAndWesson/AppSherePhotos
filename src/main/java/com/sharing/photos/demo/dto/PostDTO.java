package com.sharing.photos.demo.dto;

import java.util.Set;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PostDTO {

  private Long id;
  @NotEmpty
  private String title;
  @NotEmpty
  private String caption;
  private String location;
  private String username;
  private Integer likes;
  private Set<String> usersLiked;
}
