package com.sharing.photos.demo.service;

import com.sharing.photos.demo.dto.UserDTO;
import com.sharing.photos.demo.entity.User;
import com.sharing.photos.demo.entity.enums.ERole;
import com.sharing.photos.demo.exceptions.UserExistException;
import com.sharing.photos.demo.payload.request.SignupRequest;
import com.sharing.photos.demo.repository.UserRepository;
import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User creatUser(SignupRequest userIn){
    User user = new User();
    user.setEmail(userIn.getEmail());
    user.setName(userIn.getFirstname());
    user.setLastname(userIn.getLastname());
    user.setUsername(userIn.getUsername());
    user.setPassword(passwordEncoder.encode(userIn.getPassword()));
    user.getRoles().add(ERole.ROLE_USER);

    try {
      LOGGER.info("Saving user {}", userIn.getEmail());
      return userRepository.save(user);
    }catch (Exception ex){
      LOGGER.error("Error during registration. {}", ex.getMessage());
      throw new UserExistException("The user " + user.getUsername() + " already exist. Please chek credentials.");
    }
  }

  public User updateUser(UserDTO userDTO, Principal principal){
    User user = getUserByPrincipal(principal);
    user.setName(userDTO.getFirstname());
    user.setLastname(userDTO.getLastname());
    user.setBio(userDTO.getBio());
    return userRepository.save(user);
  }

  public User getCurrentUser(Principal principal){
    return getUserByPrincipal(principal);
  }

  private User getUserByPrincipal(Principal principal){
    String username = principal.getName();
    return userRepository.findUserByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
  }

  public User getUserById(Long userId) {
    return  userRepository.findUserById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
