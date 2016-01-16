<?php
if (empty($_POST['email'])){
	header('Location: index.php?error_email=true');
	exit();
} 
if (empty($_POST['password'])) {
	header('Location: index.php?error_password=true');
	exit();
}


// Verify credentials
$curl = curl_init("http://localhost:9090/login");

//The JSON data.
$jsonData = array(	   
	"email" => $_POST["email"],
	"password" => $_POST["password"]
);

curl_setopt($curl, CURLOPT_HEADER, false);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
curl_setopt($curl,CURLOPT_HTTPHEADER, array("Content-type: application/json"));
curl_setopt($curl, CURLOPT_POST, true);
curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($jsonData));

$response = json_decode(curl_exec($curl), true);

if (isset($response['error'])) {
  session_start();
  session_unset();
  session_destroy();
  header("Location: index.php?error_email=true&error_password=true");
  exit();
}

session_start();

curl_close($curl);

header("Location: profile.php");
$_SESSION['loggedIn'] = true;
$_SESSION['userId'] = $response["id"];
$_SESSION['name'] = $response["firstName"] . " " . $response['lastName'];
?>