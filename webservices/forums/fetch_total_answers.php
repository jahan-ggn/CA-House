<?php

$con = mysqli_connect('localhost','root','','cahouse_db');

$questionid = $_REQUEST['questionid'];

$response = array();

$sql = "select * from forum_answer_master where questionid = '$questionid'";

$result = mysqli_query($con, $sql);

$row = mysqli_num_rows($result);

if($row){
	$response['status'] = 1;
	$response['row'] = $row;
}

else{
	$response['status'] = 0;
	$response['row'] = $row;
}

echo json_encode($response);	

?>