package com.epam.magazinestore.controller;

import static com.epam.magazinestore.util.JSPTitleUtil.HOME_JSP;
import static com.epam.magazinestore.util.JSPTitleUtil.REGISTRATION_JSP;
import static com.epam.magazinestore.util.PathUtils.HOME;
import static com.epam.magazinestore.util.PathUtils.REGISTRATION;
import static com.epam.magazinestore.util.PathUtils.SIGN_OUT;
import static com.epam.magazinestore.util.PathUtils.USER;
import static java.util.Objects.isNull;

import com.epam.magazinestore.entity.Account;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

  private static final String SESSION_ACCOUNT = "sessionAccount";

  private static final String MESSAGE = "message";

  @GetMapping(value = SIGN_OUT)
  public String out(HttpSession session) {
    session.invalidate();

    return "redirect:" + HOME;
  }

  @GetMapping(value = HOME)
  public ModelAndView home(ModelAndView modelAndView, HttpSession session) {
    Account account = (Account) session.getAttribute(SESSION_ACCOUNT);

    if (!isNull(account)) {
      modelAndView.setViewName("redirect:" + USER);

      return modelAndView;
    } else {
      modelAndView.setViewName(HOME_JSP);
    }

    return modelAndView;
  }

  @GetMapping(value = REGISTRATION)
  public ModelAndView registration(ModelAndView modelAndView, HttpSession session) {

    String message = (String) session.getAttribute(MESSAGE);

    if (!isNull(message)) {
      modelAndView.addObject(MESSAGE, message);
      session.removeAttribute(MESSAGE);
    }

    modelAndView.setViewName(REGISTRATION_JSP);

    return modelAndView;
  }

}
