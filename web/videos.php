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

<!--[if lt IE 9]>
<script src="js/html5shiv.js"></script>
<script src="js/respond.min.js"></script>
<![endif]-->

</head>

<body>
    <div class="container" id="tourpackages-carousel">
      
      <div class="row">     

	  <?php
	  foreach(getVideos($_GET['section'], $_GET['year']) as $value) {
			echo "<div class=\"col-xs-18 col-sm-6 col-md-3\">";
			  echo "<div class=\"thumbnail\">";
				  echo "<div class=\"caption\">";
					 echo "<h4 id=\"h4". $value["ID"] ."\">". $value["name"] . "</h4>";
					 echo "<p id=\"p". $value["ID"] ."\">" . $value["desc"] . "</p>";
					 echo "<a onclick=\"parent.openUpdateVideo('" . $value["ID"] . "','" . $value["name"] . "','" . $value["desc"] . "');\" class=\"btn btn-primary btn-block\">View</a>";
				echo "</div>";
			  echo "</div>";
			echo "</div>";
		}
		?>
        
      </div><!-- End row -->
      
    </div><!-- End container -->

	<script src="js/jquery-1.11.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
</body>

</html>