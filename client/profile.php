<?php
// This is a protected resource
session_start();
if (isset($_SESSION['loggedIn'])) {
	if($_SESSION['loggedIn']!= true){
		echo 'pys';
		header('Location: login_form.php');
		exit();
	}
	echo "yabbadabbadoo";
} 
?>

<html>
<head>
	<title>Profile</title>
</head>

<body>
	<h1>Profile for <?=$_SESSION['userId']?></h1>
</body>
</html>