<?php

$con = mysqli_connect('localhost','root','','cahouse_db'); 

$response=array();
$sql="select * from feedback_master ORDER BY feedback_id DESC";

$result = mysqli_query($con, $sql);

	if($result->num_rows > 0){
		$response['feedback_array'] = array();

		while($row = $result->fetch_array(MYSQLI_BOTH)){
			$feed = array();
			$feed['feedback_id'] = $row[0];
			$feed['name'] = $row[1];
			$feed['email'] = $row[2];
			$feed['feedbackmsg'] = $row[3];
			$feed['feedbackdate'] = $row[4];
			$feed['feedbacktime'] = $row[5];
			array_push($response['feedback_array'],$feed);
		}

		$response['status'] = 1;
		$response['message'] = "Feedbacks Fetched Successfully";
	}

	else{
		$response['status'] = 0;
		$response['message'] = "Something Went Wrong";
	}

	echo json_encode($response);

?>	