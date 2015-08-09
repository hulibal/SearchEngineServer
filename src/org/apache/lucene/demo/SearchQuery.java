package org.apache.lucene.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

//功能函数的类
import org.apache.lucene.demo.xmlReader;

public class SearchQuery extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	Connection conn;//连接服务器的会话对象
	
	xmlReader xmlreader = new xmlReader();//用于SearchEngine_cfg.xml配置文件中获取参数的类
	int MaxRows = xmlreader.getMaxRows();//从SearchEngine_cfg.xml配置文件中获取MaxRows参数
	
	public void init() throws ServletException{
		super.init();
		try{
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/searchresult";//数据库名称:searchresult，数据表名称sresult
			conn = DriverManager.getConnection(url,"root","root");//登录数据库
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		doGet(request,response);
	}
	
	/**************使用doGet方法获取搜索结果***************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		//通用参数设置
		response.setContentType("text/html");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession(false);
		
		//网页中的参数设置
		String url="/SearchResult.jsp";//呈现搜索结果的页面
		String query = request.getParameter("query");//从上一张网页获取提交的参数query
		String page = request.getParameter("page");//提取html请求中的页数参数
		url = url + "?query=" + query;//通过在页面url后面追加问号(?)来传递参数
		
		int resultNum = 0;//搜索结果总条数
		int pageNum = 0;//搜索结果总页数
		int time = 0;//搜索总用时
		
		if(page == null||page == "1"){//如果不是由“下一页”过来，则需要查询搜索索引，并将搜索结果存入数据库中
			try {
				Lucene lucene = new Lucene();
				lucene.SearchLucene(query, conn);//使用lucene搜索query，并将结果存入数据库中		
				mysqlOperator mysqloperator = new mysqlOperator();//用于操作数据库的类
				resultNum = mysqloperator.getResultNum(conn);//获取搜索结果的总条数
				if(resultNum % MaxRows == 0){//如果总条数能被每页条数整除，
					pageNum = resultNum/MaxRows;//则总页数为resultNum/MaxRows
				}else{
					pageNum = resultNum/MaxRows + 1;//否则总页数resultNum/MaxRows外加1页
				}
				
				time = mysqloperator.getTime(conn);
				int i = 0;//每页显示条目的计数器
				//Content类型的定义在文件Content.java中，变量有：number,title,snippet,url,time，包含setter和getter方法
				List<Content> slist = new ArrayList<Content>();//该list用来存储搜索结果，并通过session提交给jsp页面
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select * from sresult where number between 1 and 10");//第一页为前10条搜索结果
				while(rs.next() && i<MaxRows){//将至多MaxRows条结果填入list
					slist.add(new Content(
							rs.getInt("number"),
							rs.getString("title"),
							rs.getString("snippet"),
							rs.getString("url"),
							rs.getString("time")));
					i++;//计数器自增1
				}
				//关闭相关结果集
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				
				//存入session中，通过对话机制(session)将列表slist传递给jsp页面
				synchronized(session){
					session.setAttribute("ResultList", slist);
				}
												
				//通过在页面url后面追加问号(?)来传递参数，如需传递多个参数，则需在各个参数之间添加与(&)符号；但这些参数不会在地址栏里显示出来
				url=url+"&page=1&pageNum="+pageNum+"&resultNum="+resultNum+"&time="+time;//搜索结果页面的url	
				//跳转到显示搜索结果的页面
				ServletContext sc = getServletContext();
				RequestDispatcher rd = sc.getRequestDispatcher(url);//跳转到指定的页面
				rd.forward(request, response);//执行跳转
				
			} catch (ParseException|SQLException|InvalidTokenOffsetsException e) {
				e.printStackTrace();
			}			
		}else{//以下处理由“第二页”等过来的搜索结果
			try {
				//不重新检索，而是直接从数据库获取搜索结果的信息
				mysqlOperator mysqloperator = new mysqlOperator();
				resultNum = mysqloperator.getResultNum(conn);//获取搜索结果的总条数
				pageNum = resultNum/MaxRows+1;//总页数，每页显示条数至多为MaxRows条
				time = mysqloperator.getTime(conn);
				
				//设置本页检索结果的范围
				int start = (Integer.parseInt(page)-1)*MaxRows+1;//本页搜索结果条目开始的序号
				int end = 0;//本页搜索结果条目结束的序号
				if(start+MaxRows < resultNum){//如果本页的条数足够MaxRows条，则结束条目的序号为起始条目序号加10
					end = start + MaxRows;
				}
				else{//如果本页的条数不够MaxRows条，则结束条目的序号为总条数
					end = resultNum;
				}
				
				int i = 0;//每页显示条目的计数器
				//Content类型的定义在文件Content.java中，变量有：number,title,snippet,url,time，包含setter和getter方法
				List<Content> slist = new ArrayList<Content>();//该list用来存储搜索结果，并通过session提交给jsp页面
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select * from sresult where number between "+ start+" and " + end);
				while(rs.next() && i<MaxRows){//将至多MaxRows条结果填入list
					slist.add(new Content(
							rs.getInt("number"),
							rs.getString("title"),
							rs.getString("snippet"),
							rs.getString("url"),
							rs.getString("time")));
					i++;//计数器自增1
				}
				//关闭相关结果集
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				
				//存入session中，通过对话机制(session)将列表slist传递给jsp页面
				synchronized(session){
					session.setAttribute("ResultList", slist);
				}
				
				//通过在页面url后面追加问号(?)来传递参数，如需传递多个参数，则需在各个参数之间添加与(&)符号；但这些参数不会在地址栏里显示出来
				url=url+"&page="+page+"&pageNum="+pageNum+"&resultNum="+resultNum+"&time="+time;//搜索结果页面的url	
				//跳转到显示搜索结果的页面
				ServletContext sc = getServletContext();
				RequestDispatcher rd = sc.getRequestDispatcher(url);//跳转到指定的页面
				rd.forward(request, response);//执行跳转
				
			} catch (SQLException e) {
				e.printStackTrace();
			}//获取搜索结果的总条数
		}
	}
		
	public void destroy(){
		super.destroy();
	}
	
}
