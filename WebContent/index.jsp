<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="./css/common.css" />
	<!-- 每隔1000秒刷新网页【注：Tomcat中Session的默认超时时间为20分钟】 -->
	<meta http-equiv="refresh" content="1000" />
	<title>一万年太久 只争朝夕</title>
</head>
	
<body>
	<!-- 页眉 -->
	<iframe src="./pages/pageHeader.html" width="100%" height="40px" frameborder="no">页眉</iframe>
	
	<!-- 所有元素居中对齐 -->
	<div align="center" style="margin-top: 100px">
		<!-- 标题栏 -->
		<a href="./pages/introduction.html">
			<img src="./picture/brand.png"/>
		</a>
		<br/><br/><br/>

		<p style="text-align: left; width: 400px; padding-bottom: 0; margin-bottom: 2px; font-weight: bold">网页  </p>

		<!-- 利用表单获取用户输入的检索词query，并跳转到SearchQuery网页 -->
		<form action="SearchQuery" onsubmit="return validate_form(this)" method="get">
			<!-- 文本输入框query -->
			<input type="text" id="query" name="query"
				style="height:30px;font-size:25px;width:400px"/>
			
			<!-- 搜索按钮button -->
			<input id="button" type="submit" value="只争朝夕" 
				style="font-size:18px"
				style="background-color:MediumTurquoise;width:120px;height:35px"/>
		</form>

		<p style="margin-top: 300px; font-size: 15px; font-family: 楷体; ">
		    <br/>北京邮电大学
		    <br/>可信分布式计算与服务教育部重点实验室
		    <br/>作者：张吉伟、胡正刚、李晶、沈科伟
		    <br/>指导教师：陆月明 
		    <br/><span id="time">时间</span>
		</p>            
	</div>	
</body>
<script>
    t = document.getElementById("time");
    showTime();
    function showTime(){
      var d = new Date();
      t.innerHTML=d.toLocaleString();
      setTimeout("showTime()", 200);       
    }
    </script>
	
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
			if(validate_required(query,"请输入搜索内容")==false){
				query.focus();
				return false;
			}
		}
	}
</script>
</html>