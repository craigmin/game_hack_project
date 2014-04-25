package foo.craig.decompress;

import foo.craig.FileOperator;
import foo.craig.TextUtil;

public class MFLFileDecompressor {
	private String inputFilePath;
	private String outputFilePath;

	public MFLFileDecompressor(String inputFilePath, String outputFilePath) {
		this.inputFilePath = inputFilePath;
		this.outputFilePath = outputFilePath;
	}

	public void start() {
		byte[] bts = FileOperator.readFile(inputFilePath);
		for(int j = 0; j < 100; j++){
			System.out.printf("第%-3d个字节 ", j+1);
			
			String strBin = TextUtil.convertToBinaryString(bts[j]);
			Integer x = TextUtil.convertBinaryStringToInteger(getXstring(strBin));
			String strY = TextUtil.convertBinaryStringToHexString(getYstring(strBin));
			
//			if(j == 0 || x == 0 || x > 7) {
//				continue;
//			}

			for (int i = 0 ; i < 8 ; i++) {
				print(TextUtil.convertToBinary(bts[j], i));
			}

			print(" (" + TextUtil.convertToHexString(bts[j]) +")");
			print("  X: " + TextUtil.format2Digi(x.toString()));
			print("  Y: " + strY);
			
			if (j > 0 && x > 0) {
				int length = 6;
				if(x < 7) {
					length = x - 1;
				} else {
					int i = 0;
					do {
						i++;
						length += bts[j + i];
					} while (TextUtil.convertToHexString(bts[j + i]).equals("FF"));
				}
				System.out.printf("  length: %-2d", length);
//				print("  length: " + length);
				
				String strNextZ = TextUtil.convertToHexString(bts[j+1]);
				String strYZ = strY+strNextZ;
				print("  YZ: " + strYZ);
				String backSteps = "";
				if(!strYZ.equals("1FFF")) {
					backSteps = TextUtil.convertHexStringToInteger(strYZ).toString();
				} else {
					String strA = TextUtil.convertToHexString(bts[j+2]);
					String strB = TextUtil.convertToHexString(bts[j+3]);
					String strAB = strA+strB;
					int iBackSteps = TextUtil.convertHexStringToInteger(strAB) + TextUtil.convertHexStringToInteger("1FFF");
					backSteps = iBackSteps + " (AB: " + strAB + ")";
				}
				print("  回退量: " + backSteps);
			}
			
			println("");
		}
	}
	
	private String getXstring(String str){
		return str.substring(0, 3);
	}

	private String getYstring(String str){
		return str.substring(3, 8);
	}

	private static void print(Object obj) {
		System.out.print(obj);
	}
	
	private static void println(Object obj) {
		System.out.println(obj);
	}
}
