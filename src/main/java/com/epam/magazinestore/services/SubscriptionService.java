package com.epam.magazinestore.services;

import com.epam.magazinestore.entity.Account;
import com.epam.magazinestore.entity.Subscription;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Service for working with subscriptions.
 */
public interface SubscriptionService {

  /**
   * Initialize new {@code Subscription}, if subscription not exists.
   *
   * @param accountId {@code Account}, that need to be subscribed.
   * @param magazineId for subscription.
   * @param duration Months of subscription.
   * @param offset start date offset from magazine publication date
   * @return {@code null} if initializing was not success, and the created {@code Subscription} in other cases.
   */
  Subscription subscribe(long accountId, long magazineId, int duration, int offset);

  /**
   * Cancel {@code Subscription} for account on magazine.
   *
   * @param subscriptionId {@code Subscription}, that needs to be unsubscribed.
   */
  void unsubscribe(long subscriptionId);

  /**
   * Prolong already existing {@code Subscription} for duration (in months)
   *
   * @param duration Months of subscription prolonging.
   * @return {@code null} if prolonging was not success, and the created {@code Subscription} in other cases.
   */
  Subscription prolongSubscription(Subscription subscription, int duration);

  Subscription prolongSubscription(long id, int duration);

  /**
   * Returns list of unpaid subscriptions of the specified account
   *
   * @param account account for which necessary to return
   * @param page zero-based page index
   * @param pageSize the size of the page to be returned
   * @return list of unpaid subscriptions of the specified account
   */
  List<Subscription> getUnpaidSubscriptionsByAccount(Account account, int page, int pageSize);

  Page<Subscription> getSubscriptionsByAccountId(long accountId, int startRow, int pageSize, String search,
                                                 String orderDirection);
}
