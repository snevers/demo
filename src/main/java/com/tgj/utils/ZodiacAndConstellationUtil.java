package com.tgj.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class ZodiacAndConstellationUtil {

	private final static String[] ZODIAC = new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪" };
	private final static int[] dayArr = new int[] { 20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22 };
	private final static String[] CONSTELLATION = new String[] { "摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
			"狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" };

	/**
	 * Java通过生日计算星座
	 * 
	 * @param month
	 * @param day
	 * @return
	 */
	public static String getConstellation(Date date) {
		Calendar calendar = DateUtils.toCalendar(date);
		int month = calendar.get(Calendar.MONTH) + 1, day = calendar.get(Calendar.DAY_OF_MONTH);
		
		return day < dayArr[month - 1] ? CONSTELLATION[month - 1] : CONSTELLATION[month];
	}

	/**
	 * 通过生日计算属相
	 * 
	 * @param year
	 * @return
	 */
	public static String getZodiac(Date date) {
		Calendar calendar = DateUtils.toCalendar(date);
		int year = calendar.get(Calendar.YEAR);
		if (year < 1900) {
			return "未知";
		}
		int start = 1900;
		return ZODIAC[(year - start) % ZODIAC.length];
	}
}
