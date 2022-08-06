package com.sharing.photos.demo.repository;

import com.sharing.photos.demo.entity.Post;
import com.sharing.photos.demo.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  List<Post> findAllByUserOrderByCurrentDateDesc(User user);
  List<Post> findAllByOrderByCurrentDateDesc();
  Optional<Post> findPostByIdAndUser(Long id, User user);

}
