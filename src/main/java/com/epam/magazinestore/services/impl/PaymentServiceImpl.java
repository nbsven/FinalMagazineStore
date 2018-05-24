package com.epam.magazinestore.services.impl;

import static java.util.Objects.isNull;

import com.epam.magazinestore.entity.Payment;
import com.epam.magazinestore.entity.Subscription;
import com.epam.magazinestore.exception.ServiceException;
import com.epam.magazinestore.repository.PaymentRepository;
import com.epam.magazinestore.services.PaymentService;
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
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;

  @Override
  public Payment registerPayment(int cost) {
    Payment savedPayment;
    Payment payment = new Payment(cost);
    try {
      savedPayment = paymentRepository.save(payment);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return savedPayment;
  }

  @Override
  public Payment registerPayment(Subscription subscription) {
    if (isNull(subscription)) {
      throw new ServiceException("Subscription can't be null");
    }
    if (isNull(subscription.getMagazine())) {
      throw new ServiceException("Magazine field must be set");
    }
    Period period = Period.between(subscription.getStartDate(), subscription.getEndDate());
    int months = period.getYears() * 12 + period.getMonths();
    int pricePerMonth = subscription.getMagazine().getPrice();
    return registerPayment(months * pricePerMonth);
  }

  @Override
  public void acceptPayment(Payment payment) {
    payment.setPaid(true);
    try {
      paymentRepository.save(payment);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
  }

  @Override
  @Transactional
  public void acceptPayment(long paymentId) {
    Payment payment;
    try {
      payment = paymentRepository.getOne(paymentId);
      payment.setPaid(true);
      paymentRepository.save(payment);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
  }

  @Override
  public List<Payment> getUnpaidPayments(int page, int pageSize) {
    Page<Payment> paymentsPage;
    if (page < 0 || pageSize < 0) {
      throw new ServiceException("Page index or page size can't be negative");
    }
    try {
      paymentsPage = paymentRepository.findAllByPaidIsFalse(new PageRequest(page, pageSize));
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return paymentsPage.getContent();
  }

  @Override
  public List<Payment> getAllPayments(int page, int pageSize) {
    Page<Payment> paymentsPage;
    if (page < 0 || pageSize < 0) {
      throw new ServiceException("Page index or page size can't be negative");
    }
    try {
      paymentsPage = paymentRepository.findAll(new PageRequest(page, pageSize));
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return paymentsPage.getContent();
  }

  @Override
  public List<Payment> getPaidPayments(int page, int pageSize) {
    Page<Payment> paymentsPage;
    if (page < 0 || pageSize < 0) {
      throw new ServiceException("Page index or page size can't be negative");
    }
    try {
      paymentsPage = paymentRepository.findAllByPaidIsTrue(new PageRequest(page, pageSize));
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return paymentsPage.getContent();
  }

  @Override
  public Page<Payment> getUnpaidDataTable(long accountId, int draw, int start, int length, String orderDirection,
                                          String search) {
    Page<Payment> payments;
    int page = getPageNumber(start, length);
    PageRequest pageRequest = preparePageRequest(length, orderDirection, page);
    try {
      if (search.length() > 1) {
        payments = paymentRepository.matchUnPaidPaymentsByAccountName(search, pageRequest);
      } else {
        payments = paymentRepository.findAllByPaidIsFalse(pageRequest);
      }
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return payments;
  }

  @Override
  public Page<Payment> getPaidDataTable(long accountId, int draw, int start, int length, String orderDirection,
                                        String search) {
    Page<Payment> payments;
    int page = getPageNumber(start, length);
    Direction direction = Direction.fromString(orderDirection);
    PageRequest pageRequest = new PageRequest(page, length, direction, "date");
    try {
      if (search.length() > 1) {
        payments = paymentRepository.matchPaidPaymentsByAccountName(search, pageRequest);
      } else {
        payments = paymentRepository.findAllByPaidIsTrue(pageRequest);
      }
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return payments;
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

  private PageRequest preparePageRequest(int length, String orderDirection, int page) {
    Direction direction = Direction.fromString(orderDirection);
    return new PageRequest(page, length, direction, "date");
  }


}
