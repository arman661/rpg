package com.game.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import javax.persistence.Converter;
import java.util.Date;

@Converter
public class DateToLongConverter implements AttributeConverter<Long, Date> {


    @Override
    public Date convertToDatabaseColumn(Long aLong) {
        return new Date(aLong);
    }

    @Override
    public Long convertToEntityAttribute(Date date) {
        return date.getTime();
    }
}
