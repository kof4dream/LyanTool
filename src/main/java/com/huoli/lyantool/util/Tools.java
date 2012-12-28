package com.huoli.lyantool.util;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang.StringUtils;

public class Tools {

	private static final Pattern numberPat = Pattern
			.compile("-?[0-9]+(.[0-9]+)?");;

	/**
	 * 图吧地图的经纬度解密
	 * 
	 * @param latlon
	 * @return
	 */
	public static double[] geoLatLnt(String latlon) {

		if (latlon == null || latlon.trim().equals("")) {
			return new double[] { 0, 0 };
		}

		int len = latlon.length();
		int tmp = 0;
		String org = "";
		int maxNumPos = -1;
		int maxNum = 0;
		long diff = 0;
		long sum = 0;
		double lat = 0;
		double lnt = 0;

		try {
			for (int i = 0; i < len; i++) {
				tmp = latlon.charAt(i) - 'A';
				if (tmp >= 10) {
					tmp -= 7;
				}
				// 把tmp转换成三十六进制的字符
				org += Integer.toString(tmp, 36);
				if (tmp > maxNum) {
					maxNumPos = i;
					maxNum = tmp;
				}
			}

			diff = Long.parseLong(org.substring(0, maxNumPos), 16);
			sum = Long.parseLong(org.substring(maxNumPos + 1), 16);

			lnt = (diff + sum - 3409) / 2;
			lat = (sum - lnt) / 100000;
			lnt = lnt / 100000;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new double[] { 0, 0 };
		}

		return new double[] { lat, lnt };
	}

	/**
	 * 计算折扣
	 * 
	 * @param showPrice
	 * @param salePrice
	 * @return
	 */
	public static String getSaleOff(double showPrice, double salePrice) {

		DecimalFormat format = new DecimalFormat("#.##");
		if (salePrice <= showPrice) {
			double off = salePrice / showPrice;
			return format.format(off);
		} else {
			return "0";
		}
	}

	/**
	 * 生成6位随机验证码
	 * 
	 * @return
	 */
	public static String getRandomVerifyCode() {

		Random random = new Random(System.currentTimeMillis());
		int code = random.nextInt(9999);
		String _code = String.format("%04d", code);

		return _code;
	}

	/**
	 * 验证信用卡号
	 * 
	 * @param cardNumber
	 * @return
	 */
	// -------------------
	// Perform Luhn check
	// -------------------
	public static boolean checkCreditCard(String cardNumber) {
		String digitsOnly = getDigitsOnly(cardNumber);
		int sum = 0;
		int digit = 0;
		int addend = 0;
		boolean timesTwo = false;
		for (int i = digitsOnly.length() - 1; i >= 0; i--) {
			digit = Integer.parseInt(digitsOnly.substring(i, i + 1));
			if (timesTwo) {
				addend = digit * 2;
				if (addend > 9) {
					addend -= 9;
				}
			} else {
				addend = digit;
			}
			sum += addend;
			timesTwo = !timesTwo;
		}
		int modulus = sum % 10;
		return modulus == 0;
	}

	// --------------------------------
	// Filter out non-digit characters
	// --------------------------------
	private static String getDigitsOnly(String s) {
		StringBuffer digitsOnly = new StringBuffer();
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c)) {
				digitsOnly.append(c);
			}
		}
		return digitsOnly.toString();
	}

	/**
	 * 验证邮箱格式
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		return m.find();
	}

	/**
	 * 验证是否为手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean checkMobile(String mobile) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	/**
	 * 将有序的自增id转换成混淆的id
	 * 
	 * @param id
	 * @return
	 */
	public static String getProguardId(long id) {

		return null;
	}

	/**
	 * 得到 全拼
	 * 
	 * @param src
	 * @return
	 */
	public static String getPingYin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// 判断是否为汉字字符
				if (java.lang.Character.toString(t1[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
					t4 += (t2[0].toUpperCase().charAt(0) + t2[0].substring(1));
				} else {
					t4 += java.lang.Character.toString(t1[i]);
				}
			}
			return t4;
		} catch (BadHanyuPinyinOutputFormatCombination e1) {
			e1.printStackTrace();
		}
		return t4;
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param numStr
	 * @return
	 */
	public static boolean isNumeric(String numStr) {

		if (StringUtils.isBlank(numStr)) {
			return false;
		}

		Matcher mat = numberPat.matcher(numStr);
		if (mat.matches()) {
			return true;
		}

		// try {
		// Double.parseDouble(numStr);
		// } catch (NumberFormatException e) {
		// // TODO Auto-generated catch block
		// return false;
		// }

		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// System.out.println(getSaleOff(268, 189));

		// String str = "-112.144";
		// System.out.println(Tools.isNumeric(str));

		String str2 = null;
		System.out.println(StringUtils.isNotEmpty(str2));
	}

}
