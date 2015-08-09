<%@ page language="java" session="true" 
import="java.util.*,org.apache.lucene.demo.Content" 
import="javax.xml.parsers.DocumentBuilder"
import="javax.xml.parsers.DocumentBuilderFactory"
import="javax.xml.parsers.ParserConfigurationException"
import="org.w3c.dom.NodeList"
import="org.w3c.dom.Element"
import="org.w3c.dom.Document"
import="org.apache.lucene.demo.xmlReader"

contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title><%=request.getParameter("query")%>_朝夕搜索</title>
</head>
<body>

<!-- 利用表单获取用户输入的检索词query，并跳转到SearchQuery网页 -->
<form action="SearchQuery" onsubmit="return validate_form(this)" method="get">
	<!-- 文本输入框query -->
	<input type="text" id="query" name="query"
		style="height:30px;font-size:25px;width:600px"
		value=<%=request.getParameter("query")%>>
	
	<!-- 搜索按钮button -->
	<input id="button" type="submit" value="搜索" 
		style="font-size:18px"
		style="background-color:MediumTurquoise;width:120px;height:35px"/>
</form>

<!-- 验证搜索栏是否为空，若为空值则报错且不继续执行 -->
<script>
function validate_required(field,alerttxt){
	with(field){
		if(value==null||value==""){//检查输入是否为空值
			alert(alerttxt);//若为空值则提示并报错
			return false;
		}else{
			return true;
		}
	}
}
//利用上面的函数检查提交的表单：
function validate_form(thisform){
	with(thisform){
		if(validate_required(query,"Query can not be NULL!")==false){
			query.focus();
			return false;
		}
	}
}
</script>

<%//设置刷新页面的时间，每隔15分钟刷新一次【注：Tomcat中Session的默认超时时间为20分钟】
response.setHeader("refresh", "900");
%>

<%//从服务器获取当页的搜索结果列表
List<Content> slist;
synchronized(session){
	slist = (List<Content>) session.getAttribute("ResultList");
}
%>

<!-- 搜索过程的信息提示，包括：搜索的词条，结果条数，所用时间 -->
<p>搜索 <B><font color='red'><%=request.getParameter("query")%></font></B> 得到约<%=request.getParameter("resultNum") %>条搜索结果，共<%=request.getParameter("pageNum")%>页，用时<%=request.getParameter("time") %>毫秒</p>

<%!String urlNextPage; //定义全局变量urlNextPage
String xmlPath = xmlReader.xmlPath;//获取xml配置文件的路径
%>
<%//显示搜索结果
for(int index = 0; index<slist.size(); index++){
	//out.println("<p>"+slist.get(index).getNumber()+"</p>");//显示序号
	out.println("</br>");//不显示序号，只显示空行
	out.println("<p><a href="+slist.get(index).getUrl()+">"+slist.get(index).getTitle()+"</a></p>");
	out.println("<p>"+slist.get(index).getSnippet()+"</p>");
	out.println("<p>"+slist.get(index).getUrl()+" ");
	out.println(slist.get(index).getTime()+"</p>");
}

//通过java方式获取当前url:
String url = "";

/***************从xml读取配置的相应功能函数***********/
//从SearchEngine_cfg.xml配置文件中获取索引文件的路径
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//建立一个解析器工厂，以利用这个工厂来获得一个具体的解析器对象
DocumentBuilder builder = factory.newDocumentBuilder();//获得一个工厂对象后，使用它的静态方法newDocumentBuilder()方法可以获得一个DocumentBuilder对象，这个对象代表了具体的DOM解析器
Document doc = builder.parse(xmlPath);//利用解析器来对XML文档进行解析
doc.normalize();
NodeList nodes = doc.getElementsByTagName("configuration");//使用Document对象的getElementsByTagName()方 法可以得到一个NodeList对象，一个Node对象代表了一个XML文档中的一个标签元素
Element node = (Element) nodes.item(0);//使用NodeList对象的item()方法来得到列表中的每一个Node对象
String sresultURL = node.getElementsByTagName("sresulturl").item(0).getFirstChild().getNodeValue();//使用Node对象的getNodeValue()方法.
//注意，这里还使用了一个getFirstChild()方法来获得message下面的第一个子Node对象。虽然在message标签下面除了文 本外并没有其它子标签或者属性，但是我们坚持在这里使用getFirseChild()方法，这主要和W3C对DOM的定义有关。
//System.out.println(sresultURL);//测试用，查看从xml读取的内容
url = sresultURL;
/****************xml读取操作结束******************/

//String url=request.getScheme()+"://" + request.getHeader("host") + request.getRequestURI();//通过java获取的当前页面的URL【！获取结果有误,localhost参数不对！】
String _url[] = url.split("[.]");//分割字符串
urlNextPage = _url[0];//初始化“下一页”的URL地址
%>
<!-- 通过一个表格显示翻页功能键，从左到右依次为：“上一页”按钮，当前页码，“下一页”按钮 -->
<table border="0">
<tr>
	<td>
		<%//显示“上一页”的按钮
		if(Integer.parseInt(request.getParameter("page")) > 1){//如果本页为第二页等
			out.println("<button onclick='perviousPage()'>上一页</button>");//“上一页”的按钮
		}%>
	</td>
	<td>
		<%//显示本页的页码信息
		out.println("第 "+request.getParameter("page")+" 页");//显示当前页数
		%>
	</td>
	<td>
		<%//显示“下一页”的按钮
		String pageNum = request.getParameter("pageNum");//获取总页数
		if(Integer.parseInt(request.getParameter("pageNum")) > 1 //如果总页数大于1
				&& Integer.parseInt(request.getParameter("page"))!=Integer.parseInt(request.getParameter("pageNum"))){//且当前页数不是最后一页%> 
		<!-- “下一页”的实现方法，隐藏了文本输入框，其内容为检索词条和下一页的页码，嵌入java代码中 -->
		<form action="SearchQuery" method="get">
			<input type="hidden" id="page" name="page" value="<%=String.valueOf((Integer.parseInt(request.getParameter("page"))+1)) %>"/>
			<input type="hidden" id="query" name="query" value="<%=request.getParameter("query") %>"/>
			<button type="submit">下一页</button>
		</form>
		<%}//if语句结束%>
	</td>
</tr>
</table>

<!-- “上一页”的实现方法  -->
<script type="text/javascript">
function perviousPage(){
	history.go(-1);//后退一页
}
</script>

<!-- 调试用，返回键，返回到主页 
<p><a href="index.jsp">Back</a></p>
-->
</body>
</html>