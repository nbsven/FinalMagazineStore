package com.epam.magazinestore.services;

import com.epam.magazinestore.entity.Account;

/**
 * Service for working with accounts.
 */
public interface AccountService {

  /**
   * Register new {@code Account} in the database, if this not exist.
   *
   * @param account {@code Account}, that needs to be created.
   * @return saved {@code Account} if registering was success, and {@code null} in other cases.
   */
  Account registerAccount(Account account);

  /**
   * Delete {@code Account} from database, if {@code Account} exists.
   *
   * @param account {@code Account}, that needs to be deleted.
   */
  Account deleteAccount(Account account);

  /**
   * Returns true if account with specified username exists in database, otherwise returns false
   *
   * @param username username to be checked
   * @return true, if exists, false if not
   */
  boolean isUserNameExists(String username);

  /**
   * Retrieves account from database with specified username
   *
   * @param username account username
   * @return account object retrieved from db
   */
  Account getAccountByUsername(String username);

}
