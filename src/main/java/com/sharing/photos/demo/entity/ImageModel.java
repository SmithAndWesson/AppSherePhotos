package com.sharing.photos.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

@Data
@Entity
public class ImageModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;
  @Lob
  @Column(nullable = false)
  private byte[] imageBytes;
  @JsonIgnore
  private Long userId;
  @JsonIgnore
  private Long postId;

}
