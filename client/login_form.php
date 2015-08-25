<?php
// This is a protected resource
//session_start();
session_start();

if (isset($_SESSION['loggedIn'])) {
	header('Location: profile.php');
	exit();
} 

$_SESSION['loggedIn'] = false;

if($_SESSION['loggedIn']!= true){
		session_unset();
		session_destroy();
		
	}

?>
<html>
<head>
	<title>Login form</title>
</head>

<body>
	<h1>Logga in</h1>
	<form action="login_handler.php" method="POST">
		Email: <input type="text" name="email" id="email"><br>
		Password: <input type="password" name="password" id="password"><br>
		<button type="submit" value="Log in">Log in</button>
	</form>
</body>
</html>