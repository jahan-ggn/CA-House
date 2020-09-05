<?php

	$con = mysqli_connect("localhost","root","","cahouse_db");

	$title = $_POST['news_title'];
	$description = $_POST['news_description'];
	$status = $_POST['news_status'];

	$response = array();

	$sql = "insert into news_master(news_title, news_description, news_date, news_time, news_status) values ('$title', '$description', CURRENT_TIMESTAMP, NOW(), '$status')";

	$result = mysqli_query($con,$sql);

	if($result){
		$response['status'] = 1;
		$response['message'] = "News Added Successfully";
}

	else{
		$response['status'] = 0;
		$response['message'] = "Something Went Wrong";
	}

	echo json_encode($response);
?>