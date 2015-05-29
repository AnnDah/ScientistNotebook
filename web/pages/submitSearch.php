
<?php 
//Created by Kristoffer Olsson
//Edited by Kristoffer Olsson

/*
This class is not completed. Search is to made based on just tagword in this iteration
*/

//API Url
$url = 'http://localhost:9090/test';

$field = $_POST["search_field"];
$projStatus = $_POST["search_projStatus"];
$sortBy = $_POST["search_sortResults"];
$projectOrNote = $_POST["search_radioSelection"];
$searchString = $_POST["searchString"];



//Initiate cURL.
$ch = curl_init($url);

//The JSON data.
$jsonData = array(	
   /* 'username' => 'MyUsername',
    'password' => 'MyPassword'*/
	
	"field"=>"$field",
	"projectStatus"=>"$projStatus",
	"sortBy"=>"$sortBy",
	"searchSelection"=>"$projectOrNote",
	"searchString"=>"$searchString"
	
	/*
	// Get result of projects through search with tags
GET 
/projects?tags=<tags>

Include all tags separated with comma.
Exampel: http://localhost:9090/projects?tags=train,car,cat
Response will include:
	id
	name
	status
	description
	created
	isPrivate
*/
);

//Encode the array into JSON.
$jsonDataEncoded = json_encode($jsonData);

//Tell cURL that we want to send a POST request.
curl_setopt($ch, CURLOPT_GET, 1); //curl_setopt($ch, CURLOPT_POST, 1);

//Attach our encoded JSON string to the POST fields.
curl_setopt($ch, CURLOPT_POSTFIELDS, $jsonDataEncoded);

//Set the content type to application/json
curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json')); 

//Execute the request
$result = curl_exec($ch);


?>



	
