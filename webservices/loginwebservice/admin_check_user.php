<?php

$con = mysqli_connect('localhost','root','','cahouse_db');

$mobile = $_REQUEST['mobile'];
$password = $_REQUEST['password'];
$gcm = $_REQUEST['gcm'];

$response = array();

$sql = "select * from ca_master where mobile ='$mobile' && password = '$password'";
$result = mysqli_query($con,$sql);
$row = mysqli_fetch_array($result);

if($row[4])
{
	$sql1 = "update ca_master set gcmtokenid = '$gcm' where mobile='$mobile'";
	mysqli_query($con, $sql1);
	$response['status'] = 1;
	$response['user_id'] = $row[0];
	$response['fname'] = $row[1];
	$response['lname'] = $row[2];
	$response['gcmtokenid'] = $row[6];
	$response['message'] = "login successful";
}
else
{
	$response['status'] = 0;
	$response['message'] = "invalid mobile number or password";
}

echo json_encode($response);
?>