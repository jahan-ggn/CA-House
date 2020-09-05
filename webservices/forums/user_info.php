<?php
include("connect.php");

$query=mysqli_query($connect,"select * from user");
$response=array();


while($result=mysqli_fetch_assoc($query))
{
	array_push($response,array("id"=>$result['id'],"username"=>$result['username'],"city"=>$result['city']));
}

echo json_encode(array("response"=>$response));
?>
