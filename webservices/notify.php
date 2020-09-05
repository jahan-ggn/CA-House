<?php

function sendnotification($token,$title,$con)
{


 $url='https://fcm.googleapis.com/fcm/send';	
	define( 'API_ACCESS_KEY', 'AIzaSyC_5-wA3ocA7pUa5gfzXdy2Fv17fapgSJg');
	$registrationIds=array($token);
#prep the bundle
     $msg = array
          (
		'body' 	=> $con,
		'title'	=> $title,
             	'icon'	=> 'myicon',/*Default Icon*/
              	'sound' => 'mySound'/*Default sound*/
          );
	$fields = array
			(
				'registration_ids' => $registrationIds,
				'data'	=> $msg
			);
	
	
	$headers = array
			(
				'Authorization: key=' . API_ACCESS_KEY,
				'Content-Type: application/json'
			);
#Send Reponse To FireBase Server	
		$ch = curl_init();
		curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
		curl_setopt( $ch,CURLOPT_POST, true );
		curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
		curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
		curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
		curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
		$result = curl_exec($ch );
		curl_close( $ch );
#Echo Result Of FireBase Server
echo $result;
 }
?>