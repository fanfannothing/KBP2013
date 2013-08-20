package thu.nlp.kbp.element;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import thu.nlp.kbp.utils.MyFileReader;


/**
 * The query contains its unique String id, the content and a list of entity.
 * The core process of recognizing xml tags is here, using regex.
 * 
 * @author WangYan
 * 
 */
public class Query {

	private String queryId = null;
	private String docId = null;
	private String name = null;
	private int begin = -1;
	private int end = -1;

	public String getQueryId() {
		return queryId;
	}

	public String getDocId() {
		return docId;
	}

	public String getName() {
		return name;
	}

	public int getBegin() {
		return begin;
	}

	public int getEnd() {
		return end;
	}
	
	private static boolean patternInitialized = false;

	/**
	 * The patterns used to parse the query doc.
	 */
	private static Pattern patId = null;
	private static Pattern patDocid = null;
	private static Pattern patName = null;
	private static Pattern patStartOffset = null;
	private static Pattern patEndOffset = null;

	private static void initPattern() {
		patId = Pattern.compile("<query\\sid=\"(.*)\"");
		patDocid = Pattern.compile("<docid>(.*)</docid>");
		patName = Pattern.compile("<name>(.*)</name>");
		patStartOffset = Pattern
				.compile("<beg>(.*)</beg>");
		patEndOffset = Pattern.compile("<end>(.*)</end>");
	}

	/**
	 * Input a list of String in XML-format.
	 * 
	 * @param strList
	 */
	public Query(List<String> strList) {

		if (!patternInitialized) {
			initPattern();
		}

		for (String line : strList) {
			Matcher matcher = patId.matcher(line);
			if (matcher.find()) {
				this.queryId = matcher.group(1);
				continue;
			}
			matcher = patDocid.matcher(line);
			if (matcher.find()) {
				this.docId = matcher.group(1);
				continue;
			}
			matcher = patName.matcher(line);
			if (matcher.find()) {
				this.name = matcher.group(1);
				continue;
			}
			matcher = patStartOffset.matcher(line);
			if (matcher.find()) {
				this.begin = Integer.parseInt(matcher.group(1));
				continue;
			}
			matcher = patEndOffset.matcher(line);
			if (matcher.find()) {
				this.end = Integer.parseInt(matcher.group(1));
				continue;
			}
			
		}
	}
	
	public boolean isDefinedPos() {
		return this.begin >= 0 && this.end > begin;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query id=\"" + this.queryId + "\">\n");
		sb.append("\t<name>" + this.name + "</name>\n");
		sb.append("\t<docid>" + this.docId + "</docid>\n");
		if (this.isDefinedPos()) {
			sb.append("\t<beg>" + this.begin + "</beg>\n");
			sb.append("\t<end>" + this.end + "</end>\n");
		}
		sb.append("</query>\n");
		return sb.toString();
	}

	/**
	 * Load queries from given file. The file format should be the same with the
	 * sample file. Read the whole content of a query and then parse it.
	 * 
	 * @param file
	 *            the source of queries
	 * @return ArrayList of queries
	 */
	public static List<Query> loadFromFile(String file) {
		List<Query> queryList = new ArrayList<Query>();
		List<String> buffer = new ArrayList<String>();
		MyFileReader reader = new MyFileReader(file);
		String temp = null;
		boolean isPart = false;
		while ((temp = reader.getNextLine()) != null) {
			temp = temp.trim();
			if (isPart) {
				if (temp.contains("</query>")) {
					isPart = false;
					try {
						queryList.add(new Query(buffer));
					} catch (NumberFormatException e) {
						System.out.println("Error! Failed to parse query!");
					}
					
				} else {
					buffer.add(temp);
				}
			} else {
				if (temp.contains("<query")) {
					isPart = true;
					buffer.clear();
					buffer.add(temp);
				}
			}

		}

		return queryList;
	}

}
