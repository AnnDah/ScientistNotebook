<?php
// This is a protected resource
session_start();
?>

<html>
<head>
	<title>Profile</title>
</head>

<body>
	<h1>Profile for <?=$_SESSION['userId']?></h1>
</body>
</html>