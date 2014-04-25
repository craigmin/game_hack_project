package foo.craig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AgemoUtil {
	private final static String LEFT_TAG = "#### ";
	private final static String RIGHT_TAG = " ####";
	private final static String END_TAG = "end";
	private static String lineSeparator = System.getProperties().getProperty(
			"line.separator");

	private static Map<Integer, String> excludeChar = new HashMap<Integer, String>();
	private static Set<Integer> excludeBreakChar = new HashSet<Integer>();

	static {
		excludeChar.put(0, END_TAG);
		excludeChar.put(32, " ");
		excludeChar.put(40, "(");
		excludeChar.put(46, ".");
		excludeChar.put(115, "s");
		excludeChar.put(83, "S");
		excludeChar.put(89, "Y");
		
		excludeBreakChar.add(0);
	}

	/*
	 * 转换MBM文件 *
	 */
	public static String toAgemoString(MBMBean bean) {
		if (bean == null || bean.getMsgCount() < 1) {
			return "";
		}

		String lineSeparator = System.getProperties().getProperty(
				"line.separator");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bean.getMsgCount(); i++) {
			sb.append(LEFT_TAG);
			sb.append(i + 1);
			sb.append(RIGHT_TAG);
			sb.append(lineSeparator);
			sb.append(bean.getMessages().get(i));
			sb.append(lineSeparator);
		}

		return sb.toString();
	}

	/*
	 * 转换UTF-8格式的byte流 *
	 */
	public static String toAgemoString(byte[] bs) {
		if (bs == null || bs.length < 1) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 1, offset = 0; offset < bs.length - 2;) {
			StringBuffer text = new StringBuffer();
			int offsetStart = offset + 1;
			for (; offset < bs.length - 2;) {
				if (bs[offset] == 10) {
					offset++;
					text.append(lineSeparator);
					continue;
				}

				boolean isContain = false;
				boolean isBreak = false;
				for (Integer exC : excludeChar.keySet()) {
					if (bs[offset] == exC) {
						offset++;
						isContain = true;
					} else if (bs[offset + 1] == exC) {
						text.append(TextUtil.readHexString(bs, offset, 1));
						offset += 2;
						isContain = true;
					} else if (bs[offset + 2] == exC) {
						text.append(TextUtil.readHexString(bs, offset, 2));
						offset += 3;
						isContain = true;
					}

					if (isContain) {
						if (!excludeBreakChar.contains(exC)) {
							text.append(excludeChar.get(exC));
						} else {
							isBreak = true;
						}
						break;
					}
				}

				if (isContain) {
					if (isBreak) {
						break;
					}
					continue;
				}

				String str = TextUtil.readHexString(bs, offset, 3);
				int wordOffset = offset;
				if (bs[offset++] == 0 || bs[offset++] == 0 || bs[offset++] == 0) {
					try {
						text.append(str.substring(0, offset - wordOffset - 1));
					} catch (Exception e) {

					}
					break;
				}
				text.append(str);
			}

			if (!TextUtil.validateJP(text.toString())) {
				continue;
			}

			sb.append(LEFT_TAG);
			String offsetStr = Integer.toHexString(offset).toUpperCase();
			while(offsetStr.length() < 8) {
				offsetStr = "0" + offsetStr;
			}
			sb.append(offsetStr);
			sb.append(",");
			sb.append(offset - offsetStart);
			//sb.append(i++);
			sb.append(RIGHT_TAG);
			sb.append(lineSeparator);

			sb.append(text);
			sb.append(lineSeparator);
			sb.append(lineSeparator);
		}

		return sb.toString();
	}
}
