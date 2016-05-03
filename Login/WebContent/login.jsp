<%@ page import ="java.sql.*" %>
<%
    String user = request.getParameter("uname");    
    String pwd = request.getParameter("pass");
    
	Connection con = null;
	try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con = DriverManager.getConnection("jdbc:mysql://localhost/TestDatabase",
				"TDguest", "TDpass");
		Statement st = con.createStatement();
		ResultSet rs; 	
		rs = st.executeQuery("select * from members where uname='" + user + "' and pass='" + pwd + "'");
		if (rs.next()) {
			
			int hscore = rs.getInt("score");
			String leveln = rs.getString("level");
			
			session.setAttribute("userid", user);
			session.setAttribute("info", hscore);
			session.setAttribute("levelnum", leveln);
			//out.println("welcome " + userid);
			//out.println("<a href='logout.jsp'>Log out</a>");
			response.sendRedirect("success.jsp");
			
		} else {
			out.println("Invalid password <a href='index.jsp'>try again</a>");
		}
		rs.close();
		st.close();
		con.close();
	} catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
	} finally{
			try {
				if (con != null)
					con.close();
			} catch (SQLException e){}
	}
	
%>