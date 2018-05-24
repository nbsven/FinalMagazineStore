package com.epam.magazinestore.controller;

import static com.epam.magazinestore.util.JSPTitleUtil.LOGIN_JSP;
import static com.epam.magazinestore.util.JSPTitleUtil.USER_JSP;
import static com.epam.magazinestore.util.PathUtils.ACCOUNT;
import static com.epam.magazinestore.util.PathUtils.CREATE_ACCOUNT;
import static com.epam.magazinestore.util.PathUtils.LOGIN;
import static com.epam.magazinestore.util.PathUtils.PROLONGATE;
import static com.epam.magazinestore.util.PathUtils.REGISTRATION;
import static com.epam.magazinestore.util.PathUtils.SUBSCRIPTIONS;
import static com.epam.magazinestore.util.PathUtils.UNSUBSCRIBE;
import static com.epam.magazinestore.util.PathUtils.USER;
import static java.util.Objects.isNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.epam.magazinestore.dto.DataTableDTO;
import com.epam.magazinestore.entity.Account;
import com.epam.magazinestore.entity.Role;
import com.epam.magazinestore.entity.Subscription;
import com.epam.magazinestore.services.AccountService;
import com.epam.magazinestore.services.SubscriptionService;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequiredArgsConstructor
@Slf4j
public class AccountController {

  private static final String SESSION_ACCOUNT = "sessionAccount";

  private static final String MESSAGE = "message";

  private static final String SUCCESS = "success";

  private static final String NAME = "account_name";

  private static final String ERROR_AUTHORIZATION = "text.login.error.message";

  private static final String ERROR_REGISTRATION = "text.registration.error.message";

  private static final String COMPLETE_REGISTRATION = "text.registration.complete.message";

  private final AccountService accountService;

  private final SubscriptionService subscriptionService;

  private final MessageSource messageSource;

  @PostMapping(value = ACCOUNT)
  public ModelAndView account(@RequestParam("username") String name,
                              @RequestParam("password") String password,
                              ModelAndView modelAndView, HttpSession session,
                              Locale locale
  ) {

    if (accountService.isUserNameExists(name)) {
      Account account = accountService.getAccountByUsername(name);

      if (account.getPassword().equals(password)) {
        modelAndView.setViewName("redirect:" + USER);
        session.setAttribute(SESSION_ACCOUNT, account);

        return modelAndView;
      }
    }

    String errorMessage = messageSource.getMessage(ERROR_AUTHORIZATION, null, locale);
    session.setAttribute(MESSAGE, errorMessage);
    modelAndView.setViewName("redirect:" + LOGIN);

    return modelAndView;
  }

  @GetMapping(value = LOGIN)
  public ModelAndView login(ModelAndView modelAndView, HttpSession session, Locale locale) {
    String message = (String) session.getAttribute(MESSAGE);

    if (!isNull(message)) {
      String completeRegistration = messageSource.getMessage(COMPLETE_REGISTRATION, null, locale);
      if (message.equals(completeRegistration)) {
        modelAndView.addObject(SUCCESS, SUCCESS);
      }
      modelAndView.addObject(MESSAGE, message);
      session.removeAttribute(MESSAGE);
    }

    modelAndView.setViewName(LOGIN_JSP);

    return modelAndView;
  }

  @PostMapping(value = CREATE_ACCOUNT)
  public ModelAndView createAccount(@RequestParam("name") String name, @RequestParam("password") String password,
                                    ModelAndView modelAndView, HttpSession session, Locale locale) {

    if (accountService.isUserNameExists(name)) {
      String errorMessage = messageSource.getMessage(ERROR_REGISTRATION, null, locale);
      session.setAttribute(MESSAGE, errorMessage);
      modelAndView.setViewName("redirect:" + REGISTRATION);
    } else {
      Account account = new Account();
      account.setUsername(name);
      account.setPassword(password);
      account.setRole(Role.ROLE_USER);
      accountService.registerAccount(account);
      String completeMessage = messageSource.getMessage(COMPLETE_REGISTRATION, null, locale);
      session.setAttribute(MESSAGE, completeMessage);
      modelAndView.setViewName("redirect:" + LOGIN);
    }

    return modelAndView;
  }

  @GetMapping(value = USER)
  public ModelAndView user(HttpSession session) {
    Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
    ModelAndView mav = new ModelAndView();

    if (!isNull(account)) {
      mav.addObject(NAME, account.getUsername());
      mav.setViewName(USER_JSP);
    } else {
      mav.setViewName("redirect:" + LOGIN);
    }

    return mav;
  }

  @GetMapping(value = ACCOUNT + SUBSCRIPTIONS, produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  public DataTableDTO getSubscriptions(HttpSession session,
                                       @RequestParam int start,
                                       @RequestParam int length,
                                       @RequestParam int draw,
                                       @RequestParam(value = "search[value]", required = false) String search,
                                       @RequestParam(value = "order[0][dir]", required = false) String orderDirection) {
    Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
    Page<Subscription> subscriptions =
        subscriptionService.getSubscriptionsByAccountId(account.getId(), start, length, search, orderDirection);

    return DataTableDTO.<Subscription>builder()
        .recordsFiltered(subscriptions.getTotalElements())
        .data(subscriptions.getContent())
        .draw(draw)
        .recordsTotal(subscriptions.getTotalElements())
        .build();
  }

  @PostMapping(value = UNSUBSCRIBE)
  public String unsubscribe(@RequestParam(name = "subscriptionId") long subscriptionId) {
    subscriptionService.unsubscribe(subscriptionId);

    return "redirect:" + USER;
  }

  @PostMapping(value = PROLONGATE)
  @ResponseBody
  public String prolongateSubscription(@RequestParam("duration") int duration, @RequestParam("id") long id) {
    subscriptionService.prolongSubscription(id, duration);

    return "ok";
  }

  @ExceptionHandler(Exception.class)
  public ModelAndView handleError(HttpServletRequest req, Exception ex) {
    log.error("Request: " + req.getRequestURL() + " raised " + ex);
    log.trace(req.getParameterMap().toString());
    ModelAndView mav = new ModelAndView();
    mav.addObject("exception", ex);
    mav.addObject("url", req.getRequestURL());
    mav.setViewName("error");
    return mav;
  }
}