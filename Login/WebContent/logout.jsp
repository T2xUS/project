<%
	session.setAttribute("userid", null);
	session.setAttribute("info", null);
	session.setAttribute("levelnum", null);
	session.setAttribute("pass_retreive", null);
	session.invalidate();
	response.sendRedirect("index.jsp");
%>