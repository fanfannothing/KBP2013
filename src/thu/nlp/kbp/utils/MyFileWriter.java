package thu.nlp.kbp.utils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class write contents into the file. When write() is called, 
 * the file would be changed at once. If there are many words to 
 * write, you could use StringBuilder to hold the segments.
 * Typical usage is:
 * 		
 * 		MyFileWriter writer = new MyFileWriter(path, false);
 * 		StringBuilder buffer = new StringBuilder();
 * 		buffer.append("@#$#");
 * 		buffer.append('\n');
 * 		writer.write(buffer.toString());
 * 		writer.close();
 * 
 * @author WangYan
 *
 */
public class MyFileWriter {
	
	private String filename;
	private FileWriter writer = null;
	
	/**
	 * Warning:
	 * To open a writing stream, here the boolean indicator means 
	 * whether to overwrite the file, rather than whether to append.
	 * I think that way is more natural to remember. 
	 * That means, if isOverwrite == true, the original content
	 * would be cleaned.
	 * @param filename
	 * @param isOverwrite
	 */
	public MyFileWriter(String filename, boolean isOverwrite) {		
		File file = new File(filename);
		if (!file.exists()) {
			file.mkdirs();
		}
		if (file.isDirectory()) {
			file.delete();
		}
		
		this.filename = filename;
		try {
			writer = new FileWriter(filename, !isOverwrite);
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.err.println("Cannot open file \"" + filename + "\", the name may be invalid!");
		}
	}
	
	/**
	 * Write a string into file and change it at once.
	 * @param s
	 */
	public synchronized void write(String s) {
			try {
			//	System.out.println("writing: " + s);
				if (writer != null) {
					writer.write(s);
					writer.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				System.err.println("Cannot write to file \"" + filename + "\"");
			}
	}
	
	/**
	 * It is better to close a writer after you don't need.
	 */
	public void close() {
		try {
			if (writer != null)
				writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.err.println("Cannot close file \"" + filename + "\"");
		}
	}
	
}
