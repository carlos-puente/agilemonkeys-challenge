package me.carlosjai.agilemonkeyschallenge.api.customer.validation;

import static me.carlosjai.agilemonkeyschallenge.api.util.FileUtil.getExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

@Component
@Data
public class ContentTypeValidator {

  private final ContentTypeConfiguration contentTypeConfiguration;

  public List<String> validate(final MultiValueMap<String, MultipartFile> fileParams) {
    List<String> nonValidValues = new ArrayList<>();
    if (MapUtils.isNotEmpty(fileParams)) {
      nonValidValues = fileParams.values().stream().map(this::nonValidValues)
          .flatMap(Collection::stream)
          .toList();
    }
    return nonValidValues;
  }

  private List<String> nonValidValues(final Collection<MultipartFile> files) {
    return files.stream().filter(file -> contentTypeConfiguration.getContentTypes()
            .stream().noneMatch(contentType -> contentType.equalsIgnoreCase(file.getContentType())))
        .filter(fileName -> contentTypeConfiguration.getFileExtensions().stream().noneMatch(
            extension -> StringUtils.isNotBlank(fileName.getOriginalFilename())
                && extension.equalsIgnoreCase(getExtension(fileName.getOriginalFilename()))
        )).map(MultipartFile::getOriginalFilename).toList();
  }


}
