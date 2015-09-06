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
<div class="container" style="height:100px; length:100px;"><!-- big div-->
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
			
			<h1>Here is what you get!</h1>			
		
			<p style="font-size:125%;">"Lorem ipsum dolor sit amet, 
			consectetur adipiscing elit, sed do eiusmod
			tempor incididunt ut labore et dolore magna aliqua.
			Ut enim ad minim veniam, quis nostrud exercitation <br>
			ullamco laboris nisi ut aliquip ex ea commodo consequat.<br>
			Duis aute irure dolor in reprehenderit in voluptate <br>
			velit esse cillum dolore eu fugiat nulla pariatur. <br>
			Excepteur sint occaecat cupidatat non proident, sunt in culpa
			qui officia deserunt mollit anim id est laborum."<p>
			<ul>
				<li><p>A place to save you findings</p></li>
				<li><p>Share them with colleagues</p></li>					
			</ul>						
			<br>
			<h3>Use the form to the right to sign up, it's free!</h3>
			</div>		
	</div>
	
</div>	<!--end of big div-->

<!-- Placed at the end of the document so the pages load faster -->
    <script src="../static/jquery.min.js"></script>
    <script src="../static/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="ie10-viewport-bug-workaround.js"></script>
</body>

</html>