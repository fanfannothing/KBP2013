package thu.nlp.kbp.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class reads text files line by line.
 * try and catch are so ugly to appear again and again.
 * The typical usage is:
 * 		MyFileReader reader = new MyFileReader(path);
 * 		String line = null;
 * 		while ((line = reader.getNextLine()) != null) {
 * 			// do something
 * 		}
 * 		reader.close();
 * 
 * @author WangYan
 *
 */
public class MyFileReader {
	public String filename = null;
	private BufferedReader reader = null;
	
	public MyFileReader(String filename) {
		this.filename = filename;
		try {
			reader = new BufferedReader(new FileReader(filename));
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Read all content from file. Different lines are separated by '\n'.
	 * @param filename
	 * @return
	 */
	public static String readFromFile(String filename) {
		StringBuilder sb = new StringBuilder();
		MyFileReader mfr = new MyFileReader(filename);
		String temp = null;
		while ((temp = mfr.getNextLine()) != null) {
			sb.append(temp);
			sb.append('\n');
		}
		mfr.close();
		return sb.toString();
	}
	
	/**
	 * Read the next line. If there is a next line, return its
	 * content; otherwise return null.
	 * @return
	 */
	public String getNextLine() {
		String temp = null;
		try {
			temp = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
	
	/**
	 * Deprecated.
	 * @param lines
	 * @return
	 */
	public String[] getSeveralLines(int lines) {
		String[] content = new String[lines];
		int i = 0;
		try {
			while ((content[i] = reader.readLine()) != null && i < lines) {
				i++;				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (i < lines) {
			for (int j = i; j < lines; j++) {
				content[j] = "";
			}
		}
		return content;
	}
	
	/**
	 * It is better to call close() when you don't need to read.
	 */
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the file and read it from the beginning.
	 */
	public void refresh() {
		try {
			reader.close();
			reader = new BufferedReader(new FileReader(this.filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
