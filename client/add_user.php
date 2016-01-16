<?php
$url = "http://localhost:9090/users";    
$curl = curl_init($url);

//The JSON data.
$jsonData = array(	   
	"firstName" => $_POST['firstName'],
	"lastName" => $_POST['lastName'],
	"email" => $_POST['email'],
	"password" => $_POST['password'],
	"organization" => $_POST['organization'],
	"department" => $_POST['department'],
	"role" => $_POST['role']
);

//Encode the array into JSON.
$jsonDataEncoded = json_encode($jsonData);

curl_setopt($curl, CURLOPT_HEADER, false);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
curl_setopt($curl, CURLOPT_HTTPHEADER, array("Content-type: application/json"));
curl_setopt($curl, CURLOPT_POST, true);
curl_setopt($curl, CURLOPT_POSTFIELDS, $jsonDataEncoded);

$json_response = curl_exec($curl);

if (curl_getinfo($curl, CURLINFO_HTTP_CODE) != 200) {
    die("Error: call to URL $url failed with status $status, response $json_response, curl_error " . curl_error($curl) . ", curl_errno " . curl_errno($curl));
}

curl_close($curl);

header("Location: index.php");
?>