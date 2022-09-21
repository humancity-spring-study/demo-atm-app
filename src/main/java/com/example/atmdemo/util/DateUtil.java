package com.example.atmdemo.util;

import java.time.Duration;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {

    public static Date addSecondsFromNow(Duration duration) {
        return DateUtils.addSeconds(new Date(), (int) duration.getSeconds());
    }
}
