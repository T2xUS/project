<%@ page import ="java.sql.*" %>
<%
	Connection con = null;
	try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TestDatabase",
				"TDguest", "TDpass");
		Statement st = con.createStatement();
		//int count;
		//create a table and insert data
		st.executeUpdate("DROP TABLE IF EXISTS members");
		st.executeUpdate("CREATE TABLE members (" + "id INT UNSIGNED NOT NULL AUTO_INCREMENT," +"PRIMARY KEY (id)," +"name CHAR (40), category CHAR (40))");
		//count = st.executeUpdate("INSERT INYO animal (name, category)" + " VALUES" + "('snake', 'reptile'," + "('frog', 'amphibian')," + "('tuna', 'fish')," + "('racoon', 'mammal')");
		//System.out.println(count + "rows were inserted");
		
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

<%// CREATE TABLE `members` (
  //`id` int(10) unsigned NOT NULL auto_increment,
  //`first_name` varchar(45) NOT NULL,
  //`last_name` varchar(45) NOT NULL,
  //`email` varchar(45) NOT NULL,
  //`uname` varchar(45) NOT NULL,
  //`pass` varchar(45) NOT NULL,
  //`score` int(10) unsigned NOT NULL,
  //`level` int(10) unsigned NOT NULL,
  //`regdate` date NOT NULL,
  // PRIMARY KEY  (`id`)
// ENGINE=InnoDB DEFAULT CHARSET=latin1;
 %>