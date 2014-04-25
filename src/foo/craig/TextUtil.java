package foo.craig;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
	public static String readHexString(byte[] b, int offset, int len) {
		String sen = "";
		for (int i = offset; i < offset + len; i++) {
			String hex = convertToHexString(b[i]);
			sen += hex;
		}
		return decode(sen);
	}

	private static String hexString = "0123456789ABCDEF";

	private static String decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				bytes.length() / 2);
		for (int i = 0; i < bytes.length(); i += 2) {
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
					.indexOf(bytes.charAt(i + 1))));
		}
		try {
			// Shift_JIS
			return new String(baos.toByteArray(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new String(baos.toByteArray());
	}

	public static int convertHextoInt(final byte[] bt, int offset) {
		int r = 0;
		for (int i = offset + 4 - 1; i >= offset; i--) {
			r <<= 8;
			r |= (bt[i] & 0x000000ff);
		}
		return r;
	}

	/*
	 * true: contain Japanese character false: not contain Japanese character *
	 */
	public static boolean validateJP(String str) {
		Pattern p = Pattern
				.compile("[\\u4e00-\\u9fa5\\u3041-\\u30ff\\uff61-\\uff9f]");
		Matcher m = p.matcher(str);

		if (m.find()) {
			return true;
		}
		return false;
	}

	public static char convertToBinary(byte bt, int index) {
		return convertToBinaryString(bt).charAt(index);
	}

	public static String convertToBinaryString(String bt) {
		if (bt.length() < 8) {
			bt = '0' + bt;
			return convertToBinaryString(bt);
		}
		return bt;
	}
	
	public static String convertToBinaryString(byte bt) {
		String str = Integer.toBinaryString(bt & 0xFF);
		return convertToBinaryString(str);
	}

	public static Integer convertBinaryStringToInteger(String bString) {
		byte result = 0;
		for (int i = bString.length() - 1, j = 0; i >= 0; i--, j++) {
			result += (Byte.parseByte(bString.charAt(i) + "") * Math.pow(2, j));
		}
		return (int) result;
	}

	public static String convertToHexString(byte bt){
		return format2Digi(Integer.toHexString(bt & 0xFF));
	}
	
	public static String format2Digi(String str){
		if (str.length() == 1) {
			str = '0' + str;
		}
		
		return str.toUpperCase();
	}

	public static String convertBinaryStringToHexString(String ystring) {
		int i = convertBinaryStringToInteger(ystring);
		return convertToHexString((byte) i);
	}
	
	public static Integer convertHexStringToInteger(String str) {
		return Integer.valueOf(str, 16);
	}
}
