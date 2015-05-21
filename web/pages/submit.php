
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
   /* 'username' => 'MyUsername',
    'password' => 'MyPassword'*/
	
	"firstname"=>"$first_name",
	"lastname"=>"$last_name",
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



	
