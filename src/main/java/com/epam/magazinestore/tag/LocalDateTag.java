package com.epam.magazinestore.tag;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.SkipPageException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
@Getter
@Setter
public class LocalDateTag extends SimpleTagSupport {

  private String pattern;
  private LocalDate localDate;

  @Override
  public void doTag() throws JspException {
    try {
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
      String formatted = localDate.format(dateTimeFormatter);
      getJspContext().getOut().write(formatted);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new SkipPageException("Exception in formatting " + localDate
          + " with format " + pattern);
    }
  }

}
