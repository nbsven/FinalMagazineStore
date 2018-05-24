package com.epam.magazinestore.controller;

import static com.epam.magazinestore.util.PathUtils.ADD_PUBLISHER;
import static com.epam.magazinestore.util.PathUtils.DELETE_PUBLISHER;
import static com.epam.magazinestore.util.PathUtils.LOGIN;
import static com.epam.magazinestore.util.PathUtils.PUBLISHERS;
import static java.util.Objects.isNull;

import com.epam.magazinestore.entity.Account;
import com.epam.magazinestore.entity.Publisher;
import com.epam.magazinestore.exception.ServiceException;
import com.epam.magazinestore.services.PublisherService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PublisherController {

  private final PublisherService publisherService;
  private static final String SESSION_ACCOUNT = "sessionAccount";

  @GetMapping(PUBLISHERS)
  public ModelAndView getPublishers(HttpSession session) {

    Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
    if (isNull(account)) {
      return new ModelAndView("redirect:" + LOGIN);
    }
    List<Publisher> publishers = publisherService.getAllPublishers(account);
    ModelAndView modelAndView = new ModelAndView("publishers");
    modelAndView.addObject("publishers", publishers);
    modelAndView.addObject("publisher", new Publisher());
    modelAndView.addObject("role", account.getRole());
    return modelAndView;
  }

  @PostMapping(DELETE_PUBLISHER)
  public String removePublisher(@RequestParam long publisherId, HttpSession session) {
    Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
    if (isNull(account)) {
      return "redirect:" + LOGIN;
    }
    publisherService.removePublisher(publisherId, account);
    return "redirect:" + PUBLISHERS;
  }

  @PostMapping(ADD_PUBLISHER)
  public String addPublisher(@ModelAttribute Publisher publisher, HttpSession session) {
    Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
    if (isNull(account)) {
      return "redirect:" + LOGIN;
    }
    publisherService.addPublisher(publisher, account);
    return "redirect:" + PUBLISHERS;
  }

  @ExceptionHandler(ServiceException.class)
  public ModelAndView handleError(HttpServletRequest req, Exception ex) {
    log.error("Request: " + req.getRequestURL() + " raised " + ex);
    ModelAndView mav = new ModelAndView();
    mav.addObject("exception", ex.getMessage());
    mav.addObject("url", req.getRequestURL());
    mav.setViewName("error");
    return mav;
  }

}
