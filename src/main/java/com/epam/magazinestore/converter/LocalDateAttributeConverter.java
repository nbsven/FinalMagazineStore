package com.epam.magazinestore.converter;

import static java.util.Objects.isNull;

import java.sql.Date;
import java.time.LocalDate;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

  @Override
  public Date convertToDatabaseColumn(LocalDate attribute) {
    return (isNull(attribute) ? null : Date.valueOf(attribute));
  }

  @Override
  public LocalDate convertToEntityAttribute(Date sqlDate) {
    return (isNull(sqlDate) ? null : sqlDate.toLocalDate());
  }
}
