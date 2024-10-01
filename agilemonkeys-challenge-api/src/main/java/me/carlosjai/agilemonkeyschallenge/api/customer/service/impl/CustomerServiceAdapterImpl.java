package me.carlosjai.agilemonkeyschallenge.api.customer.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import me.carlosjai.agilemonkeyschallenge.api.base.response.BasePageResponse;
import me.carlosjai.agilemonkeyschallenge.api.base.response.PageMetaData;
import me.carlosjai.agilemonkeyschallenge.api.customer.dto.CustomerDto;
import me.carlosjai.agilemonkeyschallenge.api.customer.mapper.CustomerMapper;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerCreateRequest;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerSearchRequest;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerUpdateRequest;
import me.carlosjai.agilemonkeyschallenge.api.customer.service.CustomerServiceAdapter;
import me.carlosjai.agilemonkeyschallenge.api.util.FileUtil;
import me.carlosjai.agilemonkeyschallenge.domain.constants.Constants;
import me.carlosjai.agilemonkeyschallenge.domain.customer.model.entity.CustomerEntity;
import me.carlosjai.agilemonkeyschallenge.domain.customer.model.entity.QCustomerEntity;
import me.carlosjai.agilemonkeyschallenge.domain.customer.model.service.CustomerService;
import me.carlosjai.agilemonkeyschallenge.domain.exception.CustomResponseStatusException;
import me.carlosjai.agilemonkeyschallenge.domain.exception.ErrorCodeEnum;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class CustomerServiceAdapterImpl implements CustomerServiceAdapter {

  private static final Logger log = LoggerFactory.getLogger(CustomerServiceAdapterImpl.class);
  private final CustomerService service;
  private final CustomerMapper mapper;

  @Override
  public void deleteCustomer(final String customerId) {
    if (log.isDebugEnabled()) {
      log.debug("deleteCustomer: {}", customerId);
    }
    final var entity = getCustomerEntity(customerId);
    if (entity.isPresent()) {
      service.delete(entity.get());
    }
  }

  @Override
  public CustomerDto getCustomerDetail(final String customerId) {
    if (log.isDebugEnabled()) {
      log.debug("getCustomerDetail: {}", customerId);
    }
    final var optionalEntity = getCustomerEntity(customerId);
    if (optionalEntity.isPresent()) {
      return mapper.mapToCustomerDto(optionalEntity.get());
    }
    throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCodeEnum.CUSTOMER_NOT_FOUND);

  }

  @Override
  public BasePageResponse<CustomerDto> getAllCustomers(
      final CustomerSearchRequest customerSearchRequest) {
    if (log.isDebugEnabled()) {
      log.debug("getAllCustomers: {}", customerSearchRequest);
    }
    final var predicate = genereratePredicate(customerSearchRequest);
    final var pageable = getPageable(customerSearchRequest);
    final var pageEntities = service.findAllBy(predicate, pageable);
    final var content = pageEntities.getContent().stream().map(mapper::mapToCustomerDto).toList();
    final var metaData = PageMetaData.builder().currentPage(pageEntities.getNumber() + 1)
        .pageSize(pageEntities.getSize())
        .totalPages(pageEntities.getTotalPages())
        .totalElements(pageEntities.getTotalElements())
        .build();
    return BasePageResponse.<CustomerDto>builder().content(content).metaData(metaData).build();
  }

  @Override
  public CustomerDto createCustomer(final CustomerCreateRequest createRequest) {
    if (log.isDebugEnabled()) {
      log.debug("createCustomer: {}", createRequest);
    }
    final var optExistingEntity = service.findByEmail(createRequest.getEmail());
    if (optExistingEntity.isPresent()) {
      throw new CustomResponseStatusException(HttpStatus.CONFLICT,
          ErrorCodeEnum.CUSTOMER_EMAIL_ALREADY_EXISTS);
    } else {
      final var newEntity = new CustomerEntity();
      newEntity.setEmail(createRequest.getEmail());
      newEntity.setFirstName(createRequest.getFirstName());
      newEntity.setLastName(createRequest.getLastName());
      newEntity.setPhoneNumber(createRequest.getPhoneNumber());

      return mapper.mapToCustomerDto(service.save(newEntity));
    }
  }

  @Override
  public CustomerDto updateCustomer(CustomerUpdateRequest updateRequest) {
    if (log.isDebugEnabled()) {
      log.debug("updateCustomer: {}", updateRequest);
    }
    final var optionalEntity = getCustomerEntity(updateRequest.getId());
    if (optionalEntity.isPresent()) {
      var entityToUpdate = optionalEntity.get();
      if (StringUtils.isNotBlank(updateRequest.getEmail())) {
        entityToUpdate.setEmail(updateRequest.getEmail());
      }
      if (StringUtils.isNotBlank(updateRequest.getFirstName())) {
        entityToUpdate.setFirstName(updateRequest.getFirstName());
      }
      if (StringUtils.isNotBlank(updateRequest.getLastName())) {
        entityToUpdate.setLastName(updateRequest.getLastName());
      }
      if (StringUtils.isNotBlank(updateRequest.getPhoneNumber())) {
        entityToUpdate.setPhoneNumber(updateRequest.getPhoneNumber());
      }

      return mapper.mapToCustomerDto(service.save(entityToUpdate));
    }
    throw new CustomResponseStatusException(HttpStatus.NOT_FOUND,
        ErrorCodeEnum.CUSTOMER_NOT_FOUND);
  }

  @Override
  public void uploadCustomerPicture(MultiValueMap<String, MultipartFile> allFileParams,
      String customerId) {
    if (log.isDebugEnabled()) {
      log.debug("uploadCustomerPicture: {}", customerId);
    }
    final var optionalEntity = getCustomerEntity(customerId);
    if (optionalEntity.isPresent()) {
      try {
        uploadPicture(allFileParams, optionalEntity.get());
        return;
      } catch (IOException e) {
        throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCodeEnum.CUSTOMER_ERROR_UPLOADING_PICTURE.getErrorMessage() + e.getMessage(),
            ErrorCodeEnum.CUSTOMER_ERROR_UPLOADING_PICTURE.name());
      }
    }
    throw new CustomResponseStatusException(HttpStatus.NOT_FOUND,
        ErrorCodeEnum.CUSTOMER_NOT_FOUND);
  }

  @Override
  public void downloadCustomerPicture(String customerId, HttpServletResponse response) {
    if (log.isDebugEnabled()) {
      log.debug("downloadCustomerPicture: {}", customerId);
    }
    final var optionalEntity = getCustomerEntity(customerId);
    if (optionalEntity.isPresent()) {
      try {
        downloadCustomerPicture(optionalEntity.get(), response);
      } catch (IOException e) {
        throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCodeEnum.CUSTOMER_ERROR_DOWNLOADING_PICTURE.getErrorMessage() + e.getMessage(),
            ErrorCodeEnum.CUSTOMER_ERROR_DOWNLOADING_PICTURE.name());
      }
      return;
    }
    throw new CustomResponseStatusException(HttpStatus.NOT_FOUND,
        ErrorCodeEnum.CUSTOMER_NOT_FOUND);
  }

  private static void downloadCustomerPicture(CustomerEntity entity, HttpServletResponse response)
      throws IOException {
    if (log.isDebugEnabled()) {
      log.debug("downloadCustomerPicture: {}", entity.getSk());
    }
    if (StringUtils.isNotEmpty(entity.getPicture()) && ArrayUtils.isNotEmpty(
        entity.getPictureBytes())) {
      final var contentDisposition = ContentDisposition.builder("attachment")
          .filename(entity.getPicture()).build();
      response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
      response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
      response.setHeader(HttpHeaders.CACHE_CONTROL,
          CacheControl.maxAge(Constants.MAX_AGE, TimeUnit.DAYS).cachePublic().mustRevalidate()
              .getHeaderValue());
      response.setContentLength(entity.getPictureBytes().length);
      response.getOutputStream().write(entity.getPictureBytes());
      response.getOutputStream().flush();
    } else {
      throw new CustomResponseStatusException(HttpStatus.NOT_FOUND,
          ErrorCodeEnum.CUSTOMER_PICTURE_NOT_AVAILABLE);
    }
  }

  public void uploadPicture(MultiValueMap<String, MultipartFile> allFileParams,
      CustomerEntity entity) throws IOException {
    if (log.isDebugEnabled()) {
      log.debug("uploadPicture: {}", entity.getSk());
    }
    if (Objects.nonNull(allFileParams.get(Constants.PICTURE))) {
      final var picture = allFileParams.get(Constants.PICTURE).get(0);
      entity.setPicture(
          "customer" + entity.getSk() + "." + FileUtil.getExtension(picture.getOriginalFilename()));
      entity.setPictureBytes(picture.getBytes());
      service.save(entity);
    }
  }

  private static Pageable getPageable(final CustomerSearchRequest customerSearchRequest) {
    if (log.isDebugEnabled()) {
      log.debug("getPageable: {}", customerSearchRequest);
    }
    final var pageSize = Integer.valueOf(
        StringUtils.defaultIfBlank(customerSearchRequest.getPageSize(),
            Constants.DEFAULT_PAGE_SIZE));
    final var currentPage = Integer.valueOf(
        StringUtils.defaultIfBlank(customerSearchRequest.getCurrentPage(),
            Constants.DEFAULT_CURRENT_PAGE));
    return PageRequest.of(currentPage - 1, pageSize, createSort(customerSearchRequest));
  }

  private static Sort createSort(final CustomerSearchRequest customerSearchRequest) {
    if (log.isDebugEnabled()) {
      log.debug("createSort: {}", customerSearchRequest);
    }
    var sort = Sort.by(Sort.Order.asc(Constants.SORT_BY_LAST_NAME),
        Sort.Order.asc(Constants.SORT_BY_FIRST_NAME), Sort.Order.asc(Constants.SORT_BY_EMAIL));
    if (StringUtils.isNotBlank(customerSearchRequest.getSortBy())) {
      if (Constants.ID_PARAM.equalsIgnoreCase(customerSearchRequest.getSortBy())) {
        customerSearchRequest.setSortBy(Constants.SK);
      }
      sort = generateCustomSortBy(customerSearchRequest.getSortBy(),
          customerSearchRequest.getSortOrder());
    }
    return sort;
  }

  private static Sort generateCustomSortBy(final String sortBy, final String sortOrder) {
    if (log.isDebugEnabled()) {
      log.debug("generateCustomSortBy: {} {}", sortBy, sortOrder);
    }
    return StringUtils.isNotBlank(sortOrder)
        && StringUtils.equalsIgnoreCase(sortOrder, Constants.DEFAULT_SORT_ORDER) ?
        Sort.by(Order.asc(sortBy)) : Sort.by(Order.desc(sortBy));

  }

  private static Predicate genereratePredicate(final CustomerSearchRequest customerSearchRequest) {
    if (log.isDebugEnabled()) {
      log.debug("genereratePredicate: {}", customerSearchRequest);
    }
    final var andBuilder = new BooleanBuilder();
    QCustomerEntity qCustomerEntity = QCustomerEntity.customerEntity;
    if (StringUtils.isNotEmpty(customerSearchRequest.getSearchTerm())) {
      final var orBuilder = new BooleanBuilder();
      orBuilder
          .or(qCustomerEntity.firstName.contains(customerSearchRequest.getSearchTerm()))
          .or(qCustomerEntity.lastName.contains(customerSearchRequest.getSearchTerm()))
          .or(qCustomerEntity.email.contains(customerSearchRequest.getSearchTerm()));
      andBuilder.and(orBuilder);
    }
    if (Objects.nonNull(customerSearchRequest.getId())) {
      andBuilder.and(qCustomerEntity.sk.eq(customerSearchRequest.getId()));
    }
    if (StringUtils.isNotEmpty(customerSearchRequest.getFirstName())) {
      andBuilder.and(qCustomerEntity.firstName.eq(customerSearchRequest.getFirstName()));
    }
    if (StringUtils.isNotEmpty(customerSearchRequest.getLastName())) {
      andBuilder.and(qCustomerEntity.lastName.eq(customerSearchRequest.getLastName()));
    }
    if (StringUtils.isNotEmpty(customerSearchRequest.getEmail())) {
      andBuilder.and(qCustomerEntity.email.eq(customerSearchRequest.getEmail()));
    }
    if (StringUtils.isNotEmpty(customerSearchRequest.getPhoneNumber())) {
      andBuilder.and(qCustomerEntity.phoneNumber.eq(customerSearchRequest.getPhoneNumber()));
    }

    return andBuilder;
  }

  private Optional<CustomerEntity> getCustomerEntity(final String customerId) {
    return getCustomerEntity(Long.valueOf(customerId));
  }

  private Optional<CustomerEntity> getCustomerEntity(final Long customerId) {
    return service.getById(customerId);
  }

}
