<?php

$con = mysqli_connect('localhost','root','','cahouse_db');

$response = array();

$sql = "select id, fname, lname from client_master";

$result = mysqli_query($con, $sql);

if($result->num_rows > 0){

	$response['client_array'] = array();

	while ($row = $result->fetch_array(MYSQLI_BOTH)) {
		$client = array();
		$client['id'] = $row[0];
		$client['fname'] = $row[1];
		$client['lname'] = $row[2];
		array_push($response['client_array'], $client);

	}

		$response['status'] = 1;
		$response['message'] = "Clients Fetched Successfully";
	}

	else{

		$response['status'] = 0;
		$response['message'] = "Something Went Wrong";
	}

echo json_encode($response);
?>