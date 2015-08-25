<?php
// Verifiera anvandaren
$email = $_POST["email"];
$password = $_POST["password"];

$url = "http://localhost:9090/login";    
$curl = curl_init($url);

//The JSON data.
$jsonData = array(	   
	"email"=>"$email",
	"password"=>"$password"
);

//Encode the array into JSON.
$jsonDataEncoded = json_encode($jsonData);

curl_setopt($curl, CURLOPT_HEADER, false);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
curl_setopt($curl, CURLOPT_HTTPHEADER,
        array("Content-type: application/json"));
curl_setopt($curl, CURLOPT_POST, true);
curl_setopt($curl, CURLOPT_POSTFIELDS, $jsonDataEncoded);

$json_response = curl_exec($curl);

$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);

if ( $status != 200 ) {
    header("Location: login_form.php?failed=true");
}

session_start();


curl_close($curl);
$response = json_decode($json_response, true);


//$id = $response["id"];
header("Location: profile.php");
$_SESSION['loggedIn']=true;
$_SESSION['userId']=$response["id"];
$_SESSION['firstname']=$response['firstName'];
$_SESSION['lastname']=$response['lastName'];
//echo 'Hej';
//exit();
?>