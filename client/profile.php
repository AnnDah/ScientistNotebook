<?php
// This is a protected resource
session_start();
if($_SESSION['loggedIn']!= true){
		echo 'pys';
		header('Location: login_form.php');
		exit();
	}
if (isset($_SESSION['loggedIn'])) {
	

} 
?>

<html>
<head>
	<title>Profile</title>
</head>

<body>
	<h1>Profile page</h1>
	This is the profile page for <?=$_SESSION['firstname']?> <?=$_SESSION['lastname']?>.<br> 
	The user ID is <?=$_SESSION['userId']?>.

	<form action="logout_handler.php" method="POST">
		
		<button type="submit" value="Log out">Log out</button>
	</form>
</body>
</html>