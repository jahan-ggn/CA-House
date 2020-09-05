<?php


$con = mysqli_connect('localhost','root','','cahouse_db');

$mobile = $_POST['mobile'];
$new_password = $_POST['password'];

$response = array();

$sql = "select mobile from client_master where mobile = '$mobile'";
$result = mysqli_query($con,$sql);
$row = mysqli_num_rows($result);
if($row>0)
{
	$sql = "update client_master set password='$new_password' where mobile = '$mobile'";
	$result = mysqli_query($con,$sql);

	if($result)
	{
		$response["status"] = 1;
		$response["message"] = "Password Changed Successfully";
	}

	else
	{
		$response["status"] = 0;
		$response["message"] = "Password Doesn't Changed Successfully";
	}
}
else
{
	$response["status"] = 0;
	$response["message"] = "Wrong Password";
}
echo json_encode($response);
?>