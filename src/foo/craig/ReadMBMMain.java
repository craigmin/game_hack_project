/*
 * author: Craig Min
 * date: 2014/03/14
 * description: 一点汉化组破解招募考核程序 Main
 * **/
package foo.craig;

import org.apache.commons.lang.StringUtils;

public class ReadMBMMain {

	public static void main(String args[]) throws Exception {
		// 文件路径
		String path = "res\\";
		// 输入mbm文件路径
		String input = path + "libgame";
//		String input = path + "resources";
		// 输出agemo格式文件路径
		String output = path + "libgame.txt";

		// 读取mbm文件到byte流
		byte[] bt = FileOperator.readFile(input);
		if (bt == null) {
			print(input + " <-- 文件不能读取");
			return;
		}

		// 将byte流格式化成mbm文件对象
		/*MBMBean bean = MBMBean.formatMBMBean(bt);
		if (bean == null) {
			print(input + " <-- mbm文件格式有问题,没有文本");
			return;
		}
		
		// 显示mbm文件对象信息
		print("---------- MBM Format String ----------");
		print(bean);

		// mbm格式bean转换成agemo格式的字符串
		String content = AgemoUtil.toAgemoString(bean);
		if (StringUtils.isEmpty(content)) {
			print(input + " <-- mbm文件格式有问题,无法转成Agemo格式");
			return;
		}*/

		// byte流转换成agemo格式的字符串
		String content = AgemoUtil.toAgemoString(bt);
		if (StringUtils.isEmpty(content)) {
			print(input + " <-- byte流有问题,无法转成Agemo格式");
			return;
		}

		// 显示agemo格式字符串
		print("---------- Agemo Format String ----------");
		print(content);

		// 输出agemo字符串到文件
		FileOperator.writeFile(output, content);
	}

	private static void print(Object obj) {
		System.out.println(obj);
	}
}
