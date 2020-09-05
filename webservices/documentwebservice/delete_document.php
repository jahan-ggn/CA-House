<?php

$con = mysqli_connect('localhost','root','','cahouse_db');

$id = $_POST['document_id'];

$response = array();

$sql = "delete from document_master where document_id = $id";

$result = mysqli_query($con, $sql);

if($result){

	$response['status'] = 1;
	$response['message'] = "Document Deleted Successfully";
}

else{

	$response['status'] = 0;
	$response['message'] = "Something Went Wrong";
}

echo json_encode($response);

?>