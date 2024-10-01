package me.carlosjai.agilemonkeyschallenge.domain.exception;

import lombok.Getter;

public enum ErrorCodeEnum {
  AUTH_SIGNUP_EXISTING_USER("The specified user already exists."),
  AUTH_SIGNUP_WRONG_PASSWORD(
      "Password must have at least one uppercase letter, one lowercase letter, one number, one special character, and be at least 8 characters long"),
  CUSTOMER_PICTURE_NOT_VALID("There are not supported files: "),
  CUSTOMER_PICTURE_NOT_AVAILABLE(
      "The picture of the requested user is not available. Please upload it first."),
  CUSTOMER_NOT_FOUND("Customer not found with the given id."),
  CUSTOMER_EMAIL_ALREADY_EXISTS("The specified email is already being used."),
  CUSTOMER_ERROR_UPLOADING_PICTURE("Error uploading the picture: "),
  CUSTOMER_ERROR_DOWNLOADING_PICTURE("Error uploading the picture: "),
  USER_CREATE_USERNAME_ALREADY_EXISTS("The specified username is already being used."),
  USER_NOT_FOUND("User not found with the given id."),
  INVALID_ROLES("Invalid roles found: "),
  JWT_NOT_VALID("The JWT is not valid or is expired. Please login again."),
  OTHER("OTHER"),
  ACCESS_DENIED(
      "Access denied: You do not have the necessary permissions to view or interact with this resource."),
  VALIDATION("Error validating request");

  @Getter
  private final String errorMessage;

  ErrorCodeEnum(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
