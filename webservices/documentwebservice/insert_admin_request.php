<?php
include "../notify.php";
$con = mysqli_connect('localhost','root','','cahouse_db');

$document_id = json_decode($_REQUEST['document_id'],true);
$ca_id = $_REQUEST['ca_id'];
$requestby = $_REQUEST['requestby'];
$cid = $_REQUEST['cid'];

$response = array();


foreach ($document_id as $key) {
	$docid=$key['doc_id'];

	$sql = "insert into request_admin_master(document_id, ca_id, cid, date, requestby) values ('$docid', '$ca_id', '$cid', CURRENT_TIMESTAMP, '$requestby')";

	$result = mysqli_query($con, $sql);

	}

	if($result)
	{
		$response['status'] = 1;
		$response['message'] = "Request Added Successfully";
	    echo json_encode($response);

		$sql1 = "select gcmtokenid from client_master where id = '$cid'";
		$res_notify = mysqli_query($con, $sql1);
		$row_notify = mysqli_fetch_assoc($res_notify);
		$token_id = $row_notify['gcmtokenid'];
		sendnotification($token_id,$requestby,"Request For Documents");
	}

	else
	{
		$response['status'] = 0;
		$response['message'] = "Something Went Wrong";
        echo json_encode($response);
	}
?>