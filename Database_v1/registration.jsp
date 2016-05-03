<%@ page import ="java.sql.*" %>
<%
    String user = request.getParameter("uname");    
    String pwd = request.getParameter("pass");
    String fname = request.getParameter("fname");
    String lname = request.getParameter("lname");
    String email = request.getParameter("email");
    
	if ((user == null) || (pwd == null) || (fname == null) || (lname == null) || (email == null) || (user == "") || (pwd == "") || (fname == "") || (lname == "") || (email == "")) {
		out.println("Invalid Inputs <a href='index.jsp'>try again</a>");
	} else {
		Connection con = null;
			try {
	
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				con = DriverManager.getConnection("jdbc:mysql://199.98.20.118:3306/TestDatabase",
						"TDguest", "TDpass");
				Statement st = con.createStatement();
				//ResultSet rs;
				int i = st.executeUpdate("insert into members(first_name, last_name, email, uname, pass, regdate) values ('" + fname + "','" + lname + "','" + email + "','" + user + "','" + pwd + "', CURDATE())");
				if (i > 0) {
					//session.setAttribute("userid", user);
					response.sendRedirect("welcome.jsp");
				   // out.print("Registration Successfull!"+"<a href='index.jsp'>Go to Login</a>");
				} else {
					response.sendRedirect("index.jsp");
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
	 }
%>