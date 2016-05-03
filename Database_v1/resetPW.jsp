<%@ page import ="java.sql.*" %>
<%
	String fname = request.getParameter("fname");
    String lname = request.getParameter("lname");
	String user = request.getParameter("uname");   
    String email = request.getParameter("email");
	
	Connection con = null;
	try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con = DriverManager.getConnection("jdbc:mysql://199.98.20.118:3306/TestDatabase",
				"TDguest", "TDpass");
		
		Statement st = con.createStatement();
		ResultSet rs;
		rs = st.executeQuery("select * from members where uname='" + user + "' and email='" + email + "' and first_name='" + fname + "' and last_name='" + lname + "' ");
		if (rs.next()) {
			String pw = rs.getString("pass");
			session.setAttribute("userid", user);
			session.setAttribute("pass_retreive", pw);
			//out.println("welcome " + userid);
			//out.println("<a href='logout.jsp'>Log out</a>");
			response.sendRedirect("successRetreive.jsp");
		} else {
			out.println("Invalid Input Infomation <a href='resetPW.jsp'>try again</a>");
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
