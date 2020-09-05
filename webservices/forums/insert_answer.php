<?php

$connect=mysqli_connect("localhost","root","","cahouse_db");

$questionid=$_REQUEST['questionid'];
$cid=$_REQUEST['cid'];
$answer=$_REQUEST['answer'];
$answerby=$_REQUEST['answerby'];


$query = "insert into forum_answer_master(questionid, cid, answer, answerby, date) values('$questionid', '$cid', '$answer', '$answerby', CURRENT_TIMESTAMP)";

$response=array();

if(mysqli_query($connect,$query))
{
	 $response['status']=1;
	 $response['message']="Answer Added Successfully";
}
else
{
	 $response['status']=0;
	 $response['message']="Something Went Wrong";
}

echo json_encode($response);


?>
