package com.epam.magazinestore;

import com.epam.magazinestore.entity.Account;
import com.epam.magazinestore.entity.Magazine;
import com.epam.magazinestore.entity.Payment;
import com.epam.magazinestore.entity.Subscription;
import com.epam.magazinestore.exception.ServiceException;
import com.epam.magazinestore.services.PaymentService;
import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PaymentServiceTests {

  private PaymentService paymentService;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void registerNullSubscriptionTest() {
    expectedException.expect(ServiceException.class);
    expectedException.expectMessage("Subscription can't be null");
    paymentService.registerPayment(null);
  }

  @Test
  public void registerSubscriptionTest() {
    Subscription subscription = new Subscription();
    Magazine magazine = new Magazine();
    magazine.setName("Time");
    magazine.setPublicationDate(10);

    Account account = new Account();
    account.setUsername("user");
    account.setSubscriptions(new ArrayList<>());
    subscription.setAccount(account);
    subscription.setMagazine(magazine);
    subscription.setEndDate(LocalDate.now().plusMonths(5));
    subscription.setStartDate(LocalDate.now());

    paymentService.registerPayment(subscription);
    int count = paymentService.getAllPayments(0, 10).size();
    Assert.assertEquals(1, count);
  }

  @Test
  public void registerSubscriptionWithNullMagazineTest() {
    expectedException.expect(ServiceException.class);
    expectedException.expectMessage("Magazine field must be set");
    Subscription subscription = new Subscription();

    Account account = new Account();
    account.setUsername("user");
    account.setSubscriptions(new ArrayList<>());
    subscription.setAccount(account);
    subscription.setMagazine(null);
    subscription.setEndDate(LocalDate.now().plusMonths(5));
    subscription.setStartDate(LocalDate.now());
    paymentService.registerPayment(subscription);
  }

  @Test
  public void acceptPaymentTest() {
    Subscription subscription = new Subscription();
    Magazine magazine = new Magazine();
    magazine.setName("Time");
    magazine.setPublicationDate(10);

    Account account = new Account();
    account.setUsername("user");
    account.setSubscriptions(new ArrayList<>());
    subscription.setAccount(account);
    subscription.setMagazine(magazine);
    subscription.setEndDate(LocalDate.now().plusMonths(5));
    subscription.setStartDate(LocalDate.now());

    Payment payment = paymentService.registerPayment(subscription);

    Payment unPaidPayment = paymentService.getAllPayments(0, 10).get(0);
    Assert.assertFalse(unPaidPayment.isPaid());
    paymentService.acceptPayment(payment);
    Payment paidPayment = paymentService.getAllPayments(0, 10).get(0);
    Assert.assertTrue(paidPayment.isPaid());
  }

  @Autowired
  public void setPaymentService(PaymentService paymentService) {
    this.paymentService = paymentService;
  }
}
