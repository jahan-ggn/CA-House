<?php


$con = mysqli_connect('localhost','root','','cahouse_db');

$id = $_POST['id'];
$old_password = $_POST['old_password'];
$new_password = $_POST['new_password'];

$response = array();

$sql = "select * from ca_master where id = $id and password='$old_password'";
$result = mysqli_query($con,$sql);
$row = mysqli_num_rows($result);
if($row>0)
{
	$sql = "update ca_master set password='$new_password' where id=$id";
	$result = mysqli_query($con,$sql);

	if($result)
	{
		$response["status"] = 1;
		$response["message"] = "Password Changed Successfully";
	}

	else
	{
		$response["status"] = 0;
		$response["message"] = "Something Went Wrong";
	}
}
else
{
	$response["status"] = 0;
	$response["message"] = "Wrong Password";
}
echo json_encode($response);
?>