package com.epam.magazinestore;

import com.epam.magazinestore.entity.Account;
import com.epam.magazinestore.exception.ServiceException;
import com.epam.magazinestore.services.AccountService;
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
public class AccountServiceTests {

  private AccountService accountService;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void existsTest() {
    expectedException.expect(ServiceException.class);
    expectedException.expectMessage("Username can't be null");
    accountService.isUserNameExists(null);
  }

  @Test
  public void registerNullAccountTest() {
    expectedException.expect(ServiceException.class);
    expectedException.expectMessage("Account can't be null");
    accountService.registerAccount(null);
  }

  @Test
  public void registerNonUniqueAccountTest() {
    expectedException.expect(ServiceException.class);
    expectedException.expectMessage("Could not register account: account with such username already exists");
    Account account = new Account();
    account.setUsername("user");
    Account anotherAccount = new Account();
    anotherAccount.setUsername("user");
    accountService.registerAccount(account);
    accountService.registerAccount(anotherAccount);
  }

  @Test
  public void deleteNonExistingAccountTest() {
    expectedException.expect(ServiceException.class);
    expectedException.expectMessage("Could not delete account: account not found");
    Account account = new Account();
    account.setUsername("user");
    accountService.deleteAccount(account);
  }


  @Autowired
  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }


}
