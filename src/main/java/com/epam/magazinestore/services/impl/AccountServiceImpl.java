package com.epam.magazinestore.services.impl;

import com.epam.magazinestore.entity.Account;
import com.epam.magazinestore.exception.ServiceException;
import com.epam.magazinestore.repository.AccountRepository;
import com.epam.magazinestore.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;

  @Override
  public Account registerAccount(Account account) {
    Account savedAccount;
    try {
      savedAccount = accountRepository.save(account);
    } catch (DataIntegrityViolationException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Could not register account: account with such username already exists", e);
    } catch (InvalidDataAccessApiUsageException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Account can't be null", e);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }

    return savedAccount;
  }

  @Override
  public Account deleteAccount(Account account) {
    if (!isUserNameExists(account.getUsername())) {
      throw new ServiceException("Could not delete account: account not found");
    }
    try {
      accountRepository.delete(account);
    } catch (InvalidDataAccessApiUsageException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Account can't be null", e);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return account;
  }

  @Override
  public boolean isUserNameExists(String username) {
    boolean exists;
    if (username == null) {
      throw new ServiceException("Username can't be null");
    }
    try {
      exists = accountRepository.existsByUsername(username);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return exists;
  }

  @Override
  public Account getAccountByUsername(String username) {
    Account account;
    if (username == null) {
      throw new ServiceException("Username can't be null");
    }
    try {
      account = accountRepository.findByUsername(username);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return account;
  }

}
