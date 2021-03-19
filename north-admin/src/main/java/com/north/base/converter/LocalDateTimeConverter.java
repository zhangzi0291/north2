package com.north.base.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

    /**
     * 可格式化 的日期 字串
     */
    private static final List<String> FORMARTS = new ArrayList();

    static {
        FORMARTS.add("yyyy-MM");
        FORMARTS.add("yyyy-MM-dd");
        FORMARTS.add("yyyy-MM-dd HH:mm");
        FORMARTS.add("yyyy-MM-dd HH:mm:ss");
        FORMARTS.add("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    @Override
    public LocalDateTime convert(String source) {
        if (!StringUtils.hasLength(source)) {
            return null;
        }

        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(FORMARTS.get(0)));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return LocalDateTime.parse(source + " 00:00:00", DateTimeFormatter.ofPattern(FORMARTS.get(3)));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(FORMARTS.get(2)));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(FORMARTS.get(3)));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}T{1}\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{3}$")) {
            LocalDateTime time = LocalDateTime.parse(source);
            return time;
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}T{1}\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{3}([Z]|[+].{4,5})$")) {
            LocalDateTime time = ZonedDateTime.parse(source).withZoneSameInstant(ZoneId.of("+08:00")).toLocalDateTime();
            return time;
        } else if (source.matches("^[-\\+]?[\\d]*$")) {
            return new Date(Long.valueOf(source)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }
    }


}
