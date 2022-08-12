package com.sharing.photos.demo.facade;

import com.sharing.photos.demo.dto.UserDTO;
import com.sharing.photos.demo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

  public UserDTO userToUserDTO(User user){

    UserDTO userDTO = new UserDTO();
    userDTO.setId(user.getId());
    userDTO.setFirstname(user.getName());
    userDTO.setLastname(user.getLastname());
    userDTO.setUsername(user.getUsername());
    userDTO.setBio(user.getBio());
    userDTO.setEmail(user.getEmail());

    return userDTO;
  }


}
