<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.example.contract.helloworld.HelloWorldPortType"%>
<%@page import="org.example.contract.helloworld.HelloWorldService"%>

<%
	HelloWorldService service = new HelloWorldService();
	HelloWorldPortType port = service.getHelloWorldPort();

	String serviceResponse = port.helloWorld("John");
%>

<jsp:include page="/head.jsp" />

<div>
	<h1>Result from calling service</h1>
	<span>
		<% out.println(serviceResponse); %>
	</span>
</div>

</body>
</html>
