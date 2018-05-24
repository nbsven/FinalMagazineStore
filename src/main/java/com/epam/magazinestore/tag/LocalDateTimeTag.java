package com.epam.magazinestore.tag;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.SkipPageException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
@NoArgsConstructor
public class LocalDateTimeTag extends SimpleTagSupport {

  private String pattern;
  private LocalDateTime localDateTime;

  @Override
  public void doTag() throws JspException {
    try {
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
      String formatted = localDateTime.format(dateTimeFormatter);
      getJspContext().getOut().write(formatted);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new SkipPageException("Exception in formatting " + localDateTime
          + " with format " + pattern);
    }
  }
}
