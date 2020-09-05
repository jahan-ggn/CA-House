<?php

	$con = mysqli_connect("localhost","root","","cahouse_db");

	$response = array();

	$sql = "select cid, request_id, document_id, requestby, status from request_admin_master ORDER BY request_id DESC";

	$result = mysqli_query($con, $sql);

	if($result->num_rows > 0){
		$response['request_array'] = array();

		while($row = $result->fetch_array(MYSQLI_BOTH)){
			$request = array();
			$request['cid'] = $row[0];
			$request['request_id'] = $row[1];
			$request['document_id'] = $row[2];
			$request['requestby'] = $row[3];
			$request['status'] = $row[4];
			$sql1 = "select * from document_master where document_id = '$row[2]'";
			$result1 = mysqli_query($con, $sql1);
			$row1 = $result1->fetch_array(MYSQLI_BOTH);
			$request['document_name'] = $row1[1]; 
			
			array_push($response['request_array'],$request);
		}
		$response['status'] = 1;
		$response['message'] = "Documents Requests Fetched Successfully";
	}

	else{
		$response['status'] = 0;
		$response['message'] = "Something Went Wrong";
	}

	echo json_encode($response);

?>