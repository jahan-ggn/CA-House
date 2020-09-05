<?php
$connect=mysqli_connect("localhost","root","","cahouse_db");
$query=mysqli_query($connect,"select * from forum_question_master order by id DESC");
$response=array();


while($result=mysqli_fetch_assoc($query))
{
	array_push($response,array("id"=>$result['id'],"cid"=>$result['cid'],"question"=>$result['question'],"questionby"=>$result['questionby'],"date"=>$result['date']));
}
echo json_encode(array("response"=>$response));
?>