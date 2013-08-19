package thu.nlp.kbp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 命令行调用工具，可以将一系列命令行参数连接起来形成调用参数，
 * 然后调用命令行之后再以字符串形式返回输出结果。
 * @author WangYan
 *
 */
public class ConsoleUtil {

	/**
	 * 本函数将命令行参数用空格连接起来，如"-t" "3" "-a" "file.txt"
	 * 将被连接为"-t 3 -a file.txt"。
	 * @param args
	 * @return
	 */
	public static String getCmd(String[] args) {
		StringBuilder sb = new StringBuilder();
		for (String temp : args) {
			sb.append(" ");
			sb.append(temp);
		}
		return sb.toString();
	}
	
	/**
	 * 本函数调用命令行，命令为传入的字符串cmd，输出结果将以字符串形式返回。
	 * @param cmd
	 * @param outputFile
	 * @return
	 */
	public static String consoleCall(String cmd) {
		StringBuilder sb = new StringBuilder();
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			Runtime.getRuntime();
			p.waitFor();
			InputStream is = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				sb.append(temp);
				sb.append('\n');
			}
			p.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static String consoleCall(String[] cmd) {
		StringBuilder sb = new StringBuilder();
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			Runtime.getRuntime();
			p.waitFor();
			InputStream is = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				sb.append(temp);
				sb.append('\n');
			}
			p.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
}
