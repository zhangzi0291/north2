package com.north.aop.validator;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-02-23
 */
public class ValidateUtil {

    /**
     * 元素只能为Null
     *
     * @param value
     * @param express 无用
     * @return
     */
    public static Boolean IsNull(Object value, String express) {
        if (value != null) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 元素不能为Null
     *
     * @param value
     * @param express 无用
     * @return
     */
    public static Boolean NotNull(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 元素必须为true
     *
     * @param value
     * @param express 无用
     * @return
     */
    public static Boolean AssertTrue(Object value, String express) {
        return Boolean.TRUE.equals(value);
    }

    /**
     * 元素必须为false
     *
     * @param value
     * @param express 无用
     * @return
     */
    public static Boolean AssertFalse(Object value, String express) {
        return Boolean.FALSE.equals(value);
    }

    /**
     * 元素必须是整数
     *
     * @param value
     * @param express 无用
     * @return
     */
    public static Boolean IsInteger(Object value, String express) {
        if (IsNull(value, express)) {
            return Boolean.FALSE;
        }
        try {
            Integer.valueOf(value.toString());
        } catch (NumberFormatException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 元素必须是数字
     *
     * @param value
     * @param express 无用
     * @return
     */
    public static Boolean IsDigits(Object value, String express) {
        if (IsNull(value, express)) {
            return Boolean.FALSE;
        }
        try {
            Double.valueOf(value.toString());
        } catch (NumberFormatException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 元素在指定范围内
     *
     * @param value
     * @param express 最小值（没有不校验） 逗号 最大值（没有不校验）,逗号必须有，例： 1,10
     * @return
     */
    public static Boolean Range(Object value, String express) {
        if (IsNull(value, express)) {
            return Boolean.TRUE;
        }
        try {
            Double d = Double.valueOf(value.toString());
            String[] ranges = express.split(",");
            if (ranges[0] != null) {
                Double min = Double.valueOf(ranges[0]);
                if (d.compareTo(min) < 0) {
                    return Boolean.FALSE;
                }
            }
            if (ranges[1] != null) {
                Double max = Double.valueOf(ranges[1]);
                if (d.compareTo(max) > 0) {
                    return Boolean.FALSE;
                }
            }
        } catch (NumberFormatException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 元素长度必须在指定范围内
     *
     * @param value
     * @param express 最小值（没有不校验） 逗号 最大值（没有不校验）,逗号必须有，例： 1,10
     * @return
     */
    public static Boolean Length(Object value, String express) {
        if (IsNull(value, null)) {
            return Boolean.TRUE;
        }
        if (value instanceof String) {
            Integer length = Integer.valueOf(express);
            if (value.toString().length() > length) {
                return Boolean.FALSE;
            }
            String[] lengths = express.split(",");
            if (lengths[0] != null) {
                Long min = Long.valueOf(lengths[0]);
                if (value.toString().length() < min) {
                    return Boolean.FALSE;
                }
            }
            if (lengths[1] != null) {
                Long max = Long.valueOf(lengths[1]);
                if (value.toString().length() > max) {
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 元素是否是一个过去时间
     *
     * @param value
     * @param express 元素日期格式化字符串
     * @return
     */
    public static Boolean Past(Object value, String express) {
        if (IsNull(value, express)) {
            return Boolean.TRUE;
        }
        if (!StringUtils.hasLength(express)) {
            express = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        }
        if (value instanceof String) {
            LocalDateTime localDateTime = LocalDateTime.parse(value.toString(), DateTimeFormatter.ofPattern(express));
            return LocalDateTime.now().isAfter(localDateTime);
        }
        if (value instanceof Date) {
            return new Date().after((Date) value);
        }
        if (value instanceof LocalDate) {
            return LocalDate.now().isAfter((LocalDate) value);
        }
        if (value instanceof LocalDateTime) {
            return LocalDateTime.now().isAfter((LocalDateTime) value);
        }
        return Boolean.TRUE;
    }

    /**
     * 元素是否是一个将来时间
     *
     * @param value
     * @param express 元素日期格式化字符串
     * @return
     */
    public static Boolean Future(Object value, String express) {
        if (IsNull(value, express)) {
            return Boolean.TRUE;
        }
        if (!StringUtils.hasLength(express)) {
            express = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        }
        if (value instanceof String) {
            LocalDateTime localDateTime = LocalDateTime.parse(value.toString(), DateTimeFormatter.ofPattern(express));
            return LocalDateTime.now().isBefore(localDateTime);
        }
        if (value instanceof Date) {
            return new Date().before((Date) value);
        }
        if (value instanceof LocalDate) {
            return LocalDate.now().isBefore((LocalDate) value);
        }
        if (value instanceof LocalDateTime) {
            return LocalDateTime.now().isBefore((LocalDateTime) value);
        }
        return Boolean.TRUE;
    }

    /**
     * 元素通过正则表达式校验
     *
     * @param value
     * @param express 正则表达式
     * @return
     */
    public static Boolean Pattern(Object value, String express) {
        if (IsNull(value, null)) {
            return Boolean.TRUE;
        }
        if (value instanceof String) {
            Pattern p = Pattern.compile(express);
            Matcher m = p.matcher((String) value);
            if (m.matches()) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 判断字符串为空
     *
     * @param value
     * @param express 无用
     * @return
     */
    public static Boolean IsEmpty(Object value, String express) {
        if (IsNull(value, null)) {
            return Boolean.TRUE;
        }
        return !StringUtils.hasLength(value.toString());
    }

    /**
     * 判断字符串非空
     *
     * @param value
     * @param express 无用
     * @return
     */
    public static Boolean NotEmpty(Object value, String express) {
        if (IsNull(value, null)) {
            return Boolean.FALSE;
        }
        return StringUtils.hasLength(value.toString());
    }

    /**
     * 判断元素是否在枚举的数据中
     *
     * @param value
     * @param express 表达式用 英文逗号隔开，无空格,例： "1,2,3"
     * @return
     */
    public static Boolean Enum(Object value, String express) {
        if (IsNull(value, null)) {
            return Boolean.TRUE;
        }
        if (IsEmpty(value, express)) {
            return Boolean.FALSE;
        }
        String[] array = express.split(",");
        Set<String> set = new HashSet<>(Arrays.asList(array));
        return set.contains(value.toString());
    }

    /**
     * 判断元素是否在枚举的数据中
     *
     * @param value
     * @param express Enum的完全限定类名
     * @return
     */
    public static Boolean EnumClass(Object value, String express) {
        if (IsNull(value, null)) {
            return Boolean.TRUE;
        }
        if (IsEmpty(value, express)) {
            return Boolean.FALSE;
        }
        Class clazz = null;
        try {
            clazz = Class.forName(express);
            if (clazz.isEnum()) {
                Object[] enums = clazz.getEnumConstants();
                for (Object o : enums) {
                    Class enumClass = o.getClass();
                    Field f = enumClass.getDeclaredField("value");
                    if (value.equals(f.get(o))) {
                        return Boolean.TRUE;
                    }
                }
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    public static void main(String[] args) {
        String value = "1";
        String express = "com.north.constant.DeviceTypeEnum";

        try {
            Class clazz = Class.forName(express);
            if (clazz.isEnum()) {
                Object[] enums = clazz.getEnumConstants();
                for (Object o : enums) {
                    Class enumClass = o.getClass();
                    Field f = enumClass.getDeclaredField("value");
                    if (value.equals(f.get(o))) {
                        System.out.println("ok");
                        break;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
