<?php
include('pages/login.php'); // Includes Login Script

if(isset($_SESSION['login_user'])){
header("location: pages/profile.php");
}
?>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SciNote</title>

    <!-- Bootstrap Core CSS -->
    <link href="UI/startbootstrap-sb-admin-2-1.0.7/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="UI/startbootstrap-sb-admin-2-1.0.7/bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">

    <!-- Timeline CSS -->
    <link href="UI/startbootstrap-sb-admin-2-1.0.7/dist/css/timeline.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="UI/startbootstrap-sb-admin-2-1.0.7/dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Morris Charts CSS -->
    <link href="UI/startbootstrap-sb-admin-2-1.0.7/bower_components/morrisjs/morris.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="UI/startbootstrap-sb-admin-2-1.0.7/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>
	
<body>	


<body>
<div id="main" style="margin-left:30%; margin-right:30%;">
<h1>Welcome to SciNote</h1>
<div id="login">
<h2>Please log in</h2>
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

<br>
<br>
<br>
<div style="margin-left:40%; margin-right:40%;">
<h2>Not a member?<br>
<a href="pages/new_user.html" style="text-decoration:none;">Become one now!</a></h2>
</div>

	<!-- jQuery -->
    <script src="UI/startbootstrap-sb-admin-2-1.0.7//bower_components/jquery/dist/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="UI/startbootstrap-sb-admin-2-1.0.7//bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="UI/startbootstrap-sb-admin-2-1.0.7//bower_components/metisMenu/dist/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="UI/startbootstrap-sb-admin-2-1.0.7//dist/js/sb-admin-2.js"></script>

</body>
</html>
