<?php
    include "../../notify.php";
  	$con = mysqli_connect('localhost','root','','cahouse_db');

  	$file_path = "/Applications/XAMPP/htdocs/webservices/upload/fileuploads/uploads/";
     
	 $userid = $_REQUEST["userid"];
   $ca_id = $_REQUEST['ca_id'];
	 $filename = $_REQUEST["filename"];
   $uploadedby = $_REQUEST["uploadedby"];

	 $response=array();
	 $sql = "insert into upload_admin_master(cid, ca_id, filename, uploadedby, date) values('$userid', '$ca_id', '$filename', '$uploadedby', CURRENT_TIMESTAMP)";
	 $result = mysqli_query($con,$sql);

    $file_path = $file_path . $_FILES['uploaded_file']['name'];
    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path) ){
        echo "success";
        $sql1 = "select gcmtokenid from client_master where id = '$userid'";
        $res_notify = mysqli_query($con, $sql1);
        $row_notify = mysqli_fetch_assoc($res_notify);
        $token_id = $row_notify['gcmtokenid'];
        sendnotification($token_id,$uploadedby,"Documents Uploaded");
      }

  else{
        echo "fail";
    }
 ?>