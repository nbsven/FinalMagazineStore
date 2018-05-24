package com.epam.magazinestore;


import com.epam.magazinestore.entity.Account;
import com.epam.magazinestore.entity.Magazine;
import com.epam.magazinestore.entity.Subscription;
import com.epam.magazinestore.services.AccountService;
import com.epam.magazinestore.services.MagazineService;
import com.epam.magazinestore.services.SubscriptionService;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SubscriptionServiceTests {

  @Test
  public void test() {

  }

  public static final String USER = "user";
  public static final int PAGE_SIZE = 10;
  public static final int PAGE_START = 0;
  public static final String MAGAZINE_NAME = "Time";

  private SubscriptionService subscriptionService;

  private AccountService accountService;

  private MagazineService magazineService;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void before() {
    Account accountToSave = new Account();
    accountToSave.setUsername(USER);
    accountToSave.setSubscriptions(new ArrayList<>());
    Magazine magazineToSave = new Magazine();
    magazineToSave.setPublicationDate(10);
    magazineToSave.setName(MAGAZINE_NAME);
    magazineService.addMagazine(magazineToSave);
    accountService.registerAccount(accountToSave);
  }

  @Test
  @Rollback
  public void subscribeTest() {
    Account account = accountService.getAccountByUsername(USER);
    Magazine magazine = magazineService.getAvailableMagazines(PAGE_START, PAGE_SIZE).get(0);
    subscriptionService.subscribe(account.getId(), magazine.getId(), 5, 0);
    Account persistedAccount = accountService.getAccountByUsername(USER);
    List<Subscription> subscriptions = persistedAccount.getSubscriptions();
    Assert.assertEquals(1, subscriptions.size());
  }
//
//    @Test
//    @Rollback
//    public void unSubscribeTest() {
//        Account account = accountService.getAccountByUsername(USER);
//        Magazine magazine = magazineService.getAvailableMagazines(PAGE_START, PAGE_SIZE).get(0);
//        subscriptionService.subscribe(account.getId(), magazine.getId(), 5, 0);
//        subscriptionService.unsubscribe(account.getId(), magazine.getId());
//        Page<Subscription> subscriptions = subscriptionService.getSubscriptionsByAccountId(account.getId(),
//                PAGE_START,
//                PAGE_SIZE, "", "asc");
//        int size = subscriptions.getContent().size();
//        Assert.assertEquals(0, size);
//    }

  @Test
  @Rollback
  public void prolongSubscriptionTest() {
    Account account = accountService.getAccountByUsername(USER);
    Magazine magazine = magazineService.getAvailableMagazines(PAGE_START, PAGE_SIZE).get(0);
    Subscription subscription = subscriptionService.subscribe(account.getId(), magazine.getId(), 2, 0);
    subscriptionService.prolongSubscription(subscription, 4);
    Assert.assertEquals(USER, subscription.getAccount().getUsername());
    Assert.assertEquals(6, Period.between(subscription.getStartDate(), subscription.getEndDate()).getMonths());
  }

  @Test
  @Rollback
  public void getUnpaidSubscriptionTest() {
    Account account = accountService.getAccountByUsername(USER);
    Magazine magazine = magazineService.getAvailableMagazines(PAGE_START, PAGE_SIZE).get(0);
    subscriptionService.subscribe(account.getId(), magazine.getId(), 2, 0);
    List<Subscription> subscriptions =
        subscriptionService.getUnpaidSubscriptionsByAccount(account, PAGE_START, PAGE_SIZE);
    Assert.assertEquals(1, subscriptions.size());
  }

  @Test
  @Rollback
  public void getAllSubscriptionTest() {
    Account account = accountService.getAccountByUsername(USER);
    Magazine magazine = magazineService.getAvailableMagazines(PAGE_START, PAGE_SIZE).get(0);
    subscriptionService.subscribe(account.getId(), magazine.getId(), 2, 0);
    Page<Subscription> subscriptions = subscriptionService.getSubscriptionsByAccountId(account.getId(),
        PAGE_START,
        PAGE_SIZE, "", "asc");
    Assert.assertEquals(1, subscriptions.getContent().size());
  }

  @Autowired
  public void setSubscriptionService(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }

  @Autowired
  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }

  @Autowired
  public void setMagazineService(MagazineService magazineService) {
    this.magazineService = magazineService;
  }

}
