package me.carlosjai.agilemonkeyschallenge.api.customer.validation;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "content.extensions")
@Data
public class ContentTypeConfiguration {

  private final List<String> contentTypes;
  private final List<String> fileExtensions;
}
