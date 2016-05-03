/*
 *  LoginGUI.java
 *  
 */

package set.client.gui;

import java.net.URL;
import java.sql.*;
import javax.swing.*;

import set.client.GameClient;

import java.awt.*;
import java.awt.event.*;
 
public class LoginGUI extends JFrame  implements ActionListener{
	
	// Client associated with this GUI
	private GameClient client;
	
	private Container CT;
	private JButton btn, btn2;
	private JLabel uname, pwd, errmeg;
	private JTextField t1;
	private JPasswordField t2;
	private JPanel p, p0, p1, p2, p3;
	
	int userID, numWins, totalGames, score, highscore;
	boolean LoggedIn = false;
	boolean InGame = false;
	
	public LoginGUI(GameClient client){
		super("Welcome to SET:The Online Version");
		this.client = client;
		CT=getContentPane();
		//setSize(100,300);
		this.setBounds(450, 300, 400, 160);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new Timer(1000,(ActionListener) this);
		initGUI();	
	}
	
	public void initGUI(){

		t1=new JTextField(10);
		t2=new JPasswordField(10);
		t2.setEchoChar('*');
		
		btn =new JButton("Register");
		btn2 =new JButton("Login");
		btn.addActionListener(this);
		btn2.addActionListener(this);
		btn.setPreferredSize(new Dimension(100, 30));
		btn2.setPreferredSize(new Dimension(100, 30));
		
		uname= new JLabel("Username: ", JLabel.RIGHT);
	    pwd = new JLabel("Password: ", JLabel.RIGHT);
	    //errmeg = new JLabel("Incorrect Username or Password!", JLabel.CENTER);
	    errmeg = new JLabel();
	    
		p=new JPanel();
		p.setLayout(new BorderLayout());
		//CT.add(p);
				
		p0=new JPanel();
		p1=new JPanel();
		p2=new JPanel();
		p3=new JPanel();
		
		p0.add(errmeg);
		//p0.setVisible(false);
		
		p1.add(uname);
		p1.add(t1);
		
		p2.add(pwd);
		p2.add(t2);

		p3.add(btn);
		p3.add(btn2);
		
		p.add(p1, BorderLayout.NORTH);
		p.add(p2, BorderLayout.CENTER);
		p.add(p3, BorderLayout.SOUTH);
		
		CT.add(p,BorderLayout.NORTH);
		CT.add(p0, BorderLayout.SOUTH);
	}
	public void go(){		
		setVisible(true);
		//setVisible(false);
	}
	
	/*
	public static void main(String[] args){
		new LoginGUI("Welcome To Set Online").go();
	}
	*/

	public void actionPerformed(ActionEvent e) {	
		if(e.getSource()==btn){
			try {
				//link to registration website
			    Desktop.getDesktop().browse(new URL("http://199.98.20.121:8080/Login1.3/index.jsp").toURI());
			} catch (Exception e1) {}
		} else if (e.getSource()==btn2){
			
			//implement login function here!
			String username = t1.getText();
			String pwd = t2.getText();
			//System.out.println(pwd);
			
			if(!username.isEmpty() && !pwd.isEmpty()){
				//check connect/status with database
				Connection con = null;
				try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					String url = "jdbc:mysql://199.98.20.121/TestDatabase";
					con = DriverManager.getConnection(url, "TDguest", "TDpass");
					Statement st = con.createStatement();
					ResultSet rs; 	
					
					if (con != null){
						System.out.println("connecting");
					}
					rs = st.executeQuery("select * from members where uname='" + username + "' and pass='" + pwd + "'");
					if (rs.next()) {
				
						//check if logged in already
						int check = rs.getInt("LoggedIn");
						if (check==0){
							
							//get player info from database
							userID = rs.getInt("id");
							numWins = rs.getInt("numWins");
							totalGames = rs.getInt("totalGames");
							score = rs.getInt("score");
							highscore = rs.getInt("highscore");
							
							System.out.println(userID +" " +numWins +" "+ totalGames+" "+score+" "+highscore);
							
							//update login status in database
							st.executeUpdate("update members set LoggedIn='1' where uname='" + username + "'");
							st.executeUpdate("update members set InGame='0' where uname='" + username + "'");
							st.executeUpdate("update members set score='0' where uname='" + username + "'");
							
							//System.out.println(rs.getInt("LoggedIn") +" " +rs.getInt("InGame") +" "+ rs.getInt("score"));
							
							LoggedIn = true;
							InGame = false;
							
							errmeg.setText("Start Loading The Game Lobby...");
							
						}else{
							errmeg.setText("This Account Has Logged In Already");
						}
						
					} else {
						errmeg.setText("Incorrect Username or Password");
					}
					rs.close();
					st.close();
				} catch (Exception e1){
						System.out.println("Exception: " + e1.getMessage());
				} finally{
						try {
							if (con != null)
								con.close();
						} catch (SQLException e1){}
				}
			}else{
				//login failed already
				errmeg.setText("Please Enter Your Username and Password");
			}
			
		}
	}
}


