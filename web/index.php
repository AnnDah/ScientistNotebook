<?php
include('pages/login.php'); // Includes Login Script

if(isset($_SESSION['login_user'])){
header("location: pages/profile.php");
}
?>
<!DOCTYPE html>
<html>
<head>
<title>SciNote</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>

<body>
<div id="main">
<h1>Welcome to SciNote</h1>
<div id="login">
<h2>Login Form</h2>
<form enctype="application/json" action="pages/login.php" method="post">
<label>Email :</label>
<input id="name" name="username" placeholder="username" type="text">
<label>Password :</label>
<input id="password" name="password" placeholder="**********" type="password">
<input name="submit" type="submit" value=" Login ">
<span><?php echo $error; ?></span>
</form>
</div>
</div>
</body>

</html>