package org.apache.lucene.demo;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class xmlReader {
	/***************从xml读取配置的相应功能函数***********/
	public xmlReader(){//空构造函数
	}
	
	//设置配置文件的路径
	public final static String xmlPath = "F:/java project/IDE FOR J2EE/SearchEngineServer/SearchEngine_cfg.xml";//定义SearchEngine_cfg.xml配置文件的路径
	
	/***************以下功能函数应用于Servlet_CrawlerIndex.java中的public void init()**********/
	//从SearchEngine_cfg.xml配置文件中获取爬虫启动时间参数
	public int getDate(String Element){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//建立一个解析器工厂，以利用这个工厂来获得一个具体的解析器对象
			DocumentBuilder builder = factory.newDocumentBuilder();//获得一个工厂对象后，使用它的静态方法newDocumentBuilder()方法可以获得一个DocumentBuilder对象，这个对象代表了具体的DOM解析器
			org.w3c.dom.Document doc = builder.parse(xmlPath);//利用解析器来对XML文档进行解析
			doc.normalize();
			NodeList nodes = doc.getElementsByTagName("configuration");//使用Document对象的getElementsByTagName()方 法可以得到一个NodeList对象，一个Node对象代表了一个XML文档中的一个标签元素
			Element node = (Element) nodes.item(0);//使用NodeList对象的item()方法来得到列表中的每一个Node对象
			Element linkdate = (Element) node.getElementsByTagName("date").item(0);
			String value = linkdate.getElementsByTagName(Element).item(0).getFirstChild().getNodeValue();//使用Node对象的getNodeValue()方法.
			//System.out.println(Element + ": " + value);//测试用，查看从xml读取的内容
			if(Integer.parseInt(value) >= 0){
				return Integer.parseInt(value);
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/***************以下各功能函数应用于Lucene.java中的public void IndexLucene()**********/
	//从SearchEngine_cfg.xml配置文件中获取待索引文件及索引文件的路径
	public String getPath(String S){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//建立一个解析器工厂，以利用这个工厂来获得一个具体的解析器对象
			DocumentBuilder builder = factory.newDocumentBuilder();//获得一个工厂对象后，使用它的静态方法newDocumentBuilder()方法可以获得一个DocumentBuilder对象，这个对象代表了具体的DOM解析器
			org.w3c.dom.Document doc = builder.parse(xmlPath);//利用解析器来对XML文档进行解析
			doc.normalize();
			NodeList nodes = doc.getElementsByTagName("configuration");//使用Document对象的getElementsByTagName()方 法可以得到一个NodeList对象，一个Node对象代表了一个XML文档中的一个标签元素
			Element node = (Element) nodes.item(0);//使用NodeList对象的item()方法来得到列表中的每一个Node对象
			String path = node.getElementsByTagName(S).item(0).getFirstChild().getNodeValue();//使用Node对象的getNodeValue()方法.
			//注意，这里还使用了一个getFirstChild()方法来获得message下面的第一个子Node对象。虽然在message标签下面除了文 本外并没有其它子标签或者属性，但是我们坚持在这里使用getFirseChild()方法，这主要和W3C对DOM的定义有关。
			//System.out.println(indexPath);//测试用，查看从xml读取的内容
			if(path != null){
				return path;//返回结果
			}			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;		
	}
		
	//从SearchEngine_cfg.xml配置文件中获取建立索引时的相关显示项参数
	public boolean getParameter(String P){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//建立一个解析器工厂，以利用这个工厂来获得一个具体的解析器对象
			DocumentBuilder builder = factory.newDocumentBuilder();//获得一个工厂对象后，使用它的静态方法newDocumentBuilder()方法可以获得一个DocumentBuilder对象，这个对象代表了具体的DOM解析器
			org.w3c.dom.Document doc = builder.parse(xmlPath);//利用解析器来对XML文档进行解析
			doc.normalize();
			NodeList nodes = doc.getElementsByTagName("configuration");//使用Document对象的getElementsByTagName()方 法可以得到一个NodeList对象，一个Node对象代表了一个XML文档中的一个标签元素
			Element node = (Element) nodes.item(0);//使用NodeList对象的item()方法来得到列表中的每一个Node对象
			String parameter = node.getElementsByTagName(P).item(0).getFirstChild().getNodeValue();//使用Node对象的getNodeValue()方法.
			//注意，这里还使用了一个getFirstChild()方法来获得message下面的第一个子Node对象。虽然在message标签下面除了文 本外并没有其它子标签或者属性，但是我们坚持在这里使用getFirseChild()方法，这主要和W3C对DOM的定义有关。
			//System.out.println(P + ": " + parameter);//测试用，查看从xml读取的内容
			if(parameter.equals("false")){
				return false;//返回结果
			}else{
				return true;
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return false;		
	}
	
	//从SearchEngine_cfg.xml配置文件中获取待从索引文件中读取的行数
	public int getRows(String S){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//建立一个解析器工厂，以利用这个工厂来获得一个具体的解析器对象
			DocumentBuilder builder = factory.newDocumentBuilder();//获得一个工厂对象后，使用它的静态方法newDocumentBuilder()方法可以获得一个DocumentBuilder对象，这个对象代表了具体的DOM解析器
			org.w3c.dom.Document doc = builder.parse(xmlPath);//利用解析器来对XML文档进行解析
			doc.normalize();
			NodeList nodes = doc.getElementsByTagName("configuration");//使用Document对象的getElementsByTagName()方 法可以得到一个NodeList对象，一个Node对象代表了一个XML文档中的一个标签元素
			Element node = (Element) nodes.item(0);//使用NodeList对象的item()方法来得到列表中的每一个Node对象
			String rows = node.getElementsByTagName(S).item(0).getFirstChild().getNodeValue();//使用Node对象的getNodeValue()方法.
			//注意，这里还使用了一个getFirstChild()方法来获得message下面的第一个子Node对象。虽然在message标签下面除了文 本外并没有其它子标签或者属性，但是我们坚持在这里使用getFirseChild()方法，这主要和W3C对DOM的定义有关。
			//System.out.println("rows from file: " + rows);//测试用，查看从xml读取的内容
			if(rows != null){
				return Integer.parseInt(rows);//返回结果
			}			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return 0;		
	}
	
	//从SearchEngine_cfg.xml配置文件中获取索引性能优化参数
	public int getOptimize(String S){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//建立一个解析器工厂，以利用这个工厂来获得一个具体的解析器对象
			DocumentBuilder builder = factory.newDocumentBuilder();//获得一个工厂对象后，使用它的静态方法newDocumentBuilder()方法可以获得一个DocumentBuilder对象，这个对象代表了具体的DOM解析器
			org.w3c.dom.Document doc = builder.parse(xmlPath);//利用解析器来对XML文档进行解析
			doc.normalize();
			NodeList nodes = doc.getElementsByTagName("configuration");//使用Document对象的getElementsByTagName()方 法可以得到一个NodeList对象，一个Node对象代表了一个XML文档中的一个标签元素
			Element node = (Element) nodes.item(0);//使用NodeList对象的item()方法来得到列表中的每一个Node对象
			Element linkdate = (Element) node.getElementsByTagName("optimize").item(0);
			String value = linkdate.getElementsByTagName(S).item(0).getFirstChild().getNodeValue();//使用Node对象的getNodeValue()方法.
			//System.out.println(S + ": " + value);//测试用，查看从xml读取的内容
			if(Integer.parseInt(value) >= 0){
				return Integer.parseInt(value);
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/***************以下各功能函数应用于Lucene.java中的public void SearchLucene(,)**********/
	//从SearchEngine_cfg.xml配置文件中获取索引文件的路径
	public String getIndexPath(){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//建立一个解析器工厂，以利用这个工厂来获得一个具体的解析器对象
			DocumentBuilder builder = factory.newDocumentBuilder();//获得一个工厂对象后，使用它的静态方法newDocumentBuilder()方法可以获得一个DocumentBuilder对象，这个对象代表了具体的DOM解析器
			org.w3c.dom.Document doc = builder.parse(xmlPath);//利用解析器来对XML文档进行解析
			doc.normalize();
			NodeList nodes = doc.getElementsByTagName("configuration");//使用Document对象的getElementsByTagName()方 法可以得到一个NodeList对象，一个Node对象代表了一个XML文档中的一个标签元素
			Element node = (Element) nodes.item(0);//使用NodeList对象的item()方法来得到列表中的每一个Node对象
			String indexPath = node.getElementsByTagName("index").item(0).getFirstChild().getNodeValue();//使用Node对象的getNodeValue()方法.
			//注意，这里还使用了一个getFirstChild()方法来获得message下面的第一个子Node对象。虽然在message标签下面除了文 本外并没有其它子标签或者属性，但是我们坚持在这里使用getFirseChild()方法，这主要和W3C对DOM的定义有关。
			//System.out.println(indexPath);//测试用，查看从xml读取的内容
			if(indexPath != null){
				return indexPath;//返回结果
			}			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	/***************以下各功能函数应用于Lucene.java中的public void SearchLucene(,)**********/
	/***************以下各功能函数应用于mysqlOperator.java中的public void insertData(,,,,)**********/
	//从SearchEngine_cfg.xml配置文件中获每一搜索结果条目的长度
	public int getLength(String S){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//建立一个解析器工厂，以利用这个工厂来获得一个具体的解析器对象
			DocumentBuilder builder = factory.newDocumentBuilder();//获得一个工厂对象后，使用它的静态方法newDocumentBuilder()方法可以获得一个DocumentBuilder对象，这个对象代表了具体的DOM解析器
			org.w3c.dom.Document doc = builder.parse(xmlPath);//利用解析器来对XML文档进行解析
			doc.normalize();
			NodeList nodes = doc.getElementsByTagName("configuration");//使用Document对象的getElementsByTagName()方 法可以得到一个NodeList对象，一个Node对象代表了一个XML文档中的一个标签元素
			Element node = (Element) nodes.item(0);//使用NodeList对象的item()方法来得到列表中的每一个Node对象
			String value = node.getElementsByTagName(S).item(0).getFirstChild().getNodeValue();//使用Node对象的getNodeValue()方法.
			//System.out.println(S + ": " + value);//测试用，查看从xml读取的内容
			if(Integer.parseInt(value) >= 0){
				return Integer.parseInt(value);
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return 0;
	}	
	
	/***************以下各功能函数应用于SearchQuery.java**********/
	//从SearchEngine_cfg.xml配置文件中获取每页显示最多结果条数MaxRows参数
	public int getMaxRows(){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//建立一个解析器工厂，以利用这个工厂来获得一个具体的解析器对象
			DocumentBuilder builder = factory.newDocumentBuilder();//获得一个工厂对象后，使用它的静态方法newDocumentBuilder()方法可以获得一个DocumentBuilder对象，这个对象代表了具体的DOM解析器
			org.w3c.dom.Document doc = builder.parse(xmlPath);//利用解析器来对XML文档进行解析
			doc.normalize();
			NodeList nodes = doc.getElementsByTagName("configuration");//使用Document对象的getElementsByTagName()方 法可以得到一个NodeList对象，一个Node对象代表了一个XML文档中的一个标签元素
			Element node = (Element) nodes.item(0);//使用NodeList对象的item()方法来得到列表中的每一个Node对象
			String maxrows = node.getElementsByTagName("MaxRows").item(0).getFirstChild().getNodeValue();//使用Node对象的getNodeValue()方法.
			//注意，这里还使用了一个getFirstChild()方法来获得message下面的第一个子Node对象。虽然在message标签下面除了文 本外并没有其它子标签或者属性，但是我们坚持在这里使用getFirseChild()方法，这主要和W3C对DOM的定义有关。
			//System.out.println(maxrows);//测试用，查看从xml读取的内容
			if(Integer.parseInt(maxrows) != 0){
				return Integer.parseInt(maxrows);//转换成int并返回结果
			}			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}		
		return 10;		
	}
	
	/***************以下各功能函数应用于Crawler.java中的public Crawler()**********/
	//从SearchEngine_cfg.xml配置文件中获取爬虫配置文件的路径
	public String getCrawlerPath(String S){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//建立一个解析器工厂，以利用这个工厂来获得一个具体的解析器对象
			DocumentBuilder builder = factory.newDocumentBuilder();//获得一个工厂对象后，使用它的静态方法newDocumentBuilder()方法可以获得一个DocumentBuilder对象，这个对象代表了具体的DOM解析器
			org.w3c.dom.Document doc = builder.parse(xmlPath);//利用解析器来对XML文档进行解析
			doc.normalize();
			NodeList nodes = doc.getElementsByTagName("configuration");//使用Document对象的getElementsByTagName()方 法可以得到一个NodeList对象，一个Node对象代表了一个XML文档中的一个标签元素
			Element node = (Element) nodes.item(0);//使用NodeList对象的item()方法来得到列表中的每一个Node对象
			String path = node.getElementsByTagName(S).item(0).getFirstChild().getNodeValue();//使用Node对象的getNodeValue()方法.
			//注意，这里还使用了一个getFirstChild()方法来获得message下面的第一个子Node对象。虽然在message标签下面除了文 本外并没有其它子标签或者属性，但是我们坚持在这里使用getFirseChild()方法，这主要和W3C对DOM的定义有关。
			//System.out.println(path);//测试用，查看从xml读取的内容
			if(path != null){
				return path;//返回结果
			}			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	/***************以下各功能函数应用于Htmlparser.java中的public Htmlparser()**********/
	//从SearchEngine_cfg.xml配置文件中获取页面分析源文件及保存文件的路径
	public String getParserPath(String S){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//建立一个解析器工厂，以利用这个工厂来获得一个具体的解析器对象
			DocumentBuilder builder = factory.newDocumentBuilder();//获得一个工厂对象后，使用它的静态方法newDocumentBuilder()方法可以获得一个DocumentBuilder对象，这个对象代表了具体的DOM解析器
			org.w3c.dom.Document doc = builder.parse(xmlPath);//利用解析器来对XML文档进行解析
			doc.normalize();
			NodeList nodes = doc.getElementsByTagName("configuration");//使用Document对象的getElementsByTagName()方 法可以得到一个NodeList对象，一个Node对象代表了一个XML文档中的一个标签元素
			Element node = (Element) nodes.item(0);//使用NodeList对象的item()方法来得到列表中的每一个Node对象
			String path = node.getElementsByTagName(S).item(0).getFirstChild().getNodeValue();//使用Node对象的getNodeValue()方法.
			//注意，这里还使用了一个getFirstChild()方法来获得message下面的第一个子Node对象。虽然在message标签下面除了文 本外并没有其它子标签或者属性，但是我们坚持在这里使用getFirseChild()方法，这主要和W3C对DOM的定义有关。
			//System.out.println(path);//测试用，查看从xml读取的内容
			if(path != null){
				return path;//返回结果
			}			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;		
	}
}
