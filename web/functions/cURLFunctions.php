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

foreach(getYears() as $value) {
	echo $value;
}*/

function getYears() {
	global $serverURL;

	$response = httpGet($serverURL . "/year");

	$json = (array) json_decode($response, true);
	
	return $json["info"];
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

/* Returns true or false if section has been created successfully */

function createSection($section, $year) {
	global $serverURL;
	global $editKey;
	
	$section = str_replace(" ", "%20", $section);
	
	$response = httpGet($serverURL . "/createsection?key=" . $editKey . "&name=" . $section . "&year=" . $year);
	
	$json = (array) json_decode($response, false);
	
	return $json["succeed"];
}

function createYear($year) {
	global $serverURL;
	global $editKey;
	
	$year = str_replace(" ", "%20", $year);
	
	$response = httpGet($serverURL . "/createyear?key=" . $editKey . "&year=" . $year);

	$json = (array) json_decode($response, false);
	
	return $json["succeed"];
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

/* Returns json array once video has been created */

function createVideo($title, $desc, $fileid, $section, $year) {
	global $serverURL;
	global $editKey;
	
	$title = str_replace(" ", "%20", $title);
	$desc = str_replace(" ", "%20", $desc);
	$section = str_replace(" ", "%20", $section);
	
	$response = httpGet($serverURL . "/createvideo?key=" . $editKey . "&title=" . $title . "&desc=" . $desc . "&section=" . $section . "&fileid=" . $fileid . "&year=" . $year);

	$json = (array) json_decode($response, true);
	
	return $json["info"];
}

/* Returns json array once video has been updated */

function updateVideo($title, $desc, $fileid, $section, $year, $ID) {
	global $serverURL;
	global $editKey;
	
	$title = str_replace(" ", "%20", $title);
	$desc = str_replace(" ", "%20", $desc);
	$section = str_replace(" ", "%20", $section);
	
	$response = httpGet($serverURL . "/createvideo?key=" . $editKey . "&title=" . $title . "&desc=" . $desc . "&section=" . $section . "&fileid=" . $fileid . "&year=" . $year . "&ID=" . $ID);

	$json = (array) json_decode($response, true);
	
	return $json["info"];
}

function deleteVideo($ID) {
	global $serverURL;
	global $editKey;
	
	httpGet($serverURL . "/delete?key=" . $editKey . "&type=" . "video" . "&ID=" . $ID);
}

function deleteYear($year) {
	global $serverURL;
	global $editKey;
	
	httpGet($serverURL . "/delete?key=" . $editKey . "&type=" . "year" . "&year=" . $year);
}

function deleteSection($section, $year) {
	global $serverURL;
	global $editKey;
	
	$section = str_replace(" ", "%20", $section);
	
	httpGet($serverURL . "/delete?key=" . $editKey . "&type=" . "section" . "&section=" . $section . "&year=" . $year);
}

/* Returns json array once section has been updated */

function updateSection($oldsection, $newsection, $ID, $year) {
	global $serverURL;
	global $editKey;
	
	$oldsection = str_replace(" ", "%20", $oldsection);
	$newsection = str_replace(" ", "%20", $newsection);
	
	$response = httpGet($serverURL . "/createsection?key=" . $editKey . "&name=" . $newsection . "&year=" . $year . "&oldname=" . $oldsection . "&ID=" . $ID);

	$json = (array) json_decode($response, true);
	
	return $json["info"];
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
	
	return $json;
}

function loginCheck($login) {
	$login = $login["succeed"];
	
	return $login;
}

function isAdmin($login) {
	$isAdmin = false;
	
	foreach($login["info"] as $value) {
		$isAdmin = $value["admin"];
	}
	
	return $isAdmin;
}
?>