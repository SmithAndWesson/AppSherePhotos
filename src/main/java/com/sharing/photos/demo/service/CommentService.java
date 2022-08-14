package com.sharing.photos.demo.service;

import com.sharing.photos.demo.dto.CommentDTO;
import com.sharing.photos.demo.entity.Comment;
import com.sharing.photos.demo.entity.Post;
import com.sharing.photos.demo.entity.User;
import com.sharing.photos.demo.exceptions.PostNotFoundException;
import com.sharing.photos.demo.repository.CommentRepository;
import com.sharing.photos.demo.repository.PostRepository;
import com.sharing.photos.demo.repository.UserRepository;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

  public static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);

  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  @Autowired
  public CommentService(CommentRepository commentRepository,
      UserRepository userRepository, PostRepository postRepository) {
    this.commentRepository = commentRepository;
    this.userRepository = userRepository;
    this.postRepository = postRepository;
  }

  public Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal) {

    User user = getUserByPrincipal(principal);
//    Post post = postRepository.findPostByIdAndUser(postId, user)
    Post post = postRepository.findPostById(postId)
        .orElseThrow(
            () -> new PostNotFoundException("Post can not be found for user : " + user.getEmail()));
    Comment comment = new Comment();
    comment.setPost(post);
    comment.setUserId(user.getId());
    comment.setUsername(user.getUsername());
    comment.setMessage(commentDTO.getMessage());

    LOGGER.info("Saving comment for Post: {}" + post.getId());
    return commentRepository.save(comment);

  }

  public List<Comment> getAllCommentsForPost(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new PostNotFoundException("Post can not be found"));
    List<Comment> comments = commentRepository.findAllByPost(post);

    return comments;
  }

  public void deleteComment(Long commentId){
    Optional<Comment> comment =  commentRepository.findById(commentId);
    comment.ifPresent(commentRepository::delete);
  }

  private User getUserByPrincipal(Principal principal) {
    String username = principal.getName();
    return userRepository.findUserByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("Username not found with username " + username));
  }
}
