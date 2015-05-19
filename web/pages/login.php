<?php
//session_start(); // Starting Session
$error=''; // Variable To Store Error Message
if (isset($_POST['submit'])) {
if (empty($_POST['username']) || empty($_POST['password'])) {
$error = "Username or Password is invalid";
}
/*
else
{
// Define $username and $password
$username=$_POST['username'];
$password=$_POST['password'];

// Establishing Connection with Server by passing server_name, user_id and password as a parameter
//$connection = mysql_connect("localhost", "root", "");

// To protect MySQL injection for Security purpose
$username = stripslashes($username);
$password = stripslashes($password);
//$username = mysql_real_escape_string($username);
//$password = mysql_real_escape_string($password);

// Selecting Database
//$db = mysql_select_db("company", $connection);

// SQL query to fetch information of registerd users and finds user match.
//$query = mysql_query("select * from login where password='$password' AND username='$username'", $connection);
//$rows = mysql_num_rows($query);
//if ($rows == 1) {

//need validation that password and username is correct. use post, take returning json and validate against entered pass and user.

*/

//API Url
$url = 'http://localhost:9090/login';


$email = $_POST["username"];
$password = $_POST["password"];



//Initiate cURL.
$ch = curl_init($url);

//The JSON data.
$jsonData = array(	   
	"email"=>"$email",
	"password"=>"$password",	
);

//Encode the array into JSON.
$jsonDataEncoded = json_encode($jsonData);

//Tell cURL that we want to send a POST request.
curl_setopt($ch, CURLOPT_POST, 1);

//Attach our encoded JSON string to the POST fields.
curl_setopt($ch, CURLOPT_POSTFIELDS, $jsonDataEncoded);

//Set the content type to application/json
curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json')); 

//Execute the request
$result = curl_exec($ch);

curl_close($ch);

//echo $result;

//this needs to listen to the response from BE
$returningString = json_decode($result, true);

var_dump(json_decode($result, true));
//var_dump($returningString);
//echo $returningString->email;

//returningString=>
//print $returningString;

//echo $checkForUser;

//$_SESSION['login_user']=$username; // Initializing Session
//header("location: profile.php"); // Redirecting To Other Page
//} else {
//$error = "Username or Password is invalid";
//}
//mysql_close($connection); // Closing Connection
//}
}

//http://stackoverflow.com/questions/16700960/how-to-use-curl-to-get-json-data-and-decode-the-data

/*
//  Initiate curl
$ch = curl_init();
// Disable SSL verification
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
// Will return the response, if false it print the response
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
// Set the url
curl_setopt($ch, CURLOPT_URL,$url);
// Execute
$result=curl_exec($ch);
// Closing
curl_close($ch);

// Will dump a beauty json :3
var_dump(json_decode($result, true));

*/
?>