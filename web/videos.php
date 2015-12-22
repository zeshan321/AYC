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
	  $i = 0;
	  foreach(getVideos($_GET['section'], $_GET['year']) as $value) {
        echo "<div class=\"col-xs-18 col-sm-6 col-md-3\">";
          echo "<div class=\"thumbnail\">";
              echo "<div class=\"caption\">";
                 echo "<h4 id=\"h4". $i ."\">". $value["name"] . "</h4>";
                 echo "<p id=\"p". $i ."\">" . $value["desc"] . "</p>";
                 echo "<a href=\"\" onclick=\"openModal(" . $i . "," . $value["fileid"] . "," . $value["ID"] .")\" class=\"btn btn-primary btn-block\">View</a>";
            echo "</div>";
          echo "</div>";
        echo "</div>";
		
		$i++;
		}
		?>
        
      </div><!-- End row -->
      
    </div><!-- End container -->
	
	<script>
	function openModal(i, fileid, id) {
		console.log(i + " " + fileid + " " + id);
	}
	
	function getParam(name) {
		name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
			results = regex.exec(location.search);
		return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	}
	</script>

	<script src="js/jquery-1.11.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
</body>

</html>