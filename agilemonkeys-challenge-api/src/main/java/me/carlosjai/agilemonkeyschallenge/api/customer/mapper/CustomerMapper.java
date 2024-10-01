package me.carlosjai.agilemonkeyschallenge.api.customer.mapper;

import me.carlosjai.agilemonkeyschallenge.api.customer.dto.CustomerDto;
import me.carlosjai.agilemonkeyschallenge.domain.customer.model.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * CustomerMapper is a MapStruct interface responsible for mapping between {@link CustomerEntity}
 * and {@link CustomerDto} objects. It is used to transform entity data from the persistence layer
 * into a DTO format for use in the service or presentation layers.
 *
 * <p>This interface is annotated with {@link Mapper} and uses
 * {@code componentModel = "spring"} to enable Spring integration, allowing the mapper to be
 * injected as a Spring bean.
 *
 * <p>Mapping details:
 * <ul>
 *   <li>{@code id} field in {@link CustomerDto} is mapped from the {@code sk} field
 *   in {@link CustomerEntity}.</li>
 * </ul>
 *
 * <p>Methods:
 * <ul>
 *   <li>{@link #mapToCustomerDto(CustomerEntity)}: Maps a {@link CustomerEntity}
 *   to a {@link CustomerDto}.</li>
 * </ul>
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {

  @Mapping(target = "id", source = "sk")
  CustomerDto mapToCustomerDto(CustomerEntity entity);
}
