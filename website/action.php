<?php
// database connection code
if(isset($_POST['txtName']))
{
	$username = $_POST['username'];
	$password = $_POST['password'];
	// $con = mysqli_connect('localhost', 'database_user', 'database_password','database');
	$con = mysqli_connect("208.109.40.126", $username, $password, 'DuelDebatabase', '3306');

	$operation = $_POST['operation'];

	$sql = "";

	if($operaton == 'AddCandidate') {
		$candidateName = $_POST['candidateName'];
		$candidateBeliefs = $_POST['candidateBeliefs'];
		$candidateImage = $_POST['candidateImage'];

		// TODO: Write INSERT Statement
		// database insert SQL code example
		//	$sql = "INSERT INTO `tbl_contact` (`Id`, `fldName`, `fldEmail`, `fldPhone`, `fldMessage`) VALUES ('0', '$txtName', '$txtEmail', '$txtPhone', '$txtMessage')";
	}
	else if($operaton == 'AddScore') {
		$candidate = $_POST['candidates'];
		$scoreDesc = $_POST['scoreDesc'];
		$scoreAmount = $_POST['scoreAmount'];

		// TODO: Write INSERT Statement
	}
	else if ($operation == 'RemoveCandidate') {
		// TODO: Write DELETE Statement
		$candidate = $_POST['candidates'];
	}


	// insert in database
	$rs = mysqli_query($con, $sql);
}
?>
