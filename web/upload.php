<?php
include 'functions/cURLFunctions.php';
include 'functions/config.php';
session_start();

if (!isset($_SESSION['login_user'])) {
    header("location: login");
}

if (isset($_POST['videoinputsection']) && isset($_POST['videoinputtitle']) && isset($_POST['videoinputdesc']) && isset($_POST['selectedyearvideo'])) {
	global $uploadDir;
    $video = createVideo($_POST['videoinputtitle'], $_POST['videoinputdesc'], "0", $_POST['videoinputsection'], $_POST['selectedyearvideo']);
	
	if ($_FILES['videoinputfile']['name'] != "") {
		foreach ($video as $value) {
			$fileExt = end((explode(".", $_FILES["videoinputfile"]["name"])));
			$fileName = $value["ID"] . "." . $fileExt;
			move_uploaded_file($_FILES["videoinputfile"]["tmp_name"], $uploadDir . $fileName);
			
			chmod($uploadDir . $fileName, 0644);
		}
	}
	
	echo "<script>window.location.assign(\"index?year=" . $_POST['selectedyearvideo'] ."&section=" . $_POST['videoinputsection'] . "\");</script>";
}

if (isset($_POST['updatevideoinputtitle']) && isset($_POST['updatevideoinputdesc']) && isset($_POST['updatevideoid']) && isset($_POST['updatevideoyear']) && isset($_POST['updatevideosection'])) {
	global $uploadDir;
    updateVideo($_POST['updatevideoinputtitle'], $_POST['updatevideoinputdesc'], "0", $_POST['updatevideosection'], $_POST['updatevideoyear'], $_POST['updatevideoid']);
	
	if ($_FILES['updatevideoinputfile']['name'] != "") {
		$fileExt = end((explode(".", $_FILES["updatevideoinputfile"]["name"])));
		$fileName = $_POST['updatevideoid'] . "." . $fileExt;
		move_uploaded_file($_FILES["updatevideoinputfile"]["tmp_name"], $uploadDir . $fileName);
		
		chmod($uploadDir . $fileName, 0644);
	}
	
	echo "<script>window.location.assign(\"index?year=" . $_POST['updatevideoyear'] ."&section=" . $_POST['updatevideosection'] . "\");</script>";
}
?>