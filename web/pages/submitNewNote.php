
<?php 
/*
class User {
    public $firstname = "";
    public $email  = "";    
}
 
$user = new User();
$user->firstname = $_POST["firstName"];
$user->lastname  = $_POST["lastName"];
$user->email  = $_POST["mail@gmail.com"];
$user->password  = $_POST["1234"];
$user->organization  = $_POST["MAH"];
$user->department  = $_POST["computer"];
$user->role  = $_POST["user"];

*/


//API Url
$url = 'http://localhost:9090/test';

$author = "mail@gmail.com";
$projSelection = $_POST["projSelection"];
$noteTitle = $_POST["noteTitle"];
$noteType = $_POST["newNote_radioOpt"];
$noteTags = $_POST["noteTags"]; //this should be array.



$tagArray = array($noteTags);
foreach ("," as $noteTags) {
    $tagString + $noteTags;
}
// $arr is now array(2, 4, 6, 8)
unset($tagString); // break the reference with the last element
echo $tagString;




$noteBody = $_POST["noteBody"];
//$shareSetting = $_POST["privacy"];
$shareSetting = 101;
$noteDescription = $_POST["description"];

//trim whitespace for tags
trim($noteTitle, " ");

//Initiate cURL.
$ch = curl_init($url);

//The JSON data.
$jsonData = array(	
   /* 'username' => 'MyUsername',
    'password' => 'MyPassword'*/
	
	"content"=>"$noteBody",
	"author"=>"$author",
	"level"=>"$shareSetting",
	"dataType"=>"$noteType",
	"project"=>"$projSelection",
	"name"=>"$noteTitle",
	"description"=>"$noteDescription",
	"tags"=>"$noteTags"

);	
	/*
	// Create a new data
POST 
/data

JSON format in request body: 
{
"content":"some data",
"author":"Annika Magnusson",
"level":"101",
"dataType":"doc",
"project":"some project",
"name":"data name",
"description":"blablabla",
"tags":["tag1","tag2"]


}
*/


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



	
