package com.epam.magazinestore.services.impl;

import com.epam.magazinestore.entity.Account;
import com.epam.magazinestore.entity.Publisher;
import com.epam.magazinestore.entity.Role;
import com.epam.magazinestore.exception.ServiceException;
import com.epam.magazinestore.repository.PublisherRepository;
import com.epam.magazinestore.services.PublisherService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

  private final PublisherRepository publisherRepository;

  @Override
  public Publisher addPublisher(Publisher publisher, Account account) {
    if (!isAuthorizedAsAdmin(account)) {
      throw new ServiceException("Access denied");
    }
    Publisher savedPublisher;
    try {
      savedPublisher = publisherRepository.save(publisher);
    } catch (DataAccessException exception) {
      log.error(exception.getMessage(), exception);
      throw new ServiceException("Database error");
    }
    return savedPublisher;
  }

  @Override
  public Publisher removePublisher(Publisher publisher, Account account) {
    if (!isAuthorizedAsAdmin(account)) {
      throw new ServiceException("Access denied");
    }
    try {
      publisherRepository.delete(publisher);
    } catch (DataAccessException exception) {
      log.error(exception.getMessage(), exception);
      throw new ServiceException("Database error");
    }
    return publisher;
  }

  @Override
  @Transactional
  public Publisher removePublisher(long publisherId, Account account) {
    if (!isAuthorizedAsAdmin(account)) {
      throw new ServiceException("Access denied");
    }
    Publisher publisher;
    try {
      publisher = publisherRepository.findOne(publisherId);
      publisherRepository.delete(publisher);
    } catch (DataAccessException exception) {
      log.error(exception.getMessage(), exception);
      throw new ServiceException("Database error");
    }
    return publisher;
  }

  @Override
  public Publisher editPublisher(Publisher publisher, Account account) {
    if (!isAuthorizedAsAdmin(account)) {
      throw new ServiceException("Access denied");
    }
    Publisher savedPublisher;
    try {
      savedPublisher = publisherRepository.save(publisher);
    } catch (DataAccessException exception) {
      log.error(exception.getMessage(), exception);
      throw new ServiceException("Database error", exception);
    }
    return savedPublisher;
  }

  @Override
  @Transactional
  public List<Publisher> getAllPublishers(Account account) {
    List<Publisher> publishers;
    try {
      publishers = publisherRepository.findAll();
    } catch (DataAccessException exception) {
      log.error(exception.getMessage(), exception);
      throw new ServiceException("Database error", exception);
    }
    return publishers;
  }

  /**
   * Returns true if account has admin rights
   *
   * @param account account to be checked
   * @return true, if specified account has admin role
   */
  private boolean isAuthorizedAsAdmin(Account account) {
    return account.getRole() == Role.ROLE_ADMIN;
  }


}
