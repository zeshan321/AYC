<?php
   include 'functions/cURLFunctions.php';
   session_start();
   
   if(!isset($_SESSION['login_user'])) {
   	header("location: login");
   }
   
   	if (isset($_POST['selectedYear']) && isset($_POST['sectionInput'])) {
		createSection($_POST['sectionInput'], $_POST['selectedYear']);
		header("index?year=" . $_POST['selectedYear']);
	}
	
	if (isset($_POST['yearInput'])) {
		createYear($_POST['yearInput']);
		header("index?year=" . $_POST['yearInput']);
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

<!--Icons-->
<script src="js/lumino.glyphs.js"></script>

<!--[if lt IE 9]>
<script src="js/html5shiv.js"></script>
<script src="js/respond.min.js"></script>
<![endif]-->

</head>

<body>
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#sidebar-collapse">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#"><span>AYC </span>Admin</a>
				<ul class="user-menu">
					<li class="dropdown pull-right">
						<a class="dropdown-toggle" data-toggle="dropdown"><svg class="glyph stroked male-user"><use xlink:href="#stroked-male-user"></use></svg><?php echo $_SESSION['login_user']; ?></span></a>
					</li>
				</ul>
			</div>
							
		</div><!-- /.container-fluid -->
	</nav>
		
	<div id="sidebar-collapse" class="col-sm-3 col-lg-2 sidebar">
		<ul class="nav menu">
			<li class="parent ">
			<a href="index"><span class="glyphicon glyphicon-home"></span> Home</a>
			<?php
			$i = 0;
			$yearList = getYears();
			foreach($yearList as $years) {
				if (isset($_GET['year']) && $_GET['year'] == $years) {
					echo "<a href=\"#d". $i ."\" data-toggle=\"collapse\" data-parent=\"#MainMenu\"><span class=\"glyphicon glyphicon-download\"></span>" . $years ."</a>";
					echo "<div class=\"collapse in\" id=\"d" . $i . "\">";
					foreach(getSections($years) as $section) {
						if (isset($_GET['section']) && $_GET['section'] == $section["name"]) {
							echo "<a href=\"#\" onclick=\"updatePrams('". $years . "','" . $section["name"] . "','" . $section["ID"] . "')\" class=\"active list-group-item\">" . $section["name"] . "</a>\n";
						} else {
							echo "<a href=\"#\" onclick=\"updatePrams('". $years . "','" . $section["name"] . "','" . $section["ID"] . "')\"  class=\"list-group-item\">" . $section["name"] . "</a>\n";
						}
					}
					echo "</div>";
				} else {
					echo "<a href=\"#d". $i ."\" data-toggle=\"collapse\" data-parent=\"#MainMenu\"><span class=\"glyphicon glyphicon-download\"></span>" . $years ."</a>";
					echo "<div class=\"collapse\" id=\"d" . $i . "\">";
					foreach(getSections($years) as $section) {
						echo "<a href=\"#\" onclick=\"updatePrams('". $years . "','" . $section["name"] . "','" . $section["ID"] . "')\"  class=\"list-group-item\">" . $section["name"] . "</a>\n";
					}
					echo "</div>";
				}
				$i++;
			}
			?>
			</li>
			<li role="presentation" class="divider"></li>
			<li><a href="" data-toggle="modal" data-target="#addYear"><span class="glyphicon glyphicon-plus"></span> Add year</a></li>
			<li><a href="" data-toggle="modal" data-target="#addSection"><span class="glyphicon glyphicon-plus"></span> Add section</a></li>
			<li><a href="" data-toggle="modal" data-target="#addVideo"><span class="glyphicon glyphicon-plus"></span> Add video</a></li>
			<li><a href="logout"><span class="glyphicon glyphicon-user"></span> Logout</a></li>
		</ul>

	</div><!--/.sidebar-->
	
	<!-- Add year modal -->
	<div class="modal fade" id="addYear" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
	  <div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
				<h3 class="modal-title" id="lineModalLabel">Add Year</h3>
			</div>
			<div class="modal-body">
				
				<!-- content goes here -->
				<form role="form" action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>" method="post">
				  <div class="form-group">
					<label for="yearInput">Year:</label>
					<input type="number" class="form-control" id="yearInput" name="yearInput" placeholder="2015">
				  </div>

				</div>
				<div class="modal-footer">
					<div class="btn-group btn-group-justified" role="group" aria-label="group button">
						<div class="btn-group" role="group">
							<button type="button" class="btn btn-default" data-dismiss="modal"  role="button">Close</button>
						</div>
						<div class="btn-group" role="group">
							<button type="submit" class="btn btn-default">Submit</button>
						</div>
					</div>
				</div>
				</form>
		</div>
	  </div>
	</div>
	
	<!-- Add section modal -->
	<div class="modal fade" id="addSection" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
	  <div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
				<h3 class="modal-title" id="lineModalLabel">Add Section</h3>
			</div>
			<div class="modal-body">
				
				<!-- content goes here -->
				<form method="post" id="reg-form">
				  <div class="form-group">
					<label for="yearInput">Year:</label>
					<div class="form-group">
					  <select id="selectedYear" name="selectedYear" class="form-control" id="sel1">
					  <?php
					  foreach($yearList as $years) {
						  echo "<option>". $years . "</option>";
					  }
					  ?>
					  </select>
					</div>
				  </div>
				  
				  <div class="form-group">
					<label for="sectionInput">Section:</label>
					<input type="text" class="form-control" id="sectionInput" name="sectionInput" placeholder="Section name">
				  </div>

				</div>
				<div class="modal-footer">
					<div class="btn-group btn-group-justified" role="group" aria-label="group button">
						<div class="btn-group" role="group">
							<button type="button" class="btn btn-default" data-dismiss="modal"  role="button">Close</button>
						</div>
						<div class="btn-group" role="group">
							<button type="submit" class="btn btn-default">Submit</button>
						</div>
					</div>
				</div>
				</form>
		</div>
	  </div>
	</div>
	
	<!-- Add video modal -->
	<div class="modal fade" id="addVideo" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
	  <div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
				<h3 class="modal-title" id="lineModalLabel">My Modal</h3>
			</div>
			<div class="modal-body">
				
				<!-- content goes here -->
				<form>
				  <div class="form-group">
					<label for="exampleInputEmail1">Email address</label>
					<input type="email" class="form-control" id="exampleInputEmail1" placeholder="Enter email">
				  </div>
				  <div class="form-group">
					<label for="exampleInputPassword1">Password</label>
					<input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
				  </div>
				  <div class="form-group">
					<label for="exampleInputFile">File input</label>
					<input type="file" id="exampleInputFile">
					<p class="help-block">Example block-level help text here.</p>
				  </div>
				  <div class="checkbox">
					<label>
					  <input type="checkbox"> Check me out
					</label>
				  </div>
				  <button type="submit" class="btn btn-default">Submit</button>
				</form>

			</div>
			<div class="modal-footer">
				<div class="btn-group btn-group-justified" role="group" aria-label="group button">
					<div class="btn-group" role="group">
						<button type="button" class="btn btn-default" data-dismiss="modal"  role="button">Close</button>
					</div>
					<div class="btn-group btn-delete hidden" role="group">
						<button type="button" id="delImage" class="btn btn-default btn-hover-red" data-dismiss="modal"  role="button">Delete</button>
					</div>
					<div class="btn-group" role="group">
						<button type="button" id="saveImage" class="btn btn-default btn-hover-green" data-action="save" role="button">Save</button>
					</div>
				</div>
			</div>
		</div>
	  </div>
	</div>
		
	<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">			
	<br>
	<br>
	<div class="video-container">
		<iframe id="videoframe" src="" name="videoframe" scrolling="no" frameborder="no" align="center" ></iframe>
		</div>
	</div>	<!--/.main-->
	
	<script>
	if (getParam("year") != "") {
		document.getElementById('videoframe').setAttribute('src', "videos" + location.search);
	}
	function updatePrams(year, name, id) {
		window.history.pushState(null, null, 'index?year=' + year + '&section=' + name + '&sid=' + id);
		document.getElementById('videoframe').setAttribute('src', "videos" + location.search);
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
