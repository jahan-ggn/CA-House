<?php
$con = mysqli_connect("localhost", "root", "", "cahouse_db");

$mobile = $_REQUEST['mobile'];

$response = array();

$sql = "select mobile from client_master where mobile = '$mobile'";

$result = mysqli_query($con, $sql);
if($result->num_rows > 0){
	$response['status'] = 1;
	$response['message'] = "Mobile Numbers Fetch Successfully";
}
else{
	$response['status'] = 0;
	$response['message'] = "Something Went Wrong";
}

echo json_encode($response)
?>