<?php
$con = mysqli_connect('localhost','root','','cahouse_db');

$fname = $_POST['fname'];
$lname = $_POST['lname'];
$email = $_POST['email'];
$mobile = $_POST['mobile'];
$password = $_POST['password'];


$response=array();

$query = "insert into client_master(fname, lname, email, mobile, password) values('$fname', '$lname', '$email', '$mobile', md5('$password'))";

$result = mysqli_query($con,$query);


if($result)
{
	$response['status'] = 1;
	$response['message'] = "User Created Successfully";
}
else
{
	$response['status'] = 0;
	$response['message'] = "Something Went Wrong";
}

echo json_encode($response);
?>