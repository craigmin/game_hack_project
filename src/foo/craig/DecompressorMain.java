package foo.craig;

import foo.craig.decompress.MFLFileDecompressor;
import foo.craig.util.PrintUtil;

public class DecompressorMain {

	public static void main(String args[]){
		PrintUtil.println("--- DecompressorMain Start --- ");
		
		String path = "res\\";
		// 输入文件路径
		String input = path + "A_comp.bin";
		// 输出文件路径
		String output = path + "mfl_out.bin";

		new MFLFileDecompressor(input, output).start().output();

		PrintUtil.println("--- DecompressorMain End   --- ");
	}
}
