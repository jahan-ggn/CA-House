<?php

	$con = mysqli_connect("localhost","root","","cahouse_db");


	$response = array();

	$sql = "select * from news_master ORDER BY news_id DESC";

	$result = mysqli_query($con, $sql);

	if($result->num_rows > 0){
		$response['news_array'] = array();

		while($row = $result->fetch_array(MYSQLI_BOTH)){
			$news = array();
			$news['news_id'] = $row[0];
			$news['news_title'] = $row[1];
			$news['news_description'] = $row[2];
			$news['news_date'] = $row[3];
			$news['news_time'] = $row[4];
			$news['news_status'] = $row[5];
			array_push($response['news_array'],$news);
		}

		$response['status'] = 1;
		$response['message'] = "News Fetched Successfully";
	}

	else{
		$response['status'] = 0;
		$response['message'] = "Something Went Wrong";
	}

	echo json_encode($response);

?>