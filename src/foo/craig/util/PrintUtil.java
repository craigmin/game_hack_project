package foo.craig.util;

public class PrintUtil {
	private static boolean isEnable = true;
	
	public static void print(String format, Object... obj) {
		if(!isEnable) {
			return;
		}

		System.out.printf(format, obj);
	}

	public static void println(String format, Object... obj) {
		if(!isEnable) {
			return;
		}
		
		if(obj != null && obj.length > 0) {
			System.out.printf(format, obj);
		} else {
			System.out.print(format);
		}
		System.out.println();
	}
}
