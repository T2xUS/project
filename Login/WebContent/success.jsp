<%
    if ((session.getAttribute("userid") == null) || (session.getAttribute("userid") == "")) {
%>
You are not logged in<br/>
<a href="index.jsp">Please Login</a>
<%} else {
%>
Welcome, <%=session.getAttribute("userid")%>
<br/>
<a href='account.jsp'>View Your Account Summary</a>
<br/>
<a href='changePW.jsp'>Change Your Password</a>
<br/>
<a href='logout.jsp'>Log out</a>
<%
    }
%>