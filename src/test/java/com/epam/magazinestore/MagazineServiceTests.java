package com.epam.magazinestore;

import com.epam.magazinestore.entity.Account;
import com.epam.magazinestore.entity.Magazine;
import com.epam.magazinestore.repository.MagazineRepository;
import com.epam.magazinestore.repository.SubscriptionRepository;
import com.epam.magazinestore.services.AccountService;
import com.epam.magazinestore.services.MagazineService;
import com.epam.magazinestore.services.SubscriptionService;
import java.util.List;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MagazineServiceTests {

  public static final int PAGE_INDEX = 0;
  public static final int PAGE_SIZE = 10;
  public static final int MAGAZINE_ID = 1;
  private MagazineService magazineService;

  @Autowired
  private MagazineRepository magazineRepository;

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  private SubscriptionService subscriptionService;

  @Autowired
  private AccountService accountService;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  @Rollback
  public void removeMagazineTest() {
    List beforeRemoveList = magazineService.getAvailableMagazines(PAGE_INDEX, PAGE_SIZE);
    int prevSize = beforeRemoveList.size();
    magazineService.deleteMagazine(MAGAZINE_ID);
    List afterRemoveList = magazineService.getAvailableMagazines(PAGE_INDEX, PAGE_SIZE);
    int newSize = afterRemoveList.size();
    Assert.assertEquals(prevSize - 1, newSize);
  }

  @Test
  @Rollback
  public void getUnSubscribedMagazinesTest() {
    Account account = accountService.getAccountByUsername("nyaz");
    Magazine magazine = magazineService.getAvailableMagazines(PAGE_INDEX, PAGE_SIZE).get(0);
    //subscriptionService.subscribe(account.getId(), magazine.getId(), 5, 0);
    Page<Magazine> page = magazineRepository.getUnSubscribedMagazines(account.getId(), new PageRequest(0, 2));
    Assert.assertEquals(5, page.getTotalElements());
  }

  @Test
  @Rollback
  public void regexTest() {
    Account account = accountService.getAccountByUsername("nyaz");
    Page<Magazine> page =
        magazineRepository
            .matchByMagazineNameOrPublisherName("Na", new PageRequest(PAGE_INDEX, PAGE_SIZE), account.getId());
    System.out.println(page.getContent());
    Assert.assertEquals(2, page.getContent().size());
  }

  @Autowired
  public void setMagazineService(MagazineService magazineService) {
    this.magazineService = magazineService;
  }
}
