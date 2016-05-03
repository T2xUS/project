<%@ page import ="java.sql.*" %>
<%
    String user = request.getParameter("uname");    
    String pwd = request.getParameter("pass");
    String fname = request.getParameter("fname");
    String lname = request.getParameter("lname");
    String email = request.getParameter("email");
    
	if ((user!= null && !user.isEmpty()) || (pwd != null && !pwd.isEmpty()) || (fname != null && !fname.isEmpty()) || (lname != null && !lname.isEmpty())) {
		out.println("Invalid Inputs <a href='reg.jsp'>try again</a>");
	} else {
		Connection con = null;
		//if (!con.isClosed()){
			//System.out.println("Successfully connceted to MySQL Server...");
		//}
			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TestDatabase",
						"TDguest", "TDpass");
				
				//if (!con.isClosed())
					//System.out.println("Successfully connceted to MySQL Server...");
			
				Statement st = con.createStatement();
				//ResultSet rs;
				ResultSet rs;
				rs = st.executeQuery("select * from members where uname='" + user + "' or email='" + email + "'");
				if (rs.next()){
					response.sendRedirect("regfail.jsp");
					//out.print("Registration UNSuccessfull!"+"<a href='index.jsp'>Go to Login</a>");
				} else {
				int i = st.executeUpdate("insert into members(first_name, last_name, email, uname, pass, regdate) values ('" + fname + "','" + lname + "','" + email + "','" + user + "','" + pwd + "', CURDATE())");
				//out.println("Invalid Inputs <a href='index.jsp'>try again</a>");
				if (i > 0) {
					//session.setAttribute("userid", user);
					response.sendRedirect("welcome.jsp");
				   // out.print("Registration Successfull!"+"<a href='index.jsp'>Go to Login</a>");
				} else {
					response.sendRedirect("index.jsp");
				}}
				rs.close();
				st.close();
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