package com.example.atmdemo.common;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import com.example.atmdemo.util.SecurityConvertUtil;

@Converter
public class SecurityConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String s) {
        if(s==null)
            return null;
        return SecurityConvertUtil.encrypt(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        if(s==null)
            return null;
        return SecurityConvertUtil.decrypt(s);
    }
}
