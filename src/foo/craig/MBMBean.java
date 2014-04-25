package foo.craig;

import java.util.ArrayList;

public class MBMBean {
	private static final int FILE_HEADER_LENGTH = 16;
	private static final int FILE_OFFSET_UNIT = 4;
	private static final int MSG_END_TAG = 4;
	private int msgCount;
	private ArrayList<Integer> msgOffset;
	private ArrayList<Integer> msgLength;
	private int headerOffset;
	private int fileLength;
	private ArrayList<String> messages;

	public int getMsgCount() {
		return msgCount;
	}

	public ArrayList<Integer> getMsgOffset() {
		return msgOffset;
	}

	public ArrayList<Integer> getMsgLength() {
		return msgLength;
	}

	public int getHeaderOffset() {
		return headerOffset;
	}

	public int getFileLength() {
		return fileLength;
	}

	public ArrayList<String> getMessages() {
		return messages;
	}

	public static MBMBean formatMBMBean(byte[] bt) {
		MBMBean bean = new MBMBean();

		// 读取文件头部信息
		bean.fileLength = TextUtil.convertHextoInt(bt, 12);
		// 读取当前文本头部信息
		bean.msgCount = TextUtil.convertHextoInt(bt, FILE_HEADER_LENGTH);
		bean.headerOffset = TextUtil.convertHextoInt(bt, FILE_HEADER_LENGTH
				+ FILE_OFFSET_UNIT);

		if (bean.msgCount < 1) {
			return bean;
		}

		bean.msgOffset = new ArrayList<Integer>(bean.msgCount);
		bean.msgLength = new ArrayList<Integer>(bean.msgCount);
		bean.messages = new ArrayList<String>(bean.msgCount);
		int offset = FILE_HEADER_LENGTH + bean.headerOffset;
		for (int i = 1; i < bean.msgCount + 1; i++, offset += FILE_OFFSET_UNIT * 4) {

			int msgLength = TextUtil.convertHextoInt(bt, offset + FILE_OFFSET_UNIT)
					- MSG_END_TAG;
			int msgOffset = TextUtil.convertHextoInt(bt, offset + FILE_OFFSET_UNIT * 2);
			String msg = TextUtil.readHexString(bt, msgOffset, msgLength);

			bean.msgLength.add(msgLength);
			bean.msgOffset.add(msgOffset);
			bean.messages.add(msg);
		}

		return bean;
	}

	public String toString() {
		String lineSeparator = System.getProperties().getProperty(
				"line.separator");
		StringBuffer sb = new StringBuffer();
		sb.append("文件共有文本: " + this.msgCount + "条");
		sb.append(lineSeparator);
		sb.append("文件长度: " + this.fileLength);
		sb.append(lineSeparator);
		sb.append("文件头偏移量: " + this.headerOffset);
		sb.append(lineSeparator);

		if (this.getMsgCount() < 0) {
			return sb.toString();
		}

		sb.append("------------- 文本  ------------");
		sb.append(lineSeparator);
		for (int i = 0; i < this.getMsgCount(); i++) {
			sb.append("第");
			sb.append(i + 1);
			sb.append("条 --- 偏移量: ");
			sb.append(this.getMsgOffset().get(i));
			sb.append(" --- 长度: ");
			sb.append(this.getMsgLength().get(i));
			sb.append(" --- ");
			sb.append(this.getMessages().get(i));
			sb.append(lineSeparator);
			sb.append(lineSeparator);
		}

		return sb.toString();
	}
}
