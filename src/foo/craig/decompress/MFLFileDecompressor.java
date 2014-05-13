package foo.craig.decompress;

import java.io.ByteArrayOutputStream;

import foo.craig.FileOperator;
import foo.craig.TextUtil;

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
		int outlength = 0;
		for (int j = 8; j < 5297; j++) {

			if (outlength > 0) {
				bos.write(bts, j, outlength);
				j = j + outlength - 1;
				outlength = 0;
				continue;
			}
			if (j > 5291) {
				print("");
			}

			System.out.printf("第%-3d个字节 ", j + 1);
			String strBin = TextUtil.convertToBinaryString(bts[j]);
			Integer iX = TextUtil
					.convertBinaryStringToInteger(getXstring(strBin));
			Integer iY = TextUtil
					.convertBinaryStringToInteger(getYstring(strBin)) + 1;
			print("  strBin: " + strBin);
			print("  output size: " + bos.size());
			// if(true) break;

			if (j == 8 || iX == 0) {
				outlength = iY;
			} else {
				int length = 6;
				if (iX < 7) {
					length = iX - 1;
				} else {
					int i = 0;
					do {
						i++;
						length += bts[++j];
					} while (TextUtil.convertToHexString(bts[j]).equals("FF"));
				}

				String strNextZ = TextUtil.convertToHexString(bts[++j]);
				print("  strNextZ: " + strNextZ);
				
				String strY = TextUtil
						.convertBinaryStringToHexString(getYstring(strBin));
				String strYZ = strY + strNextZ;
				Integer backSteps;
				if (!strYZ.equals("1FFF")) {
					backSteps = TextUtil.convertHexStringToInteger(strYZ);
				} else {
					String strA = TextUtil.convertToHexString(bts[++j]);
					String strB = TextUtil.convertToHexString(bts[++j]);
					String strAB = strA + strB;
					backSteps = TextUtil.convertHexStringToInteger(strAB)
							+ TextUtil.convertHexStringToInteger("1FFF");
				}
				// print("  回退量: " + backSteps);

				length += 3;
				byte[] outBts = bos.toByteArray();
				if (backSteps == 0) {
					byte bt = outBts[outBts.length - 1];
					for (int k = 0; k < length; k++) {
						bos.write(bt);
					}
				} else {
					if (backSteps < length) {
						byte[] tempBts = new byte[length];
						
						for(int k = 0; k < length; k++) {
							int offset = outBts.length - backSteps -1 + k;
							if(offset < (outBts.length -1)) {
							tempBts[k] = outBts[offset];
							} else{
								tempBts[k] = tempBts[(offset +1- outBts.length)];
							} 
						}

						bos.write(tempBts, 0, length);
					} else {
						bos.write(outBts, outBts.length - backSteps - 1, length);
					}
				}
			}
			println("");
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

	private static void print(Object obj) {
		System.out.print(obj);
	}

	private static void println(Object obj) {
		System.out.println(obj);
	}
}
