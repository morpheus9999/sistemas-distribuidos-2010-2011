<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>BetAndUin - Add New User</title>
<link href="css/page.css" rel="stylesheet" type="text/css" />
</head>

<body>

<div id="bg_container">
    <div id="container">
        <br>
        <img src="images/add_new_user_normal.png" />
        <br><br>

        <form action="RegistoServlet" method="post">
            Username: <input type="text" name="userName" value="" size="30">
            <br>
            Password: <input type="password" name="passWord" value="" size="30">
            <br>
            email: <input type="text" name="mail" value="" size="30">
            <br>
            credito: <input type="text" name="credit" value="" size="10">
            <br><br>
            <input type="button" value="back" onClick="history.go(-1)">
            <input type="submit" value="ADD USER">
        </form>
    </div>

    <div id="logo"></div>
</div>

<div id="square"></div>

<div id="footer">
    <div class="line1">Application created to 2010 Distributed Systems project.</div>
    <div class="line2">Developed by Jorge Figueira.</div>
</div>

</body>
</html>
