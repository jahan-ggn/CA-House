<?php

$con = mysqli_connect('localhost','root','','cahouse_db');

$response = array();

$sql = "select * from document_master";

$result = mysqli_query($con, $sql);

if($result->num_rows > 0){

	$response['documents_array'] = array();

	while($row = $result->fetch_array(MYSQLI_BOTH)){
		$doc = array();
		$doc['document_id'] = $row[0];
		$doc['document_name'] = $row[1];
		array_push($response['documents_array'],$doc);

	}

	$response['status'] = 1;
	$response['message'] = "Document Fetched Successfully";
}

else{

	$response['status'] = 0;
	$response['message'] = "Something Went Wrong";
}

echo json_encode($response);
?>