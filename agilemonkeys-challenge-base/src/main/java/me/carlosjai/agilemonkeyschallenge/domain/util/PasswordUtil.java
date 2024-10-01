package me.carlosjai.agilemonkeyschallenge.domain.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.carlosjai.agilemonkeyschallenge.domain.constants.Constants;
import me.carlosjai.agilemonkeyschallenge.domain.exception.CustomResponseStatusException;
import me.carlosjai.agilemonkeyschallenge.domain.exception.ErrorCodeEnum;
import org.springframework.http.HttpStatus;

public final class PasswordUtil {

  public static void validatePassword(String password) {
    Pattern pattern = Pattern.compile(Constants.REGEX_PASSWORD, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(password);
    if (!matcher.matches()) {
      throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST,
          ErrorCodeEnum.AUTH_SIGNUP_WRONG_PASSWORD);
    }
  }

}
