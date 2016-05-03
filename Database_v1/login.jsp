<%@ page import ="java.sql.*" %>
<%
    String user = request.getParameter("uname");    
    String pwd = request.getParameter("pass");
    
	Connection con = null;
	try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con = DriverManager.getConnection("jdbc:mysql://199.98.20.118:3306/TestDatabase",
				"TDguest", "TDpass");
		Statement st = con.createStatement();
		ResultSet rs; 	
		rs = st.executeQuery("select * from members where uname='" + user + "' and pass='" + pwd + "'");
		if (rs.next()) {
			
			int hscore = rs.getInt("score");
			int leveln = rs.getInt("level");
			
			session.setAttribute("userid", userid);
			session.setAttribute("info", hscore);
			session.setAttribute("levelnum", leveln);
			//out.println("welcome " + userid);
			//out.println("<a href='logout.jsp'>Log out</a>");
			response.sendRedirect("success.jsp");
			
		} else {
			out.println("Invalid password <a href='index.jsp'>try again</a>");
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