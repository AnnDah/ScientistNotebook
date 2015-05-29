<?php 
//Created by Kristoffer Olsson
//Edited by Kristoffer Olsson

//API Url
$url = 'http://localhost:9090/users';

$first_name = $_POST["firstName"];
$last_name = $_POST["lastName"];
$email = $_POST["email"];
$password = $_POST["password"];
$organization = $_POST["organization"];
$department = $_POST["department"];
$role = $_POST["role"];




//Initiate cURL.
$ch = curl_init($url);

//The JSON data.
$jsonData = array(	   
	"firstName"=>"$first_name",
	"lastName"=>"$last_name",
	"email"=>"$email",
	"password"=>"$password",
	"organization"=>"$organization",
	"department"=>"$department",
	"role"=>"$role"	
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


?>



	
