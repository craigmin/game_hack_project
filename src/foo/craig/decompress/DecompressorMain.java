package foo.craig.decompress;

public class DecompressorMain {
	public static void main(String args[]){
		System.out.println("--- Start --- ");
		
		String path = "res\\";
		// 输入文件路径
		String input = path + "mfl";
		// 输出文件路径
		String output = path + "mfl_out";

		new MFLFileDecompressor(input, output).start();
		
		System.out.println("---  End  --- ");
	}
}
