package foo.craig;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperator {

	public static byte[] readFile(String fileName) {
		byte[] str = null;
		try {
			File file = new File(fileName);
			FileInputStream fileInputStream = new FileInputStream(file);
			BufferedInputStream bi = new BufferedInputStream(fileInputStream);

			str = new byte[bi.available()];
			int i = 0;
			while (true) {
				int temp = bi.read();
				if (temp == -1) {
					break;
				}
				
				// 修正取得0xFF的问题
				Byte bt = new Byte((byte) temp);
				str[i++] = bt.intValue() < -1 ? -1 : bt.byteValue();
//				print(temp, i);
				
			}

			bi.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		print(str);
		return str;
	}

	public static void writeFile(String output, String content) {
		try {
			File f = new File(output);
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(content);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 输出16进制byte到console
	private static void print(int b, int i){
		String hex = Integer.toHexString(b & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		System.out.print(hex + " ");
		if(i%1024 == 0){
			System.out.println();
		}
	}
	
	// 输出byte流到out.txt文件
	private static void print(byte[] bs){
		System.out.println(bs.length + " ---------");
		StringBuffer str = new StringBuffer();
		for(int offset = 0 ; offset < bs.length-2; offset +=3){
			str.append(TextUtil.readHexString(bs, offset, 3));
		}
		FileOperator.writeFile("res/out.txt", str.toString());
		System.out.println(" --------- End");
	}
}
