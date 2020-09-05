<?php

$con = mysqli_connect('localhost','root','','cahouse_db'); 

$name = $_POST['name'];
$email = $_POST['email'];
$feedbackMsg = $_POST['feedbackMsg'];

$response = array();
$query="insert into feedback_master(name, email, feedbackmsg, feedbackdate, feedbacktime) values('$name', '$email', '$feedbackMsg', CURRENT_TIMESTAMP, NOW())";

$result = mysqli_query($con,$query);


if($result)
{
	$response['status'] = 1;
	$response['message'] = "Feedback Sent Successfully";
}
else
{
	$response['status'] = 0;
 	$response['message'] = "Something Went Wrong";
}

echo json_encode($response);

?>	