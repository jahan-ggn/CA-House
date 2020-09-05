<?php

$con = mysqli_connect('localhost','root','','cahouse_db');

$document_name = $_POST['document_name'];

$response = array();

$sql = "insert into document_master(document_name) values('$document_name')";

$result = mysqli_query($con, $sql);

if($result){

	$response['status'] = 1;
	$response['message'] = "Document Inserted Successfully";
}

else{

	$response['status'] = 0;
	$response['message'] = "Something Went Wrong";
}

echo json_encode($response);

?>