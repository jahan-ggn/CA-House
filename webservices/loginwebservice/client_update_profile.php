<?php

$con = mysqli_connect('localhost','root','','cahouse_db');

$id = $_REQUEST['id'];
$fname = $_REQUEST['fname'];
$lname = $_REQUEST['lname'];
$email = $_REQUEST['email'];
$mobile = $_REQUEST['mobile'];

$response = array();

$sql = "update client_master set fname = '$fname', lname = '$lname', email = '$email', mobile = '$mobile' where id = $id ";

$result = mysqli_query($con, $sql);

if($result)
{
	$response['status'] = 1;
	$response['message'] = "Profile Updated Successfully";
} 

else
{
	$response['status'] = 0;
	$response['message'] = "Profile Doesn't Updated Successfully";
} 

echo json_encode($response);
?>