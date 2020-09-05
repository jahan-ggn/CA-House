<?php

$con = mysqli_connect("localhost","root","","cahouse_db");

$response = array();

$mobile=$_REQUEST['mobile'];
$gcmToken=$_REQUEST['token'];

$sql = "select * from ca_master where mobile = '$mobile'";
$result = mysqli_query($con, $sql);
if(mysqli_num_rows($result)>0)
{
	
	$q_new = "update ca_master set gcmtokenid='$gcmToken' where mobile = '$mobile'";
	$result_changed = mysqli_query($con, $q_new);
	if($result_changed){
		$response["status"] = 1;
		$response["message"] = "GCM Token Updated Successfully";
	}
	else
	{
		$response["status"];
		$response["message"] = "Error occurred while update GCM Token id... ";
	}
}
else
{
	$response["status"] = 0;
	$response["message"] = "Error occurred while update GCM Token id... ";
}
echo json_encode($response);
	
?>

