<?php
// This is a protected resource
session_start();
if($_SESSION['loggedIn']!= true){
		echo 'pys';
		header('Location: login_form.php');
		exit();
	}
if (isset($_SESSION['loggedIn'])) {
	
	echo "yabbadabbadoo";
} 
?>

<html>
<head>
	<title>Profile</title>
</head>

<body>
	<h1>Profile for <?=$_SESSION['userId']?></h1>

	<form action="logout_handler.php" method="POST">
		
		<button type="submit" value="Log out">Log out</button>
	</form>
</body>
</html>