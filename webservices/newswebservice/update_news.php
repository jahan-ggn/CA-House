<?php

$con = mysqli_connect("localhost", "root", "", "cahouse_db");

$id = $_POST['news_id'];
$title = $_POST['news_title'];
$description = $_POST['news_description']; 
$status = $_POST['news_status'];


$response=array();

$sql = "update news_master set news_title = '$title' ,news_description = '$description', news_date = CURRENT_TIMESTAMP, news_status = '$status' where news_id = $id ";

$result = mysqli_query($con, $sql);

if($result)
{
	$response["status"] = 1;
	$response["message"] = "News Edited Successfully";
}
else
{
	$response["status"] = 0;
	$response["message"] = "Something Went Wrong";
}

echo json_encode($response);

?>