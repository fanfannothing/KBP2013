package thu.nlp.kbp.lyk;

import thu.nlp.kbp.utils.ConsoleUtil;

public class DocInfoExtract {
	static String dir="/home/lyk423";
	private void ParserWithSenna(String filename)
	{
		String cmd ="/home/lyk423/senna/senna  </home/lyk423/input/1.txt -ner";// dir+"/senna/senna -ner <"+dir+"/input/"+filename;
		String[] cmds = new String[] {"/home/lyk423/senna/senna", "-ner", "< /home/lyk423/input/1.txt"};
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
