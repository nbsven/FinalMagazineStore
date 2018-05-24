package com.epam.magazinestore.services.impl;

import static java.util.Objects.isNull;

import com.epam.magazinestore.entity.Account;
import com.epam.magazinestore.entity.Magazine;
import com.epam.magazinestore.entity.Payment;
import com.epam.magazinestore.entity.Subscription;
import com.epam.magazinestore.exception.ServiceException;
import com.epam.magazinestore.repository.AccountRepository;
import com.epam.magazinestore.repository.MagazineRepository;
import com.epam.magazinestore.repository.SubscriptionRepository;
import com.epam.magazinestore.services.PaymentService;
import com.epam.magazinestore.services.SubscriptionService;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;

  private final PaymentService paymentService;

  private final AccountRepository accountRepository;

  private final MagazineRepository magazineRepository;

  @Override
  @Transactional
  public Subscription subscribe(long accountId, long magazineId, int duration, int offset) {
    if (accountId <= 0) {
      throw new ServiceException("Account id can't be less than zero");
    }
    if (magazineId <= 0) {
      throw new ServiceException("Magazine id can't be less than zero");
    }
    if (duration <= 0) {
      throw new ServiceException("Duration can't be less than zero");
    }
    Magazine magazine = magazineRepository.findOne(magazineId);
    Account account = accountRepository.findOne(accountId);
    LocalDate startDate = getStartDate(magazine, offset);
    LocalDate endDate = startDate.plusMonths(duration);
    Subscription subscription = Subscription.builder()
        .account(account)
        .endDate(endDate)
        .magazine(magazine)
        .startDate(startDate)
        .build();
    account.addSubscription(subscription);
    try {
      saveOrUpdateSubscription(subscription, startDate, endDate);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }

    return subscription;
  }

  @Override
  @Transactional
  public void unsubscribe(long subscriptionId) {
    Subscription subscription = subscriptionRepository.findOne(subscriptionId);

    if (isNull(subscription)) {
      throw new ServiceException("Subscription not found");
    }
    subscriptionRepository.delete(subscriptionId);
  }

  @Override
  @Transactional
  public Subscription prolongSubscription(Subscription subscription, int duration) {
    if (isNull(subscription)) {
      throw new ServiceException("Subscription can't be null");
    }
    if (duration <= 0) {
      throw new ServiceException("Duration can't be less than zero");
    }
    LocalDate oldDate = subscription.getEndDate();
    LocalDate newDate = oldDate.plusMonths(duration);
    subscription.setEndDate(newDate);
    try {
      saveOrUpdateSubscription(subscription, oldDate, newDate);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error");
    }
    return subscription;
  }

  private void saveOrUpdateSubscription(Subscription subscription, LocalDate oldDate, LocalDate newDate) {
    int price = subscription.getMagazine().getPrice();
    int cost = getSubscriptionCost(oldDate, newDate, price);
    Payment payment = paymentService.registerPayment(cost);
    subscription.setPayment(payment);
    payment.setSubscription(subscription);
    subscriptionRepository.save(subscription);
  }

  @Override
  public Subscription prolongSubscription(long subscriptionId, int duration) {
    if (duration <= 0) {
      throw new ServiceException("Duration can't be less than zero");
    }
    Subscription subscription = subscriptionRepository.findOne(subscriptionId);
    LocalDate oldDate = subscription.getEndDate();
    subscription.setEndDate(oldDate.plusMonths(duration));
    int price = subscription.getMagazine().getPrice();
    try {
      Payment payment = paymentService.registerPayment(duration * price);
      subscription.setPayment(payment);
      payment.setSubscription(subscription);
      subscriptionRepository.save(subscription);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error");
    }
    return subscription;
  }

  @Override
  public List<Subscription> getUnpaidSubscriptionsByAccount(Account account, int page, int pageSize) {
    Page<Subscription> subscriptions;
    if (isNull(account)) {
      throw new ServiceException("Account can't be null");
    }
    if (page < 0 || pageSize < 0) {
      throw new ServiceException("Page index or page size can't be negative");
    }
    try {
      subscriptions =
          subscriptionRepository.findAllByPaymentPaidIsFalseAndAccountId(account.getId(),
              new PageRequest(page, pageSize));
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error");
    }
    return subscriptions.getContent();
  }

  @Override
  public Page<Subscription> getSubscriptionsByAccountId(long accountId, int startRow, int pageSize, String search,
                                                        String orderDirection) {
    Page<Subscription> subscriptions;
    if (startRow < 0 || pageSize < 0) {
      throw new ServiceException("Page start row or page size can't be negative");
    }
    try {
      Direction direction = Direction.fromString(orderDirection);
      int page = getPageNumber(startRow, pageSize);
      PageRequest pageRequest = new PageRequest(page, pageSize, direction, "endDate");
      if (search.length() > 0) {
        subscriptions = subscriptionRepository.matchByMagazineNameOrPublisherName(accountId, search, pageRequest);
      } else {
        subscriptions =
            subscriptionRepository.findAllByAccountId(accountId, pageRequest);
      }
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error");
    }
    return subscriptions;
  }

  private LocalDate getStartDate(Magazine magazine, int monthOffset) {
    LocalDate localDate = LocalDate.now();
    return localDate.withDayOfMonth(magazine.getPublicationDate()).plusMonths(monthOffset);
  }

  private int getPageNumber(int start, int length) {
    int tmp;
    if (start < length) {
      tmp = start;
    } else {
      if ((start % length) == 0) {
        tmp = start / length;
      } else {
        tmp = start % length;
      }
    }
    return tmp;
  }

  private int getSubscriptionCost(LocalDate start, LocalDate end, int pricePerMonth) {
    Period period = Period.between(start, end);
    int months = period.getYears() * 12 + period.getMonths();
    return months * pricePerMonth;
  }

}
