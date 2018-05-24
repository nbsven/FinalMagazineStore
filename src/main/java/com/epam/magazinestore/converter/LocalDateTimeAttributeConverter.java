package com.epam.magazinestore.converter;

import static java.util.Objects.isNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

  @Override
  public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
    return (isNull(attribute) ? null : Timestamp.valueOf(attribute));
  }

  @Override
  public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
    return (isNull(sqlTimestamp) ? null : sqlTimestamp.toLocalDateTime());
  }
}
