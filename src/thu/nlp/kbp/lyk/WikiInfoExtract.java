package thu.nlp.kbp.lyk;

import java.util.*;

import thu.nlp.kbp.utils.MyFileReader;
import thu.nlp.kbp.utils.MyFileWriter;



public class WikiInfoExtract {
	static String dir="/home/lyk423";
	HashMap<String,Integer> title2id;
	HashMap<Integer,String> id2title;
	HashMap<Integer, Integer> id2type;
	HashMap<String, Set<String> > disambiguation;
	HashMap<Integer,Set<Integer> > id2category;
	HashMap<Integer,Set<Integer> > id2context;
	HashMap<String,Integer> category;
	HashMap<String,Integer> context;
	int categorynum;
	int contextnum;
	private void IndexParser(String filename) 
	{
		MyFileReader reader = new MyFileReader(filename);
		String str = reader.getNextLine();
		while (str!=null)
		{
			String[] aList =  str.split("\t");
			if (title2id.containsKey(aList[5]))
				System.out.println("title 重复");
			int id = Integer.parseInt(aList[0]);
			String title=aList[5];
			title2id.put(title,id );
			id2title.put(id,title);
			Set<String> set  = disambiguation.get(title);
			if  (set==null)
				set = new HashSet<String>();
			disambiguation.put(title, set);
			if ( Integer.parseInt(aList[1])==1)//type 1 means isDisam
				id2type.put(id,1);
			else
				if (Integer.parseInt(aList[4])==1)//type 2 means isList
					id2type.put(id, 2);
				else
				if (Integer.parseInt(aList[2])==1)//type 3 means a redirect page
					id2type.put(id, 3);
				else//type 0 means entity
					id2type.put(id, 0);
			str = reader.getNextLine();
		}
		reader.close();
	}
	private void CategoryParser(String filename)
	{
		MyFileReader reader = new MyFileReader(filename);
		String str = reader.getNextLine();
		while (str!=null)
		{
			String[] aList =  str.split("\t");
			int id = Integer.parseInt(aList[0]);
			if (id2type.get(id)==0)
			{
				Set<Integer> set = new HashSet<Integer>();
				for (int i=1; i<aList.length; i++)
				{
					int categoryid;
					if (!category.containsKey(aList[i]))
					{
						category.put(aList[i], categorynum);
						categorynum++;
					}
					categoryid=category.get(aList[i]);
					set.add(categoryid);
				}
				id2category.put(id, set);
			}
			str = reader.getNextLine();
		}
		reader.close();
	}
	private void LinkParser(String filename)
	{
		MyFileReader reader = new MyFileReader(filename);
		String str = reader.getNextLine();
		while (str!=null)
		{
			String[] aList =  str.split("\t");
			int id = Integer.parseInt(aList[0]);
			if (id2type.get(id)==0)
			{
				Set<Integer> set = new HashSet<Integer>();
				for (int i=1; i<aList.length; i++)
				{
					String[] bList = aList[i].split("\\|");
					for (int j=0; j<bList.length; j++)
					{
						int contextid;
						if (!context.containsKey(bList[j]))
						{
							context.put(bList[j],contextnum);
							contextnum++;
						}
						contextid=context.get(bList[j]);
						set.add(contextid);
						
					}
				}
				id2context.put(id, set);
			}
			else
			{
				for (int i=1; i<aList.length; i++)
				{
					String[] bList = aList[i].split("\\|");
					if (id2type.get(id)==1||id2type.get(id)==3)
					{
						String str1 = id2title.get(id);
						Set<String> set = disambiguation.get(str1);
						if (set==null)
							set = new HashSet<String>();
						for (int j=0; j<bList.length; j++)
							set.add(bList[j]);
						disambiguation.put(str1, set);
					}
					if (title2id.containsKey(bList[0]))
					{
						int linkid=title2id.get(bList[0]);
						if (id2type.get(linkid)==0)
						{
							if (id2type.get(id)==2)
							{
								Set<Integer> set = id2category.get(linkid);
								int categoryid;
								if (!category.containsKey(bList[0]))
								{
									category.put(bList[0], categorynum);
									categorynum++;
								}
								categoryid=category.get(bList[0]);
								set.add(categoryid);
								id2category.put(linkid, set);
							}
						}
					}
				}
			}
			str = reader.getNextLine();
		}
		reader.close();
	}
	private void Output()
	{
		//输出Disambiguation 信息
		MyFileWriter writer3 = new  MyFileWriter(dir+"/output/Disambiguation.txt", true) ;	
		Iterator iter3 = disambiguation.entrySet().iterator();
		while (iter3.hasNext()) 
		{
			Map.Entry entry = (Map.Entry) iter3.next();
			String  key = (String) entry.getKey();
			Set<String>  val = (Set<String>) entry.getValue();
			 Stack<String> stack = new Stack<String>();  
			 for(Object jie :val) 
			{
				String str=  (String)jie;
				stack.push(str);
			}
			Set<String> val1 = new HashSet<String>();
			while (!stack.empty())
			{
				String str = stack.pop();
				val1.add(str);
				if (!str.matches(".*(disambiguation)"))
					if (disambiguation.containsKey(str+" (disambiguation)"))
					{
						for (String o1: disambiguation.get(str+" (disambiguation)") )
							if (!val1.contains(o1))
								stack.push(o1);
					}
				if (disambiguation.containsKey(str))
				{
					for (String o1: disambiguation.get(str) )
						if (!val1.contains(o1))
							stack.push(o1);
				}
			}
			writer3.write(key);
			for(Object jie :val1) 
			{
				String str=  (String)jie;
				writer3.write("\t"+str);
			}
			writer3.write("\n");
		}
		writer3.close();
		//输出Context 信息
		MyFileWriter writer2 = new  MyFileWriter(dir+"/output/Context.txt", true) ;	
		Iterator iter2 = context.entrySet().iterator();
		while (iter2.hasNext()) 
		{
			Map.Entry entry = (Map.Entry) iter2.next();
			String  key = (String) entry.getKey();
			int  val = (Integer) entry.getValue();
			writer2.write(key+"\t"+val+"\n");
		}
		writer2.close();
		//输出Category 信息
		MyFileWriter writer1 = new  MyFileWriter(dir+"/output/Category.txt", true) ;	
		Iterator iter1 = category.entrySet().iterator();
		while (iter1.hasNext()) 
		{
			Map.Entry entry = (Map.Entry) iter1.next();
			String  key = (String) entry.getKey();
			int  val = (Integer) entry.getValue();
			writer1.write(key+"\t"+val+"\n");
		}
		writer1.close();
		
		Iterator iter = id2category.entrySet().iterator();
		MyFileWriter writer = new  MyFileWriter(dir+"/output/index.feature", true) ;		
		while (iter.hasNext()) 
		{
			Map.Entry entry = (Map.Entry) iter.next();
			int  key = (Integer) entry.getKey();
			writer.write(key+"");
			//输出entity的category feature
			Set  val = (Set) entry.getValue();
			for(Object jie :val) 
			{
				int i =  (Integer)jie;
				writer.write("\t"+i+":"+"1");
			}
			//输出entity的context feature
			Set  val1 = id2context.get(key);
			for(Object jie :val1) 
			{
				int i =  (Integer)jie+categorynum;
				writer.write("\t"+i+":"+"1");
			}
			writer.write("\n");
		}
		writer.close();
	}
	public WikiInfoExtract() {
		title2id = new HashMap<String,Integer>();
		id2title = new HashMap<Integer,String>();
		id2type = new HashMap<Integer,Integer>();
		id2category = new HashMap<Integer,Set<Integer> >();
		id2context = new HashMap<Integer,Set<Integer> >();
		disambiguation = new HashMap<String,Set<String>>();
		category=new HashMap<String,Integer>();
		categorynum=0;
		context=new HashMap<String,Integer>();
		contextnum=0;
	}
	
	public void Run()
	{
		IndexParser(dir+"/input/new_index");
		CategoryParser(dir+"/input/index.txt_cate");
		LinkParser(dir+"/input/index.txt_links");
		Output();
	}
}
