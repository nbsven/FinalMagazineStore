package com.epam.magazinestore.services;

import com.epam.magazinestore.entity.Payment;
import com.epam.magazinestore.entity.Subscription;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Service for working with payments.
 */
public interface PaymentService {

  Payment registerPayment(int amount);

  Payment registerPayment(Subscription subscription);

  /**
   * Accept payment
   *
   * @param payment payment to be accepted
   */
  void acceptPayment(Payment payment);

  /**
   * Accept payment
   *
   * @param paymentId id of payment to be accepted
   */
  void acceptPayment(long paymentId);

  /**
   * Returns list of unpaid payments for all accounts
   *
   * @param page zero-based page index
   * @param pageSize the size of the page to be returned
   * @return list of all unpaid payments
   */
  List<Payment> getUnpaidPayments(int page, int pageSize);

  /**
   * Returns list of all payments for all accounts
   *
   * @param page zero-based page index
   * @param pageSize the size of the page to be returned
   * @return list of all payments for all accounts
   */
  List<Payment> getAllPayments(int page, int pageSize);

  /**
   * Returns list of paid payments for all accounts
   *
   * @param page zero-based page index
   * @param pageSize the size of the page to be returned
   * @return list of paid payments for all accounts
   */
  List<Payment> getPaidPayments(int page, int pageSize);

  Page<Payment> getUnpaidDataTable(long accountId, int draw, int start, int length, String orderDirection,
                                   String search);

  Page<Payment> getPaidDataTable(long accountId, int draw, int start, int length, String orderDirection, String search);

}
