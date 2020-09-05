<?php

$connect=mysqli_connect("localhost","root","","cahouse_db");

$cid = $_REQUEST['cid'];
$question = $_REQUEST['question'];
$questionby = $_REQUEST['questionby'];

$query = "insert into forum_question_master(cid, question, questionby, date) values('$cid', '$question', '$questionby', CURRENT_TIMESTAMP)"; 

$response=array();

if(mysqli_query($connect,$query))
{
	 $response['status']=1;
	 $response['message']="Question Added Successfully";
}
else
{
	 $response['status']=0;
	 $response['message']="Something Went Wrong";
}

echo json_encode($response);


?>
