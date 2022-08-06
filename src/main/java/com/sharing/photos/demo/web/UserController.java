package com.sharing.photos.demo.web;

import com.sharing.photos.demo.dto.UserDTO;
import com.sharing.photos.demo.entity.User;
import com.sharing.photos.demo.facade.UserFacade;
import com.sharing.photos.demo.service.UserService;
import com.sharing.photos.demo.validations.ResponseErrorValidation;
import java.security.Principal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/user")

public class UserController {

  @Autowired
  private UserService userService;
  @Autowired
  private UserFacade userFacade;
  @Autowired
  private ResponseErrorValidation responseErrorValidation;

  @GetMapping("/")
  public ResponseEntity<UserDTO> getCurrentUser(Principal principal){
    User user = userService.getCurrentUser(principal);
    UserDTO userDTO = userFacade.userToUserDTO(user);

    return new ResponseEntity<>(userDTO, HttpStatus.OK);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId){
    User user = userService.getUserById(Long.parseLong(userId));
    UserDTO userDTO = userFacade.userToUserDTO(user);

    return new ResponseEntity<>(userDTO, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Principal principal){
    ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
    if (!ObjectUtils.isEmpty(errors)) return errors;

    User user = userService.updateUser(userDTO, principal);
    UserDTO userUpdated = userFacade.userToUserDTO(user);
    return new ResponseEntity<>(userUpdated, HttpStatus.OK);

  }
}
