package foo.craig.decompress;

public class DecompressorMain {
	public static void main(String args[]){
		System.out.println("--- Start --- ");
		
		String path = "res\\";
		// 输入文件路径
		String input = path + "A_comp.bin";
		// 输出文件路径
		String output = path + "mfl_out.bin";

		new MFLFileDecompressor(input, output).start().output();
		
		System.out.println("---  End  --- ");
	}
}
