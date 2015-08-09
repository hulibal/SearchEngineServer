package org.apache.lucene.demo;

/**************服务器与网页的Session会话期间所使用的数据格式************/
/**************以下各功能函数应用于SearchQuery.java中的public void doGet(,)**********/
public class Content {
	private int number;
	private String title;
	private String snippet;
	private String url;
	private String time; 
	//构造函数
	public Content(int number, String title, String snippet, String url, String time){
		this.number = number;
		this.title = title;
		this.snippet = snippet;
		this.url = url;
		this.time = time;
	}
	//JavaBean转换器是通过setter和getter来访问Java对象的属性
	//每个属性需要有对应的set和get函数
	//Number:
	public int getNumber(){
		return number;
	}
	public void setNumber(int number){
		this.number = number;
	}
	//Title:
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	//Snippet
	public String getSnippet(){
		return snippet;
	}
	public void setSnippet(String snippet){
		this.snippet = snippet;
	}
	//Url
	public String getUrl(){
		return url;
	}
	public void setUrl(String url){
		this.url = url;
	}
	//Time
	public String getTime(){
		return time;
	}
	public void setTime(String time){
		this.time = time;
	}
}
