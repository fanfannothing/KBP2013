package thu.nlp.kbp.lyk;

import thu.nlp.kbp.utils.ConsoleUtil;

public class DocInfoExtract {
	static String dir="/home/lyk423";
	private void ParserWithSenna(String filename)
	{
		String cmd ="cmd D:/Packages/senna/senna-win32.exe -ner < D:/Packages/1.txt > D:/Packages/2.txt";// dir+"/senna/senna -ner <"+dir+"/input/"+filename;
		
		System.out.println(cmd);
		String str = ConsoleUtil.consoleCall(cmd);
		System.out.println(str);
	}
	public DocInfoExtract()
	{
	}
	public void Run()
	{
		ParserWithSenna("1.txt");
	}
}
