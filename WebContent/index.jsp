<%@ page language="java" import="java.util.*,com.webcloud.entity.*" isELIgnored="false" pageEncoding="utf-8"%>
<style type="text/css">
ul{padding: 0px;margin: 0px; }
li{float: left; list-style: none;}
#prodCateDiv{height: 50px;}
#prodCateDiv li{width: 100px;height: 30px;line-height: 30px;border: solid 1px red;text-align: center;}
#prodCateDiv a{text-decoration: none;color: red;}
#prodCateDiv a:HOVER {
	text-decoration: none;color: blue;
}
</style>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">  
    <!-- 设置路径 -->
    
<title>Insert title here</title>
</head>

<body>
<div id = bar>
<a href="upload.jsp">上传</a>
</div>
<div id=search>
<form action="main/search.do" method="post">
文件名：<input type="text" name="filename" />
<input type ="submit" value="搜索" />
</form>
</div>
<div id="file">
<table>
<tr>
<td>文件名</td>
<td>文件大小</td>
<td>上传时间</td>
</tr>
<%
List<Fileforusr> fileforusrlist=(List<Fileforusr>)request.getAttribute("fileforusrlist");
for(int i=0;i<fileforusrlist.size();i++){
	Fileforusr fileforusr = fileforusrlist.get(i) ;
%> 
<tr>
<td><a href="wenjianyulanjiemian"><%=fileforusr.getUfilename() %></a></td>
<td><%=fileforusr.getUsize() %></td>
<td><%=fileforusr.getUploadtime() %></td>
<td><a href="share/create.do?key=<%=fileforusr.getUfileid() %>">分享</a></td>
<td><a href="file/download.do?key=<%=fileforusr.getUfileid() %>">下载</a></td>
<td><a href="main/del.do?key=<%=fileforusr.getUfileid() %>">删除</a></td>
</tr>
<%} %>
</table>
</div>

<div id="bar">
<ul>
<li>
<a href = "main/init.do">我的文件</a>
<a href = "share/init.do">我的分享</a>
<a href = "group/init.do">我的群组</a>
<a href = "main/recycle.do">回收站</a>
</li>
</ul>
</div>
<br/>
<div>
<ul>
<li>
<a href = "user/logout.do">安全退出</a>
<a href = "pwd.jsp">修改密码</a>
</li>
</ul>
</div>
</body>
</html>