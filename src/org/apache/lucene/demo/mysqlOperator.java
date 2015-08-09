package org.apache.lucene.demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

public class mysqlOperator {
	public mysqlOperator(){//空构造函数
	}
	
	/**************数据库操作的相应功能函数*******************/
	/**************以下各功能函数应用于SearchQuery.java中的public void doGet(,)**********/
	//获取数据表的第一条记录，该条记录包含搜索结果的信息
	//获取搜索结果总条数
	public int getResultNum(Connection conn) throws SQLException{
		int i = 0;
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from sresult limit 1");
			if(rs.next()){
				i = Integer.parseInt(rs.getString(Lucene.Field_Title));
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
		return i;
	}
	
	//获取搜索过程总用时
	public int getTime(Connection conn) throws SQLException{
		int i = 0;
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from sresult limit 1");//只取第一条
			if(rs.next()){
				i = Integer.parseInt(rs.getString(Lucene.Field_Time));
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
		return i;
	}

	/**************以下各功能函数应用于Lucene.java中的public void SearchLucene(,)**********/
	//删除数据表中原有的数据
	public void delTable(Connection conn) throws SQLException{
		PreparedStatement psmt = conn.prepareStatement("delete from sresult");
		psmt.executeUpdate();
	}

	//在数据表中的第一条记录了搜索结果数目TotalHits,分词后的检索内容query,和搜索用时timer
	public void insertPara(Connection conn, int TotalHits, Query query, long timer) throws SQLException{
		//数据库添加语句模板
		String sql = "insert into sresult values (?,?,?,?,?)";
		//四个参数依次为：序号number，网页标题title，内容摘要snippet，网址url，修改时间time
		
		//数据表中的第一条记录了搜索结果数目TotalHits,分词后的检索内容query,和搜索用时timer
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setLong(1, 0);
		ps.setString(2, Integer.toString(TotalHits));
		ps.setString(3, query.toString(Lucene.Field_Content));
		ps.setString(4, "");
		ps.setString(5, Long.toString(timer));
		ps.executeUpdate();
	}
		
	//将搜索结果存入数据库
	public void insertData(Connection conn, ScoreDoc[] scoreDocs, IndexSearcher isearcher, Highlighter highlighter, Analyzer analyzer) 
			throws SQLException, CorruptIndexException, IOException, InvalidTokenOffsetsException{
		xmlReader xmlreader = new xmlReader();
		
		//数据库添加语句模板
		String sql = "insert into sresult values (?,?,?,?,?)";
		//四个参数依次为：序号number，网页标题title，内容摘要snippet，网址url，修改时间time
		
		//数据表中的第一条记录了搜索结果数目TotalHits,分词后的检索内容query,和搜索用时timer
		PreparedStatement ps = conn.prepareStatement(sql);
		
		//将搜索结果存入数据库
		for(int i = 0;i<scoreDocs.length;i++){
			Document targetDoc = isearcher.doc(scoreDocs[i].doc);//读取检索结果
			//格式化检索结果，关键字为红色加粗字体，但若不包含关键字，则返回内容为空
			String formatTitle = highlighter.getBestFragment(analyzer, Lucene.Field_Title, targetDoc.get(Lucene.Field_Title).toString());
			String formatContent = highlighter.getBestFragment(analyzer, Lucene.Field_Content, targetDoc.get(Lucene.Field_Content).toString());
			String shortTitle,shortContent;
			int length = xmlreader.getLength("length");
			//字符串截短，对于非包含检索字符串的域，只显示部分内容
			if(targetDoc.get(Lucene.Field_Title).toString().length()>length){
				shortTitle = targetDoc.get(Lucene.Field_Title).toString().substring(0, length);
			}else{
				shortTitle = targetDoc.get(Lucene.Field_Title).toString();
			}
			if(targetDoc.get(Lucene.Field_Content).toString().length()>length){
				shortContent = targetDoc.get(Lucene.Field_Content).toString().substring(0, length);
			}else{
				shortContent = targetDoc.get(Lucene.Field_Content).toString();
			}
			
			//分别为每一项赋值
			ps.setLong(1, i+1);			
			if(formatTitle != null){
				ps.setString(2, formatTitle);
			}else if(formatTitle == null){
				ps.setString(2, shortTitle);
			}
			if(formatContent != null){
				ps.setString(3, formatContent);
			}else if(formatContent == null){
				ps.setString(3, shortContent);
			}
			ps.setString(4, targetDoc.get(Lucene.Field_URL).toString());
			ps.setString(5, targetDoc.get(Lucene.Field_Time).toString());
			ps.executeUpdate();
		}
	}
}
