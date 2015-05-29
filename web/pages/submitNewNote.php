
<?php 
//Created by Kristoffer Olsson
//Edited by Kristoffer Olsson


//cURL use to send json to backend

//API Url
$url = 'http://localhost:9090/test';

$author = "mail@gmail.com";
$projSelection = $_POST["projSelection"];
$noteTitle = $_POST["noteTitle"];
$noteType = $_POST["newNote_radioOpt"];
$noteTags = $_POST["noteTags"]; //this should be array.


/* not working, need solution
$tagArray = array($noteTags);
foreach ("," as $noteTags) {
    $tagString + $noteTags;
}
// $arr is now array(2, 4, 6, 8)
unset($tagString); // break the reference with the last element
echo $tagString;
*/


$noteBody = $_POST["noteBody"];
//$shareSetting = $_POST["privacy"];
$shareSetting = 101; //hard-coded
$noteDescription = $_POST["description"];

//trim whitespace for tags
trim($noteTitle, " ");

//Initiate cURL.
$ch = curl_init($url);

//The JSON data.
$jsonData = array(	   
	"content"=>"$noteBody",
	"author"=>"$author",
	"level"=>"$shareSetting",
	"dataType"=>"$noteType",
	"project"=>"$projSelection",
	"name"=>"$noteTitle",
	"description"=>"$noteDescription",
	"tags"=>"$noteTags" //needs to be array of tags

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



	
