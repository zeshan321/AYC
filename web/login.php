<?php
   include 'functions/cURLFunctions.php';
   session_start();
   
   if(isset($_SESSION['login_user'])) {
   	header("location: index");
   }
 ?>

<!DOCTYPE html>
<html>
   <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <title>AYC Login</title>
      <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
      <link href="bootstrap/bootstrap.min.css" rel="stylesheet">
      <link href="css/login.css" rel="stylesheet">
      <!--[if lt IE 9]>
      <script src="js/html5shiv.js"></script>
      <script src="js/respond.min.js"></script>
      <![endif]-->
   </head>
   
   <body>
      <div class="modal fade in" id="login-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;" data-backdrop="static" data-keyboard="false">
         <div class="modal-dialog">
            <div class="loginmodal-container">
               <h1>Login</h1>
               <br>
               <form role="form" action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>" method="post">
				  <fieldset>
					  <input type="text" name="user" placeholder="Username">
					  <input type="password" name="pass" placeholder="Password">
					  <input type="submit" name="login" class="login loginmodal-submit" value="Login">
				  </fieldset>
               </form>
			   
			  <?php
				if (isset($_SESSION['login_status']) && $_SESSION['login_status'] == false) {
					session_unset();
					echo "<div class=\"col-md-12\"> <div class=\"update-nag\"><div class=\"update-text\">The username or password you entered is incorrect.</div> </div></div>\n";
				}
				?>
            </div>
         </div>
      </div>
	  
      <?php
         if (isset($_POST['user']) && isset($_POST['pass'])) {
         	$user = $_POST['user'];
         	$pass = $_POST['pass'];
         	$login = login($user, $pass);
         	
         	if (loginCheck($login) == true && isAdmin($login) == true) {
         		$_SESSION["login_user"] = $user;
				$_SESSION["login_status"] = true;
				
				header('Location: index');
				exit;
         	} else {
         		$_SESSION["login_status"] = false;
				
				header('Location: ?');
				exit;
         	}
         }
         ?>
		 
      <script src="js/jquery-1.11.1.min.js"></script>
      <script src="js/bootstrap.min.js"></script>
      <script>
		 $('#login-modal').modal('show');
      </script>	
   </body>
</html>