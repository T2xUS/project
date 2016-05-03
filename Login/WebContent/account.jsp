<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Example</title>
    </head>
    <body>
        <form method="post" action="login.jsp">
            <center>
            <table border="1" width="30%" cellpadding="3">
                <thead>
                    <tr>
                        <th colspan="2">Account Summary</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>User Name</td>
                        <td><%=session.getAttribute("userid")%></td>
                    </tr>
                    <tr>
                        <td>Score</td>
                        <td><%=session.getAttribute("info")%></td>
                    </tr>
                    <tr>
                        <td>Level</td>
                        <td><%=session.getAttribute("levelnum")%></td>
                    </tr>
                   	<tr>
                        <td colspan="2"><a href='changePW.jsp'>Change Your Password</a></td>
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