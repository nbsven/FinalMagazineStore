package com.epam.magazinestore.controller;

import static com.epam.magazinestore.util.JSPTitleUtil.CATALOG_JSP;
import static com.epam.magazinestore.util.JSPTitleUtil.MODIFICATION_OF_MAGAZINE_JSP;
import static com.epam.magazinestore.util.PathUtils.ADD_MAGAZINE;
import static com.epam.magazinestore.util.PathUtils.CATALOG;
import static com.epam.magazinestore.util.PathUtils.DELETE;
import static com.epam.magazinestore.util.PathUtils.GET_CATALOG_DATA;
import static com.epam.magazinestore.util.PathUtils.LOGIN;
import static com.epam.magazinestore.util.PathUtils.MODIFICATION;
import static com.epam.magazinestore.util.PathUtils.MODIFY_MAGAZINE;
import static com.epam.magazinestore.util.PathUtils.SUBSCRIBE;
import static java.util.Objects.isNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.epam.magazinestore.dto.DataTableDTO;
import com.epam.magazinestore.entity.Account;
import com.epam.magazinestore.entity.Magazine;
import com.epam.magazinestore.entity.Publisher;
import com.epam.magazinestore.entity.Role;
import com.epam.magazinestore.services.MagazineService;
import com.epam.magazinestore.services.PublisherService;
import com.epam.magazinestore.services.SubscriptionService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@Slf4j
@RequiredArgsConstructor
public class CatalogController {

  private final MagazineService magazineService;

  private final SubscriptionService subscriptionService;

  private final PublisherService publisherService;

  private static final String SESSION_ACCOUNT = "sessionAccount";

  @GetMapping(value = CATALOG)
  public String catalog(Model model, HttpSession session) {
    Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
    if (isNull(account)) {
      return "redirect:" + LOGIN;
    }
    List<Publisher> publishers = publisherService.getAllPublishers(account);
    model.addAttribute("role", account.getRole());
    model.addAttribute("newMagazine", new Magazine());
    model.addAttribute("publishers", publishers);
    return CATALOG_JSP;
  }

  @GetMapping(value = GET_CATALOG_DATA, produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  @SneakyThrows
  public DataTableDTO<Magazine> getCatalog(HttpSession session,
                                           @RequestParam int draw,
                                           @RequestParam int start,
                                           @RequestParam int length,
                                           @RequestParam(value = "order[0][dir]") String priceSortDirection,
                                           @RequestParam(value = "search[value]", required = false) String search,
                                           HttpServletResponse response
  ) {
    Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
    if (isNull(account)) {
      response.sendRedirect(LOGIN);
    }
    Page<Magazine> magazines = magazineService.getMagazines(
        start, length, priceSortDirection, search, account.getId());
    return DataTableDTO.<Magazine>builder()
        .recordsTotal(magazines.getTotalElements())
        .draw(draw)
        .data(magazines.getContent())
        .recordsFiltered(magazines.getTotalElements())
        .build();
  }

  @PostMapping(value = SUBSCRIBE)
  public String subscribe(@RequestParam(name = "magazineId", defaultValue = "1") long magazineId,
                          @RequestParam(name = "duration", defaultValue = "0") int duration,
                          HttpSession session) {
    Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
    subscriptionService.subscribe(account.getId(), magazineId, duration, 0);

    return "redirect:" + CATALOG;
  }

  @PostMapping(value = ADD_MAGAZINE)
  public String addMagazine(@ModelAttribute Magazine magazine,
                            HttpSession session) {
    Account account = (Account) session.getAttribute(SESSION_ACCOUNT);

    if (account.getRole().equals(Role.ROLE_USER)) {
      return "redirect:" + CATALOG;
    }

    magazineService.addMagazine(magazine);

    return "redirect:" + CATALOG;
  }

  @PostMapping(value = DELETE)
  public String delete(@RequestParam(name = "magazineId") long magazineId,
                       HttpSession session) {
    Account account = (Account) session.getAttribute(SESSION_ACCOUNT);

    if (account.getRole().equals(Role.ROLE_USER)) {
      return "redirect:" + CATALOG;
    }

    magazineService.deleteMagazine(magazineId);

    return "redirect:" + CATALOG;
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


  @GetMapping(value = MODIFICATION)
  public ModelAndView modification(@RequestParam(name = "id") long magazineId,
                                   HttpSession session) {
    Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
    ModelAndView modelAndView = new ModelAndView();
    Magazine magazine = magazineService.getMagazineById(magazineId);

    if (account.getRole().equals(Role.ROLE_ADMIN)) {
      List<Publisher> publishers = publisherService.getAllPublishers(account);
      modelAndView.setViewName(MODIFICATION_OF_MAGAZINE_JSP);
      modelAndView.addObject("magazine", magazine);
      modelAndView.addObject("publishers", publishers);
      modelAndView.addObject("modifiedMagazine", new Magazine());
    } else {
      modelAndView.setViewName("redirect:" + CATALOG);
    }

    return modelAndView;
  }


  @PostMapping(value = MODIFY_MAGAZINE)
  public String modify(@ModelAttribute(name = "modifiedMagazine") Magazine modifiedMagazine) {
    magazineService.addMagazine(modifiedMagazine);

    return "redirect:" + CATALOG;
  }
}