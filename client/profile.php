<?php
// This is a protected resource
session_start();

if (isset($_SESSION['loggedIn'])) {
	if ($_SESSION['loggedIn'] == false) {
		header('Location: index.php');
		exit();
	}	
}

// Get the user object from the server
$url = "http://localhost:9090/users/" . $_SESSION['userId'];
$curl = curl_init($url);

curl_setopt($curl, CURLOPT_HEADER, false);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);

$response = json_decode(curl_exec($curl), true);

if (curl_getinfo($curl, CURLINFO_HTTP_CODE) != 200) {
  //header('Location: logout_handler.php');
  exit;
}

curl_close($curl);

$user = $response['users'][0];
?>

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

    <div class="container" style="margin-top: 80px;">
      <h1>Profile page</h1>
      <table class='table table-striped table-hover'>
        <thead></thead>
        <tbody>
          <tr>
            <th>Name</th>
            <td><?php echo $user['firstName'] . " " . $user['lastName']; ?></td>
          </tr>
          <tr>
            <th>ID</th>
            <td><?php echo $user['id']; ?></td>
          </tr>
          <tr>
            <th>Email</th>
            <td><?php echo $user['email'];?></td>
          </tr>
          <tr>
            <th>Member since</th>
            <td><?php echo $user['memberSince']; ?></td>
          </tr>
          <tr>
            <th>Role</th>
            <td><?php echo $user['role']; ?></td>
          </tr>
          <tr>
            <th>Organization</th>
            <td><?php echo $user['organization']; ?></td>
          </tr>
          <tr>
            <th>Department</th>
            <td><?php echo $user['department']; ?></td>
          </tr>
          <tr>
            <th>Follows</th>
            <td>
                <ul>
                  <?php foreach ($user['follows'] as $item) { ?>
                    <li><?php echo $item;?></li>
                  <?php } ?>
                </ul>
            </td>
          </tr>
        </tbody>
      </table>
    </div> <!-- /container -->
</body>
</html>