package com.judicature.databackend.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Gabri
 */
public class StringFilter {

    /**
     * 单个字符串普通空格过滤
     *
     * @param str
     * @return
     */
    public static String spaceFilter(String str) {
        return str.replaceAll(" ", "");
    }

    /**
     * 字符串数组普通空格过滤
     *
     * @param strs
     * @return
     */
    public static List<String> spaceFilter(List<String> strs) {
        List<String> strsFilter = new ArrayList<>();
        for (String str : strs) {
            String strFilter = StringFilter.spaceFilter(str);
            strsFilter.add(strFilter);
        }
        return strsFilter;
    }


    /**
     * 消除字符串中的HTML网页不间断空格
     *
     * @param str
     * @return
     */
    public static String nonBreakingSpaceFilter(String str) {
        if (str == null) {
            return str;
        }
        // 不间断空格
        StringBuilder builder = new StringBuilder();
        int position = 0;
        char currentChar;

        while (position < str.length()) {
            currentChar = str.charAt(position++);
            if (currentChar != (char) 160) {
                builder.append(currentChar);
            }
        }
        return builder.toString();
    }


    /**
     * 消除字符串数组的所有的HTML不间断空格
     *
     * @param strs
     * @return
     */
    public static List<String> nonBreakingSpaceFilter(List<String> strs) {
        if (strs == null) {
            return strs;
        }
        List<String> strsFilter = new ArrayList<>();
        for (String strFilter : strs) {
            strsFilter.add(StringFilter.nonBreakingSpaceFilter(strFilter));
        }
        return strsFilter;

    }

    //过滤特殊字符
    public static List<String> specialCharFilter(List<String> strs) {
        if (strs == null) {
            return strs;
        }
        List<String> strsFilter = new ArrayList<>();
        for (String str : strs) {
            strsFilter.add(StringFilter.specialCharacterFilter(str));
        }
        return strsFilter;
    }


    /**
     * 对属性值进行分隔 返回字符串数组
     *
     * @param parameterValue
     * @return
     */
    public static List<String> parameterValueSeparates(String parameterValue) {
        List<String> valueList=Arrays.asList(parameterValue.split("、"));
        if (valueList.size() == 1) {
            valueList = Arrays.asList(parameterValue.split("，"));
        }
        if (valueList.size() == 1) {
            valueList = Arrays.asList(parameterValue.split("；"));
        }
        if (valueList.size() == 1) {
            valueList = Arrays.asList(parameterValue.split("/"));
        }
        if (valueList.size() == 1) {
            valueList = Arrays.asList(parameterValue.split(","));
        }
        if (valueList.size() == 1) {
            valueList = Arrays.asList(parameterValue.split("："));
        }
        if (valueList.size() == 1) {
            valueList = new ArrayList<>();
            valueList.add(parameterValue);
        }
        List<String> valueListFilter = StringFilter.nonBreakingSpaceFilter(valueList);
        valueListFilter = StringFilter.spaceFilter(valueListFilter);
        return valueListFilter;
    }

    /**
     * 消除字符串中的特殊字符
     *
     * @param str
     * @return
     */
    public static String specialCharacterFilter(String str) {
        if (str == null) {
            return str;
        }

        return str.replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "");
    }

    /**
     * 消除属性名中的一些特殊字符
     *
     * @param parameterNames
     * @return 过滤后的属性名
     */
    public static List<String> parameterNameFilter(List<String> parameterNames) {
        List<String> parameterNameFilters = new ArrayList<>();
        for (String parameterName : parameterNames) {
            String filterName = StringFilter.specialCharacterFilter(parameterName);
            try {
                parameterNameFilters.add(filterName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return parameterNameFilters;
    }
}
