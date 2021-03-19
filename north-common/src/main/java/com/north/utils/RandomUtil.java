package com.north.utils;

import org.springframework.util.StringUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Random Utils
 * <ul>
 * 洗牌算法
 * <li>{@link #shuffle(Object[])} 随机置换指定的数组使用的默认源的随机性</li>
 * <li>{@link #shuffle(Object[], int)} 随机置换指定的数组</li>
 * <li>{@link #shuffle(int[])} 随机置换指定数组的使用随机的默认源</li>
 * <li>{@link #shuffle(int[], int)}随机置换指定的数组</li>
 * </ul>
 * <ul>
 * 得到随机int
 * <li>{@link #getRandom(int)} 得到随机int 0和最大值之间</li>
 * <li>{@link #getRandom(int, int)} 得到之间的最小和最大的随机数组</li>
 * </ul>
 * <ul>
 * 得到随机数字或字母
 * <li>{@link #getRandomCapitalLetters(int)} 得到一个固定长度的随机字符串，其混合大写字母</li>
 * <li>....</li>
 * </ul>
 */
public class RandomUtil {

    public static final String NUMBERS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERS = "0123456789";
    public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";


    private RandomUtil() {
        throw new AssertionError();
    }

    /**
     * 得到一个固定长度的随机字符串，其混合大写，小写字母和数字
     *
     * @param length
     * @return
     * @see RandomUtil#getRandom(String source, int length)
     */
    public static String getRandomNumbersAndLetters(int length) {
        return getRandom(NUMBERS_AND_LETTERS, length);
    }

    /**
     * 得到一个固定长度的随机字符串，一个混合的数字
     *
     * @param length
     * @return
     * @see RandomUtil#getRandom(String source, int length)
     */
    public static String getRandomNumbers(int length) {
        return getRandom(NUMBERS, length);
    }

    /**
     * 得到一个固定长度的随机字符串，其混合大写和小写字母
     *
     * @param length
     * @return
     * @see RandomUtil#getRandom(String source, int length)
     */
    public static String getRandomLetters(int length) {
        return getRandom(LETTERS, length);
    }

    /**
     * 得到一个固定长度的随机字符串，其混合大写字母
     *
     * @param length
     * @return
     * @see RandomUtil#getRandom(String source, int length)
     */
    public static String getRandomCapitalLetters(int length) {
        return getRandom(CAPITAL_LETTERS, length);
    }

    /**
     * 得到一个固定长度的随机字符串，其混合物的小写字母
     *
     * @param length
     * @return
     * @see RandomUtil#getRandom(String source, int length)
     */
    public static String getRandomLowerCaseLetters(int length) {
        return getRandom(LOWER_CASE_LETTERS, length);
    }

    /**
     * 得到一个固定长度的随机字符串，其混合来源source参数字符
     *
     * @param source
     * @param length
     * @return <ul>
     * <li>if source is null or empty, return null</li>
     * <li>else see {@link RandomUtil#getRandom(char[] sourceChar, int length)}</li>
     * </ul>
     */
    public static String getRandom(String source, int length) {
        return StringUtils.hasLength(source) ? getRandom(source.toCharArray(), length):null;
    }

    /**
     * 得到一个固定长度的随机字符串，其混合在sourcechar
     *
     * @param sourceChar
     * @param length
     * @return <ul>
     * <li>if sourceChar is null or empty, return null</li>
     * <li>if length less than 0, return null</li>
     * </ul>
     */
    public static String getRandom(char[] sourceChar, int length) {
        if (sourceChar == null || sourceChar.length == 0 || length < 0) {
            return null;
        }

        StringBuilder str = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(sourceChar[random.nextInt(sourceChar.length)]);
        }
        return str.toString();
    }

    /**
     * 得到随机int 0和最大值之间
     *
     * @param max
     * @return <ul>
     * <li>if max <= 0, return 0</li>
     * <li>else return random int between 0 and max</li>
     * </ul>
     */
    public static int getRandom(int max) {
        return getRandom(0, max);
    }

    /**
     * 在最小和最大之间的随机数
     *
     * @param min
     * @param max
     * @return <ul>
     * <li>if min > max, return 0</li>
     * <li>if min == max, return min</li>
     * <li>else return random int between min and max</li>
     * </ul>
     */
    public static int getRandom(int min, int max) {
        if (min > max) {
            return 0;
        }
        if (min == max) {
            return min;
        }
        return min + new Random().nextInt(max - min);
    }

    /**
     * 洗牌算法，随机置换指定的数组使用的默认的随机性
     *
     * @param objArray
     * @return
     */
    public static boolean shuffle(Object[] objArray) {
        if (objArray == null) {
            return false;
        }

        return shuffle(objArray, getRandom(objArray.length));
    }

    /**
     * 洗牌算法，随机置换指定的数组
     *
     * @param objArray
     * @param shuffleCount
     * @return
     */
    public static boolean shuffle(Object[] objArray, int shuffleCount) {
        int length;
        if (objArray == null || shuffleCount < 0 || (length = objArray.length) < shuffleCount) {
            return false;
        }

        for (int i = 1; i <= shuffleCount; i++) {
            int random = getRandom(length - i);
            Object temp = objArray[length - i];
            objArray[length - i] = objArray[random];
            objArray[random] = temp;
        }
        return true;
    }

    /**
     * 洗牌算法，随机置换指定数组的使用随机的默认源
     *
     * @param intArray
     * @return
     */
    public static int[] shuffle(int[] intArray) {
        if (intArray == null) {
            return null;
        }

        return shuffle(intArray, getRandom(intArray.length));
    }

    /**
     * 洗牌算法，随机置换指定的数组
     *
     * @param intArray
     * @param shuffleCount
     * @return
     */
    public static int[] shuffle(int[] intArray, int shuffleCount) {
        int length;
        if (intArray == null || shuffleCount < 0 || (length = intArray.length) < shuffleCount) {
            return null;
        }

        int[] out = new int[shuffleCount];
        for (int i = 1; i <= shuffleCount; i++) {
            int random = getRandom(length - i);
            out[i - 1] = intArray[random];
            int temp = intArray[length - i];
            intArray[length - i] = intArray[random];
            intArray[random] = temp;
        }
        return out;
    }

    public static String getRandomNumber(int digCount) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(digCount);
        for (int i = 0; i < digCount; i++) {
            sb.append((char) ('0' + rnd.nextInt(10)));
        }
        return sb.toString();
    }

    public static void main(String[] args) {

    }


    public static Boolean boolNum(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        } else {
            return true;
        }
    }
}
