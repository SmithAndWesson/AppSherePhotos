package com.sharing.photos.demo.repository;

import com.sharing.photos.demo.entity.Comment;
import com.sharing.photos.demo.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findAllByPost(Post post);
  Comment findByIdAndUserId(Long commentId, Long userId);

}
