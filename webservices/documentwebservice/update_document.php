<?php

$con = mysqli_connect("localhost", "root", "", "cahouse_db");

$id = $_REQUEST['document_id'];
$document_name = $_REQUEST['document_name'];

$response=array();

$sql = "update document_master set document_name = '$document_name' where document_id = $id";


$result = mysqli_query($con, $sql);

if($result)
{
	$response["status"] = 1;
	$response["message"] = "Document Edited Successfully";
}
else
{
	$response["status"] = 0;
	$response["message"] = "Something Went Wrong";
}

echo json_encode($response);

?>