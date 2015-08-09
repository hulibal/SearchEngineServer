package org.apache.lucene.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Lucene {
	/*************使用Lucene建立索引*****************/
	//定义需要索引的域
	static String Field_Title = "title";//网页的标题
	static String Field_URL = "url";//网页的url地址
	static String Field_Time = "time";//网页的创建/获取时间
	static String Field_Content = "contents"; //网页的内容
	
	public Lucene(){//空构造函数
	}
	
	/**************以下各功能函数应用于MyTask.java中的public MyTask()及public void run()**********/
	public void IndexLucene(String isFirstRun){
		xmlReader xmlreader = new xmlReader();//用于SearchEngine_cfg.xml配置文件中获取参数的类
		String indexPath = xmlreader.getPath("index");//从SearchEngine_cfg.xml配置文件中获取索引文件的路径
		String sourcePath = xmlreader.getPath("source");//从SearchEngine_cfg.xml配置文件中获取待索引文件的路径
		String updatePath = xmlreader.getPath("updatesource");//从SearchEngine_cfg.xml配置文件中获取更新的待索引文件的路径
		int rows_to_read = xmlreader.getRows("rows_to_read");//从SearchEngine_cfg.xml配置文件中获取从待索引文件中读取的行数
		File docDir = null;
		
		//create参数：如果为true则新建索引，如果为false则更新现有索引		
		boolean create = false;
		if(isFirstRun == "firstRun"){//如果是firstRun(第一次运行)，则create参数设置为true
			create = true;
		}
		boolean showProcess = xmlreader.getParameter("showProcess");//从SearchEngine_cfg.xml配置文件中读取是否在建立索引的过程中显示索引建立的进度细节
		
		//实例化IKAnalyzer分词器
		Analyzer analyzer = null;
		Directory directory = null;
		IndexWriter iwriter = null;
		
		//设置建立索引的性能参数
		int mergeFactor = xmlreader.getOptimize("mergeFactor");//控制多个segment合并的频率，值较大时建立索引速度较快，默认是10，可以在建立索引时设置为100。
		//int minMergeDocs = 10;//最小合并文档数，决定了内存中的文档数至少达到多少才能将它们写回磁盘。这个参数的默认值是10，如果你有足够的内存，那么将这个值尽量设的比较大一些将会显著的提高索引性能。
		int maxMergeDocs = Integer.MAX_VALUE;//控制一个segment中可以保存的最大document数目，值较小有利于追加索引的速度，默认Integer.MAX_VALUE，无需修改。
		int maxBufferedDocs = xmlreader.getOptimize("maxBufferedDocs");//控制写入一个新的segment前内存中保存的document的数目，设置较大的数目可以加快建索引速度，默认为10。
		double RAMBufferSizeMB = xmlreader.getOptimize("RAMBufferSizeMB");//控制RAM缓存的大小，如果需要索引大量文件，则需增加RAM缓存，同时需要调整Java虚拟机的max heap参数(例如： -Xmx512m or -Xmx1g)
		
		LogMergePolicy mergePolicy = new LogByteSizeMergePolicy();
		mergePolicy.setMergeFactor(mergeFactor);
		mergePolicy.setMaxMergeDocs(maxMergeDocs);
		
		try {
			Date start = new Date(); //Timer start
			System.out.println("Reading files from directory: '" + sourcePath );
			System.out.println("Indexing to directory: '" + indexPath + "'...");
			
			analyzer = new IKAnalyzer();
			directory = FSDirectory.open(new File(indexPath));
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, analyzer);
			FileReader filereader = new FileReader();//用于读取文件的类
			
			if (create) {
				// Create a new index in the directory, removing any previously indexed documents:
				iwc.setOpenMode(OpenMode.CREATE);
				filereader.delAllFile(indexPath);//如果是新建索引，则先删除原有文件夹中的内容
				docDir = new File(sourcePath);//将路径设置为源文档文件夹的位置
		    } else {
		    	docDir = new File(updatePath);//将路径设置为待更新文档文件夹的位置
		    	// Add new documents to an existing index:
		    	iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		    }
			
			iwc.setMaxBufferedDocs(maxBufferedDocs);
		    iwc.setMergePolicy(mergePolicy);//应用索引优化策略
			// Optional: for better indexing performance, if you
			// are indexing many documents, increase the RAM
			// buffer.  But if you do this, increase the max heap
			// size to the JVM (eg add -Xmx512m or -Xmx1g):
			//
			iwc.setRAMBufferSizeMB(RAMBufferSizeMB);
			
		    iwriter = new IndexWriter(directory, iwc);
		    
		    //iwriter.deleteAll();
		    //indexing all text files under a directory
		    indexDocs(iwriter, docDir, rows_to_read, showProcess, analyzer); 
			
			// NOTE: if you want to maximize search performance,
			// you can optionally call forceMerge here.  This can be
			// a terribly costly operation, so generally it's only
			// worth it when your index is relatively static (ie
			// you're done adding documents to it):
			//
			// iwriter.forceMerge(1);
			
			iwriter.close();
			
			Date end = new Date(); //Timer stop
			
			System.out.println("Indexing process Completed!");
			System.out.println(end.getTime() - start.getTime() + " total milliseconds");			
		}catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {					
			e.printStackTrace();					
		} catch (IOException e)	{
			e.printStackTrace();
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		} finally{
			if(directory != null){
				try {
					directory.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//读取待索引文件并将其插入索引
	static void indexDocs(IndexWriter iwriter, File file, int rows_to_read, boolean showProcess, Analyzer analyzer)throws IOException{
		//get the list of .txt files in the directory
		if (file.canRead()) {
			if (file.isDirectory()) {
				String[] files = file.list();
				// an IO error could occur
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						indexDocs(iwriter, new File(file, files[i]), rows_to_read, showProcess, analyzer);
					}
				}
	      	} else {
	      		FileInputStream fis;
	      		try {
	      			fis = new FileInputStream(file);
	      		} catch (FileNotFoundException fnfe) {
	      			// at least on windows, some temporary files raise this exception with an "access denied" message
	      			// checking if the file can be read doesn't help
	      			return;
	      		}
	        
	      		try {
					// make a new, empty document
					Document doc = new Document();
					
					/******读取源文件中的信息******/
					//判断文档文件的编码格式
					String code = "";
					FileReader filereader = new FileReader();//用于读取文件的类
					code = filereader.Encoding(fis);//获取文档编码格式
					if(code == ""){//如果是gb2312的格式，则特殊处理（新读取文件）
						fis.close(); 
						fis = new FileInputStream(file);
						code = "UTF-8";//此处对应UTF-8无BOM编码格式
					}
					
					/**********读取待索引的文本文件正文****************/
					InputStreamReader inputReader = new InputStreamReader(fis,code);
					BufferedReader bufferReader = new BufferedReader(inputReader); 
					
					//需要读取的内容
					String Title = null;//网页的标题，即文本文件的文件名
					String URL = null;//网页的URL，即文本文件的第一行
					String Time = null;//网页的时间，即文本文件的第二行
					String Content = null;//网页的正文，即文本文件第三行即之后的内容
					
					//开始读取文本
					String line = null;//从文本中读取的内容，第一行及第二行内容的缓存					
					StringBuffer strBuffer = new StringBuffer();//第三行及之后内容的缓存
					
					//读取文本文件的文件名，赋值给网页的标题
					Title = file.getName();
					//逐行读取文本内容：
					if((line = bufferReader.readLine()) != null){//读取第一行，赋值给URL
						URL = line;
					}
					
					if((line = bufferReader.readLine()) != null){//读取第二行，赋值给Time
						Time = line;
					}
					/*
					while((line = bufferReader.readLine()) != null){//读取第三行及以后的内容
						strBuffer.append(line);//采用追加的方式将文本内容赋值给字符串
					}
					*/
					for(int i=0; i<rows_to_read; i++){
						line = bufferReader.readLine();
						if(line != null){
							strBuffer.append(line);
						}
					}
					Content = strBuffer.toString();//将读取出来的内容赋值给Content
					//System.out.println("url: " + URL);
					//System.out.println("time: " + Time);
					//System.out.println("title: " + Title);
					//System.out.println("content: " + Content);
					bufferReader.close();
					/*******读取信息结束***********/
					
					/**********读取待索引的文本文件正文***************
					//需要读取的内容
					String Title = FileReader.readTitle(file, fis, code);//网页的标题，即文本文件的文件名
					String URL = FileReader.readURL(fis, code);//网页的URL，即文本文件的第一行
					String Time = FileReader.readTime(fis, code);//网页的时间，即文本文件的第二行
					String Content = FileReader.readContent(fis, code);//网页的正文，即文本文件第三行即之后的内容
					*/
					/**********将读取的信息添加到索引中*************/
					//保存并分词的域：网页标题title、网页正文content
					//保存但不分词的域：网页地址url、网页时间time
					doc.add(new Field(Lucene.Field_Title, Title, Field.Store.YES, Field.Index.ANALYZED));
					doc.add(new Field(Lucene.Field_URL, URL, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
					doc.add(new Field(Lucene.Field_Time, Time, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
					doc.add(new Field(Lucene.Field_Content, Content, Field.Store.YES, Field.Index.ANALYZED));
										
					if (iwriter.getConfig().getOpenMode() == OpenMode.CREATE) {
						// New index, so we just add the document (no old document can be there):
						if(showProcess){
							System.out.println("adding file");
							System.out.println("Title	:" + doc.get(Lucene.Field_Title));
							System.out.println("URL	: " + doc.get(Lucene.Field_URL));
							System.out.println("Time	: " + doc.get(Lucene.Field_Time));
							System.out.println("Content	: " + doc.get(Lucene.Field_Content));
						}
						iwriter.addDocument(doc);
					} else {
						// Existing index (an old copy of this document may have been indexed) so 
					  	// we use updateDocument instead to replace the old one matching the exact 
						// path, if present:
						if(showProcess){
							System.out.println("updating file");
							System.out.println("Title	:" + doc.get(Lucene.Field_Title));
							System.out.println("URL	: " + doc.get(Lucene.Field_URL));
							System.out.println("Time	: " + doc.get(Lucene.Field_Time));
							System.out.println("Content	: " + doc.get(Lucene.Field_Content));
						}
						iwriter.updateDocument(new Term(Lucene.Field_Title, doc.get(Lucene.Field_Title)), doc, analyzer);
					}	
				} finally {
					fis.close();
				}
	      	}
		}
	}
	/******************建立索引结束*****************/		

	/*************使用Lucene进行检索*****************/
	/**************以下各功能函数应用于SearchQuery.java中的public void doGet(,)**********/
	//使用lucene搜索query，并将结果存入数据库中：
	public void SearchLucene(String querystring, Connection conn) 
			throws CorruptIndexException, IOException, ParseException, SQLException, InvalidTokenOffsetsException{
		/*********使用Lucene搜索*****************/
		//参数设置
		xmlReader xmlreader = new xmlReader();//用于SearchEngine_cfg.xml配置文件中获取参数的类
		String index = xmlreader.getIndexPath();//从SearchEngine_cfg.xml配置文件中获取索引文件的路径
		String queryString = querystring;
		int hitsPerPage = 10;
		
		//实例化搜索器
		Analyzer analyzer = new IKAnalyzer();
		IndexSearcher isearcher = null;
		IndexReader reader = IndexReader.open(FSDirectory.open(new File(index)));
		isearcher = new IndexSearcher(reader);
		
		while(true){
			String line = queryString;
			if(line == null || line.length() == -1){
				break;
			}
			line = line.trim();
			if(line.length() == 0){
				break;
			}
		
			//使用QueryParser查询分析器构造Query对象
			//QueryParser qp = new QueryParser(Version.LUCENE_36,fieldName,analyzer);
			//qp.setDefaultOperator(QueryParser.AND_OPERATOR);
			//Query query = qp.parse(line);
			
			//在多个域中查找,而不一定关心在那个字段中包含需要查找的值
			MultiFieldQueryParser multiqueryparser = new MultiFieldQueryParser(Version.LUCENE_36,new String[]{Lucene.Field_Title,Lucene.Field_Content},analyzer);
			multiqueryparser.setDefaultOperator(QueryParser.Operator.AND);//设置以空格分开的短语是并的关系（默认是或的关系）
			Query query = multiqueryparser.parse(line);
			
			//开始搜索索引
			//计时开始
			Date timerStart = new Date();
			
			//检索结果
			TopDocs topDocs = isearcher.search(query, 10*hitsPerPage);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			int TotalHits = topDocs.totalHits;
			
			//计时结束
			Date timerEnd = new Date();
			long timer = timerEnd.getTime()-timerStart.getTime();
			
			//设置高亮显示格式
			int strlength = xmlreader.getLength("length");
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<B><font color='red'>","</font></B>");//设定高亮显示的格式，也就是对高亮显示的词组加上前缀后缀  
			Highlighter highlighter = new Highlighter(simpleHTMLFormatter,new QueryScorer(query));
			highlighter.setTextFragmenter(new SimpleFragmenter(strlength));//设置每次返回的字符数.这里是设定只展示部分数据
					
			/*****数据库操作*****/
			mysqlOperator mysqloperator = new mysqlOperator();//用于操作数据库的类
			mysqloperator.delTable(conn);//删除数据表中原有的数据
			mysqloperator.insertPara(conn, TotalHits, query, timer);//数据表中的第一条记录了搜索结果数目TotalHits,分词后的检索内容query,和搜索用时timer
			mysqloperator.insertData(conn, scoreDocs, isearcher, highlighter, analyzer);//将搜索结果存入数据库
			
			break;//退出while
		}
		isearcher.close();
		reader.close();
		/*********搜索过程结束*******************/
	}

}
