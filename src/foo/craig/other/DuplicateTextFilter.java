package foo.craig.other;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import foo.craig.util.TextUtil;

public class DuplicateTextFilter {
	public static void main(String args[]){
		try {
			String fileName = "res/ittledew.txt";
			File file = new File(fileName);
			FileInputStream fileInputStream = new FileInputStream(file);
			BufferedInputStream bi = new BufferedInputStream(fileInputStream);
			int i = 0;
			byte[] str = new byte[bi.available()];
			while (true) {
				int temp = bi.read();
				if (temp == -1) {
					break;
				}
				str[i++] = (byte) temp;
			}
			bi.close();
			
			StringBuffer strB = new StringBuffer();
			String strC = "";
			for(int offset = 0 ; offset < str.length-2; offset +=3){
				String strCu = TextUtil.readHexString(str, offset, 3);
				if(!strC.equals(strCu)){
					strB.append(strCu);
					strC = strCu;
				}
			}
			System.out.println(strB);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
