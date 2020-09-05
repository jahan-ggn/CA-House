<?php
$con = mysqli_connect('localhost', 'root', '', 'cahouse_db');

$cid = $_REQUEST['cid'];

$response = array();

$sql = "delete from client_master where id = $cid";
$result = mysqli_query($con, $sql);

if($result){
	$response['status'] = 1;
	$response['message'] = "User Deleted Successfully";
}

else{
	$response['status'] = 0;
	$response['message'] = "Something Went Wrong";
}

echo json_encode($response);
?>