<?php
$con = mysqli_connect('localhost', 'root', '', 'cahouse_db');
$cid = $_REQUEST['cid'];
$response = array();
$sql = "select filename from upload_client_master where cid = '$cid'";
$result = mysqli_query($con, $sql);
	if($result->num_rows > 0){
		$response['upload_array'] = array();

		while($row = $result->fetch_array(MYSQLI_BOTH)){
			$upload = array();
			$upload['filename'] = $row[0];
			array_push($response['upload_array'],$upload);
		}
		$response['status'] = 1;
		$response['message'] = "File Name Fetched Successfully";
	}
	else{
		$response['status'] = 0;
		$response['message'] = "Something Went Wrong";
	}
	
	echo json_encode($response);
?>