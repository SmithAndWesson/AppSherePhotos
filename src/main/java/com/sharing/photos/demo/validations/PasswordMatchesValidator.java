package com.sharing.photos.demo.validations;

import com.sharing.photos.demo.annotations.PasswordMatches;
import com.sharing.photos.demo.payload.request.SignupRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

  @Override
  public void initialize(PasswordMatches constraintAnnotation) {

  }

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
    SignupRequest userSignupRequest = (SignupRequest) obj;
    return userSignupRequest.getPassword().equals(userSignupRequest.getConfirmPassword());
  }
}
