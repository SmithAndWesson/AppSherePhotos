package com.sharing.photos.demo.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import lombok.Data;

@Data
@Entity
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  private Post post;
  @Column(nullable = false)
  private String username;
  @Column(nullable = false)
  private Long userId;
  @Lob
  @Column( nullable = false)
  private String message;
  @Column(updatable = false)
  private LocalDateTime currentDate;

  @PrePersist
  protected void onCreate(){
    this.currentDate = LocalDateTime.now();
  }

}
