package com.epam.magazinestore.services;

import com.epam.magazinestore.entity.Account;
import com.epam.magazinestore.entity.Publisher;
import java.util.List;

/**
 * Service for working with publishers.
 */
public interface PublisherService {

  /**
   * Register new {@code Publisher} in database, if publisher not exists.
   *
   * @param publisher {@code Publisher} need to be registered.
   * @param account current auditor
   * @return saved {@code Publisher} if adding was success, and {@code null} in other cases.
   */
  Publisher addPublisher(Publisher publisher, Account account);

  /**
   * Removes publisher from database
   *
   * @param publisher publisher to be deleted
   * @param account current auditor
   * @return removed publisher
   */
  Publisher removePublisher(Publisher publisher, Account account);

  /**
   * Removes publisher from database
   *
   * @param publisherId publisher id to be deleted
   * @param account current auditor
   * @return removed publisher
   */
  Publisher removePublisher(long publisherId, Account account);

  /**
   * Change {@code Publisher}.
   *
   * @param publisher {@code Publisher} that number needs to be changed.
   * @param account current auditor
   * @return updated publisher
   */
  Publisher editPublisher(Publisher publisher, Account account);

  /**
   * Returns list of available publishers
   *
   * @param account current auditor
   * @return list of publishers
   */
  List<Publisher> getAllPublishers(Account account);

}
