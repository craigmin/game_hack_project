package foo.craig.decompress;

import java.io.ByteArrayOutputStream;

import foo.craig.util.FileOperator;
import foo.craig.util.PrintUtil;
import foo.craig.util.TextUtil;

public class MFLFileDecompressor {
	private String inputFilePath;
	private String outputFilePath;
	private ByteArrayOutputStream bos;

	public MFLFileDecompressor(String inputFilePath, String outputFilePath) {
		this.inputFilePath = inputFilePath;
		this.outputFilePath = outputFilePath;
		this.bos = new ByteArrayOutputStream();
	}

	public MFLFileDecompressor start() {
		byte[] bts = FileOperator.readFile(inputFilePath);
		int type = 0;
		for (int j = 8; j < bts.length; j++) {

			PrintUtil.print("第%-3d个字节 ", j + 1);
			String strBin = TextUtil.convertToBinaryString(bts[j]);
			Integer iX = TextUtil
					.convertBinaryStringToInteger(getXstring(strBin));
			Integer iY = TextUtil
					.convertBinaryStringToInteger(getYstring(strBin)) + 1;
			PrintUtil.print("  strBin: %s", strBin);
			PrintUtil.print("  output size: %s", bos.size());

			if (j == 8) {
				type = iX;
			}

			if (j == 8 || iX == 0) {
				bos.write(bts, j + 1, iY);
				j += iY;
			} else {
				int length = iX - 1;
				if (iX == 7) {
					if (type == 1) {
						int i = 0;
						do {
							i++;
							length += (0x000000ff & bts[++j]);
						} while (TextUtil.convertToHexString(bts[j]).equals(
								"FF"));
					} else {
						length += (0x000000ff & bts[++j]);
					}
				}

				Integer backSteps;
				String strNextZ = TextUtil.convertToHexString(bts[++j]);
				String strY = TextUtil
						.convertBinaryStringToHexString(getYstring(strBin));
				String strYZ = strY + strNextZ;

				if (type == 1) {
					if (!strYZ.equals("1FFF")) {
						backSteps = TextUtil.convertHexStringToInteger(strYZ);
					} else {
						String strA = TextUtil.convertToHexString(bts[++j]);
						String strB = TextUtil.convertToHexString(bts[++j]);
						String strAB = strA + strB;
						backSteps = TextUtil.convertHexStringToInteger(strAB)
								+ TextUtil.convertHexStringToInteger("1FFF");
					}
				} else {
					backSteps = TextUtil.convertHexStringToInteger(strYZ);
				}

				length += 3;
				byte[] outBts = bos.toByteArray();
				if (backSteps == 0) {
					byte bt = outBts[outBts.length - 1];
					for (int k = 0; k < length; k++) {
						bos.write(bt);
					}
				} else if (backSteps < length) {
					byte[] tempBts = new byte[length];

					for (int k = 0; k < length; k++) {
						int offset = outBts.length - backSteps - 1 + k;
						if (offset < outBts.length) {
							tempBts[k] = outBts[offset];
						} else {
							tempBts[k] = tempBts[(offset - outBts.length)];
						}
					}

					bos.write(tempBts, 0, length);
				} else {
					bos.write(outBts, outBts.length - backSteps - 1, length);
				}
			}
			PrintUtil.println("");
		}

		return this;
	}

	public void output() {
		FileOperator.writeFile(outputFilePath, bos);
	}

	private String getXstring(String str) {
		return str.substring(0, 3);
	}

	private String getYstring(String str) {
		return str.substring(3, 8);
	}
}
