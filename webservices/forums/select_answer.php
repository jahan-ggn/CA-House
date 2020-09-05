<?php
$connect=mysqli_connect("localhost","root","","cahouse_db");
$passid=$_REQUEST['passid'];
$sql = "select * from forum_answer_master where questionid='".$passid."' order by id DESC";
$result = mysqli_query($connect, $sql);
$response=array();

if($result->num_rows > 0){
		$response['forum_array'] = array();

		while($row = $result->fetch_array(MYSQLI_BOTH)){
			$forum = array();
			$forum['id'] = $row[0];
			$forum['question_id'] = $row[1];
			$forum['cid'] = $row[2];
			$forum['answer'] = $row[3];
			$forum['answerby'] = $row[4];
			$forum['date'] = $row[5];
			array_push($response['forum_array'],$forum);
		}

		$response['status'] = 1;
		$response['message'] = "Answers Fetched Successfully";
	}

	else{
		$response['status'] = 0;
		$response['message'] = "Something Went Wrong";
	}

echo json_encode($response);
?>
