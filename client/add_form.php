<html>
<head>
	<title>Add user</title>
</head>

<body>
	<h1>Add user</h1>
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
</body>

</html>