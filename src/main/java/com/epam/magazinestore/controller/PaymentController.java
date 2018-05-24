package com.epam.magazinestore.controller;

import static com.epam.magazinestore.util.PathUtils.GET_PAID_PAYMENTS_DATA;
import static com.epam.magazinestore.util.PathUtils.GET_UNPAID_PAYMENTS_DATA;
import static com.epam.magazinestore.util.PathUtils.HOME;
import static com.epam.magazinestore.util.PathUtils.LOGIN;
import static com.epam.magazinestore.util.PathUtils.PAY;
import static com.epam.magazinestore.util.PathUtils.PAYMENTS;
import static com.epam.magazinestore.util.PathUtils.USER;
import static java.util.Objects.isNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.epam.magazinestore.dto.DataTableDTO;
import com.epam.magazinestore.entity.Account;
import com.epam.magazinestore.entity.Payment;
import com.epam.magazinestore.entity.Role;
import com.epam.magazinestore.services.PaymentService;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

  private static final String SESSION_ACCOUNT = "sessionAccount";

  @Autowired
  PaymentService paymentService;

  @GetMapping(value = PAYMENTS)
  public ModelAndView payments(@RequestParam(name = "payment_view", defaultValue = "unpaid") String paymentView,
                               HttpSession session, ModelAndView modelAndView) {
    Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
    if (redirectNotAdmin(modelAndView, account)) {
      return modelAndView;
    }
    modelAndView.addObject("paymentView", paymentView);
    modelAndView.setViewName("payments");

    return modelAndView;
  }

  private boolean redirectNotAdmin(ModelAndView modelAndView, Account account) {
    if (isNull(account)) {
      modelAndView.setViewName("redirect:" + HOME);
      return true;
    }
    if (account.getRole().equals(Role.ROLE_USER)) {
      modelAndView.setViewName("redirect:" + USER);
      return true;
    }
    return false;
  }

  @PostMapping(value = PAY)
  public ModelAndView pay(@RequestParam(name = "paymentId") long paymentId,
                          HttpSession session, ModelAndView modelAndView) {
    Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
    if (redirectNotAdmin(modelAndView, account)) {
      return modelAndView;
    }
    paymentService.acceptPayment(paymentId);
    modelAndView.setViewName("redirect:" + PAYMENTS);
    return modelAndView;
  }


  @GetMapping(value = GET_UNPAID_PAYMENTS_DATA, produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  @SneakyThrows
  public DataTableDTO<Payment> getUnpaidPayments(HttpSession session,
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
    Page<Payment> payments =
        paymentService.getUnpaidDataTable(account.getId(), draw, start, length, priceSortDirection, search);

    return DataTableDTO.<Payment>builder()
        .data(payments.getContent())
        .draw(draw)
        .recordsFiltered(payments.getTotalElements())
        .recordsTotal(payments.getTotalElements())
        .build();
  }

  @GetMapping(value = GET_PAID_PAYMENTS_DATA, produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  @SneakyThrows
  public DataTableDTO<Payment> getPaidPayments(HttpSession session,
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
    Page<Payment> payments =
        paymentService.getPaidDataTable(account.getId(), draw, start, length, priceSortDirection, search);

    return DataTableDTO.<Payment>builder()
        .data(payments.getContent())
        .draw(draw)
        .recordsFiltered(payments.getTotalElements())
        .recordsTotal(payments.getTotalElements())
        .build();
  }

}
