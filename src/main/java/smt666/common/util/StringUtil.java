package smt666.common.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 解析字符串的工具类。
 * @author 27140
 */
public class StringUtil {

    /**
     * 注意，字符串中，只能由数值类型的字符，如果出现字母则会解析异常。
     * @param str
     * @return
     */
    public static List<Integer> splitToListInt(String str) {
        // {1,2, ,3,4,   ,5,6  } ====> {1,2,3,4,5,6}
        List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
        return strList.stream().map(strItem -> Integer.parseInt(strItem)).collect(Collectors.toList());
    }
}
