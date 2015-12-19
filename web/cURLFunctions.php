<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include 'config.php';

function httpGet($url)
{
    $ch = curl_init(); 
    curl_setopt($ch, CURLOPT_URL, $url); 

	curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
	curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
	curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 10);
	curl_setopt($ch, CURLOPT_TIMEOUT, 10);

    $output = curl_exec($ch); 
    curl_close($ch); 
    
    return $output;
}

/* Example:

foreach(getSections("2015") as $value) {
	echo $value;
}*/

function getSections($year) {
	global $serverURL;

	$response = httpGet($serverURL . "/section?year=" . $year);

	$json = (array) json_decode($response, true);
	
	return $json["info"];
}

/* Example:

foreach(getVideos("Section 1", "2015") as $value) {
	echo $value["name"] . "<br>";
	echo $value["desc"] . "<br>";
	echo $value["fileid"] . "<br>";
	echo $value["year"] . "<br>";
} */

function getVideos($section, $year) {
	global $serverURL;
	
	$section = str_replace(" ", "%20", $section);
	
	$response = httpGet($serverURL . "/videos?section=" . $section . "&year=" . $year);

	$json = (array) json_decode($response, true);
	
	return $json["info"];
}

/* Returns true or false if section has been created successfully */

function createSection($section, $year) {
	global $serverURL;
	global $editKey;
	
	$section = str_replace(" ", "%20", $section);
	
	$response = httpGet($serverURL . "/createsection?key=" . $editKey . "&name=" . $section . "&year=" . $year);

	$json = (array) json_decode($response, false);
	
	return $json["succeed"];
}


/* Returns true or false if video has been created successfully */

function createVideo($title, $desc, $fileid, $section, $year) {
	global $serverURL;
	global $editKey;
	
	$title = str_replace(" ", "%20", $title);
	$desc = str_replace(" ", "%20", $desc);
	$section = str_replace(" ", "%20", $section);
	
	$response = httpGet($serverURL . "/createvideo?key=" . $editKey . "&title=" . $title . "&desc=" . $desc . "&section=" . $section . "&fileid=" . $fileid . "&year=" . $year);

	$json = (array) json_decode($response, false);
	
	return $json["succeed"];
}

/* Returns json array from login 
foreach(login("zeshan", "aslam") as $value) {
	echo $value["admin"] . "<br>";
}
*/
function login($user, $pass) {
	global $serverURL;
	
	$response = httpGet($serverURL . "/login?user=" . $user . "&pass=" . $pass);
	
	$json = (array) json_decode($response, true);
	
	return $json["info"];
}

?>