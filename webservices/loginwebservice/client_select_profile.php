<?php

$con = mysqli_connect('localhost', 'root', '', 'cahouse_db');

$id = $_POST['id'];

$response = array();

$sql = "select * from client_master where id = $id";

$result = mysqli_query($con, $sql);

if($result->num_rows > 0){
		$response['client_array'] = array();

		while($row = $result->fetch_array(MYSQLI_BOTH)){
			$client = array();
			$client['id'] = $row[0];
			$client['fname'] = $row[1];
			$client['lname'] = $row[2];
			$client['email'] = $row[3];
			$client['mobile'] = $row[4];
			$client['password'] = $row[5];
			array_push($response['client_array'],$client);
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