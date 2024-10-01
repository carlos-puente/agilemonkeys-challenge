package me.carlosjai.agilemonkeyschallenge.api.util;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class FileUtil {

  public static String getExtension(final String fileName) {
    return Objects.isNull(fileName) || fileName.lastIndexOf('.') == -1 ? StringUtils.EMPTY :
        fileName.substring(fileName.lastIndexOf('.') + 1);
  }
}
