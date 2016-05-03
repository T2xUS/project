<%@ page import ="java.sql.*" %>
<%
    String user = request.getParameter("uname");    
    String pwd = request.getParameter("pass");
    String newpwd = request.getParameter("new_password");
    String newpwd2 = request.getParameter("new_password2");
	
	Connection con = null;
	try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con = DriverManager.getConnection("jdbc:mysql://199.98.20.118:3306/TestDatabase",
				"TDguest", "TDpass");
		
		Statement st = con.createStatement();
		ResultSet rs;
		rs = st.executeQuery("select * from members where uname='" + user + "' and pass='" + pwd + "'");
		if (rs.next()) {
			if (newpwd == newpwd2){
				st.excuteUpdate("update members set pass='" + newpwd + "' where uname='" + user + "'");
				//session.setAttribute("userid", user);
				//out.println("welcome " + userid);
				//out.println("<a href='logout.jsp'>Log out</a>");
				response.sendRedirect("successChange.jsp");
			}
			else {
				out.println("Invalid new password <a href='changePW.jsp'>try again</a>");
			}
		
		} else {
			out.println("Invalid password <a href='changePW.jsp'>try again</a>");
		}
		rs.close();
		s.close();
	} catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
	} finally{
			try {
				if (con != null)
					con.close();
			} catch (SQLException e){}
	}
%>
