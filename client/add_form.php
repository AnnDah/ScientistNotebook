<?php
session_start();
?>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="static/favicon.ico">

    <title>Create new user</title>

    <!-- Bootstrap core CSS -->
    <script src="https://code.jquery.com/jquery-1.12.0.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" type="text/css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha256-KXn5puMvxCw+dAYznun+drMdG1IFl3agK0p/pqT9KAo= sha512-2e8qq0ETcfWRI4HJBzQiA3UoyFk6tbNyG+qSaIBZLyW9Xf3sWZHN/lxe9fTh1U45DpPf07yj94KsUHHWe4Yk1A==" crossorigin="anonymous"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
	

<body>
  <nav class="navbar navbar-default navbar-fixed-top navbar-inverse">
      <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="index.php">SciNote</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          <ul class="nav navbar-nav">
            <li><a href="add_form.php">New user</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <?php
              if (isset($_SESSION['loggedIn'])) {
                if ($_SESSION['loggedIn'] == true) {
            ?>
                <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><?php echo $_SESSION['name']; ?><span class="caret"></span></a>
                  <ul class="dropdown-menu">
                    <li><a href="profile.php">My profile</a></li>
                    <li><a href="logout_handler.php">Log out</a></li>
                  </ul>
                </li>
            <?php
                }
              } else {
            ?>
              <form class="navbar-form navbar-right" action="login_handler.php" method="POST">
                <div class="form-group">      
                  <input type="text" name='email' placeholder="Email" class="form-control" <?php if(isset($_GET['error_email'])) :?>style="border-color: #FF0000" <?php endif; ?> id='email'>
                </div>
                <div class="form-group">
                  <input type="password" name='password' placeholder="Password" class="form-control" <?php if(isset($_GET['error_password'])) :?>style="border-color: #FF0000" <?php endif; ?> id='password'>
                </div>
               <button type="submit" value="Log in" class='btn btn-default'>Log in</button>
              </form>
            <?php
              }
            ?>
        </ul>
        </div><!-- /.navbar-collapse -->
      </div><!-- /.container-fluid -->
    </nav>

  <div class="container" style="margin-top: 80px; height:100px; length:100px;"><!-- big div-->
	  <div style="display:inline-block; float:right; background-color:white; width:50%;"><!--div to hold actual form-->	
		<form role="form" action="add_user.php" method="POST">	
			<div class="form-group">														
				<label>First name</label>
				<input class="form-control" placeholder="First name..." name="firstName">			
			</div>
		
			<div class="form-group">														
				<label>Last name</label>
				<input class="form-control" placeholder="Last name..." name="lastName">			
			</div>
		
			<div class="form-group">														
				<label>Email</label>
				<input class="form-control" placeholder="Email..." name="email">
				<p class="help-block">You will use this to log in</p>
			</div>
		
			<div class="form-group">														
				<label>Password</label>
				<input class="form-control" placeholder="Password..." name="password" type="password">			
			</div>
		
			<div class="form-group">
			<label>Project field</label>
			<select class="form-control" name="organization">                                    									
				<option>None</option>
				<option>Org1</option>
			</select>		
			</div>
		
			<div class="form-group">
			<label>Department</label>
			<select class="form-control" name="department">                                    									
				<option>MAH</option>
				<option>Other</option>
			</select>		
			</div>
		
			<div class="form-group">
			<label>Role</label>
				<select class="form-control" name="role">                                    									
				<option>User</option>
				<option>Admin</option>
			</select>	
			</div>

			<button type="submit" class="btn btn-default">Submit</button>

		</form>
	</div><!-- </ of form holder-->

	<div style="display:inline-block; float:left; background-color:white; width:50%; padding:50px;">		
		<h1>Create a new user</h1>			
		<p>Use the form to the right to sign up, it's free!</p>
	</div>
	
</div>	<!--end of big div-->
</body>

</html>