package com.sharing.photos.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sharing.photos.demo.entity.enums.ERole;
import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;
  @Column(unique = true, updatable = true)
  private String lastname;
  @Column(nullable = false)
  private String username;
  @Column(unique = true)
  private String email;
  @Lob
  @Column()
  private String bio;
  @Column(length = 3000)
  private String password;

  @ElementCollection(targetClass = ERole.class)
  @CollectionTable(name = "user_role", joinColumns = @JoinColumn(columnDefinition = "user_id"))
  private Set<ERole> roles = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
  private List<Post> posts = new ArrayList<>();

  @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
  @Column(updatable = false)
  private LocalDateTime createdDate;

  @Transient
  private Collection<? extends GrantedAuthority> authorities;

  public User(Long id, String username, String email, String password,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  public User() {
  }

  @PrePersist
  protected void onCreate(){
    this.createdDate = LocalDateTime.now();
  }

  /**
    *SECURITY
   **/
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
