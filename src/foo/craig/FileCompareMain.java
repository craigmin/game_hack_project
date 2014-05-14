package foo.craig;

import foo.craig.util.FileOperator;
import foo.craig.util.PrintUtil;
import foo.craig.util.TextUtil;

public class FileCompareMain {

	public static void main(String[] args) {
		PrintUtil.println("--- FileCompareMain Start ---");

		String path = "res\\";
		String strFstFileName = path + "A_not_comp.bin";
		String strSndFileName = path + "mfl_out.bin";

		byte[] bts1 = FileOperator.readFile(strFstFileName);
		byte[] bts2 = FileOperator.readFile(strSndFileName);

		int length = Math.min(bts1.length, bts2.length);
		int iDiffNum = 0;
		for (int i = 0; i < length; i++) {
			if (bts1[i] != bts2[i]) {
				iDiffNum++;
				PrintUtil.println("%-8d 字节不同. %s,%s", i,
						TextUtil.convertToHexString(bts1[i]),
						TextUtil.convertToHexString(bts2[i]));
			}
		}

		PrintUtil.println("有 %d 个字节不同", iDiffNum);
		if (bts1.length != bts2.length) {
			PrintUtil.println("文件长度不同. %d, %d", bts1.length, bts2.length);
		}

		PrintUtil.println("--- FileCompareMain End ---");
	}
}
