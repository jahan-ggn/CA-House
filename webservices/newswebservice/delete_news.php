<?php

$con = mysqli_connect("localhost","root","","cahouse_db");

$id = $_POST['news_id'];

$response = array();

$sql = "delete from news_master where news_id = $id";


$result = mysqli_query($con, $sql);


if($result){
	$response['status'] = 1;
	$response['message'] = "News Deleted Successfully";
}

else{
	$response['status'] = 0;
	$response['message'] = "Something Went Wrong";
}

echo json_encode($response);

?>