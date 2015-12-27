<?php
   include 'functions/cURLFunctions.php';
   session_start();
   
   if(!isset($_SESSION['login_user'])) {
   	header("location: login");
   }
   
   if (!isset($_GET['section']) && !isset($_GET['year'])) {
	   exit;
   }
 ?>

	<!DOCTYPE html>
	<html>

	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>Aikido Yoshinkai Canada</title>

		<link href="bootstrap/bootstrap.min.css" rel="stylesheet">
		<link href="css/styles.css" rel="stylesheet">
		<link href="css/videos.css" rel="stylesheet">

		<!--[if lt IE 9]>
<script src="js/html5shiv.js"></script>
<script src="js/respond.min.js"></script>
<![endif]-->

	</head>

	<body>
		<!-- End container -->
		
		<div class="row carousel-row">
		<?php
		 foreach(getVideos($_GET['section'], $_GET['year']) as $value) {
			echo "<div class=\"col-xs-8 col-xs-offset-2 slide-row\">";
				echo "<div class=\"slide-content\">";
					echo "<h4 id=\"h4". $value["ID"] ."\">" . $value["name"] . "</h4>";
					echo "<p id=\"p". $value["ID"] ."\">" . $value["desc"] . "</p>";
				echo "</div>";
				echo "<div class=\"slide-footer\">";
					echo "<span class=\"pull-right buttons\">";
                    echo "<button class=\"btn btn-sm btn-primary\" onclick=\"parent.openUpdateVideo('" . str_replace("'", "\'", $value["ID"]) . "','" . str_replace("'", "\'", $value["name"]) . "','" . str_replace("'", "\'", $value["desc"]) . "');\">Edit</button>";
					echo "<button style=\"margin-left: 5px;\" class=\"btn btn-sm btn-danger\" onclick=\"parent.openDeleteVideo('" . str_replace("'", "\'", $value["ID"]) . "','" . str_replace("'", "\'", $value["name"]) . "');\">Delete</button>";
               echo "</span>";
				echo "</div>";
			echo "</div>";
		 }
		?>
		</div>

		<script src="js/jquery-1.11.1.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
	</body>

	</html>