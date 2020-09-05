<?php
    include "../../notify.php";
  	$con = mysqli_connect('localhost','root','','cahouse_db');

  	$file_path = "/Applications/XAMPP/htdocs/webservices/upload/fileuploads/uploads/";
     
	 $userid = $_REQUEST["userid"];
	 $filename = $_REQUEST["filename"];
   $uploadedby = $_REQUEST["uploadedby"];

	 $response=array();
	 $sql = "insert into upload_client_master(cid, filename, uploadedby, date) values('$userid', '$filename', '$uploadedby', CURRENT_TIMESTAMP)";
	 $result = mysqli_query($con,$sql);

    $file_path = $file_path . $_FILES['uploaded_file']['name'];
    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path) ){
        echo "success";
        $sql1 = "select gcmtokenid from ca_master";
        $res_notify = mysqli_query($con, $sql1);
        $row_notify = mysqli_fetch_assoc($res_notify);
        $token_id = $row_notify['gcmtokenid'];
        sendnotification($token_id,$uploadedby,"Documents Uploaded");
      }

    else {
        echo "fail";
    }
 ?>