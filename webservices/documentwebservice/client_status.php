<?php
$con = mysqli_connect('localhost', 'root', '', 'cahouse_db');

$request_id = $_REQUEST['request_id'];

$response = array();

$sql = "update request_client_master set status = 1 where request_id = '$request_id'";
$result = mysqli_query($con, $sql);

if($result){
	$response['status'] = 1;
	$response['message'] = "Status Fetch Successfully";
}

else{
	$response['status'] = 0;
	$response['message'] = "Something Went Wrong";
}
echo json_encode($response);

?>