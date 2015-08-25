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

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="static/favicon.ico">

    <title>Log in</title>

    <!-- Bootstrap core CSS -->
    <link href="static/bootstrap.min.css" type="text/css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="static/jumbotron.css" type="text/css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="add_form.php">Register new user</a>
          <a class="navbar-brand" href="add_form.php">About SciNote</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <form class="navbar-form navbar-right" action="login_handler.php" method="POST">
            <div class="form-group">			
              Email: <input type="text" name='email' placeholder="Email" class="form-control" id='email'>
            </div>
            <div class="form-group">
              Password: <input type="password" name='password' placeholder="Password" class="form-control" id='password'>
            </div>
           <button type="submit" value="Log in">Log in</button>
          </form>
        </div><!--/.navbar-collapse -->
      </div>
    </nav>

    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
      <div class="container">
        <h1>Hello!</h1>
        <p>  Start using SciNote now!!!</p>
      </div>
    </div>

    <div class="container">
      <!-- Example row of columns -->
      <div class="row">
        <div class="col-md-4" align="center">
          <img src="static/logo.png" alt="SciNote logo" width="240" height="300">
       </div>
      </div>

      <hr>

      <footer>
        
      </footer>
    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="../static/jquery.min.js"></script>
    <script src="../static/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="ie10-viewport-bug-workaround.js"></script>
  </body>
</html>