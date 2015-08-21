<html>
<head>
	<title>Add user</title>
</head>

<body>
	<?php
	$first_name = $_POST["firstName"];
	$last_name = $_POST["lastName"];
	$email = $_POST["email"];
	$password = $_POST["password"];
	$organization = $_POST["organization"];
	$department = $_POST["department"];
	$role = $_POST["role"];

	$url = "http://localhost:9090/users";    
	$curl = curl_init($url);
	
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


	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_HTTPHEADER,
	        array("Content-type: application/json"));
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $jsonDataEncoded);

	$json_response = curl_exec($curl);

	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);

	if ( $status != 200 ) {
	    die("Error: call to URL $url failed with status $status, response $json_response, curl_error " . curl_error($curl) . ", curl_errno " . curl_errno($curl));
	}

	curl_close($curl);

	//$response = json_decode($json_response, true);

	echo "Response: " . $json_response;
	?>
</body>
</html>