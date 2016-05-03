<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Example</title>
    </head>
    <body>
        <form method="post" action="changeMyPW.jsp">
            <center>
            <table border="1" width="30%" cellpadding="3">
                <thead>
                    <tr>
                        <th colspan="2">Reset Password Here</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>User Name</td>
                        <td><input type="text" name="uname" value="" /></td>
                    </tr>
                    <tr>
                        <td>Password</td>
                        <td><input type="password" name="pass" value="" /></td>
                    </tr>
                    <tr>
                        <td>New Password</td>
                        <td><input type="password" name="new_password" value="" /></td>
                    </tr>
                    <tr>
                        <td>Confirm Your Password</td>
                        <td><input type="password" name="new_password2" value="" /></td>
                    </tr>
                    <tr>
                        <th colspan="2"><input type="submit" value="Submit" /></th>
                    </tr>
                    <tr>
                    	<td colspan="2"> <a href='account.jsp'>View Your Account Summary</a></td>
                    </tr>
                    <tr>
                    	<td colspan="2"> <a href='logout.jsp'>Log out</a></td>
                    </tr>                    
                </tbody>
            </table>
            </center>
        </form>
    </body>
</html>