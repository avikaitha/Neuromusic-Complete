<?php
    ini_set('display_errors',1);
    ini_set('display_startup_errors',1);
    error_reporting(E_ALL);
    
    $con = mysql_connect("localhost","root","");
    if(!$con)
    {
        die('Could not connect: '.mysql_error());
    }
    mysql_select_db("EEG",$con);
    
	if (!empty($_POST['fd']) && !empty($_POST['mood'])) {
        $fd = $_POST['fd'];
        $mood = $_POST['mood'];
        
        echo "INSERT INTO Raw_FD(`_id`,`FD`,`Mood`) VALUES(NULL,".$fd.",".$mood.")";
        
        mysql_query("INSERT INTO Raw_FD(`_id`,`FD`,`Mood`) VALUES(NULL,".$fd.",\"".$mood."\")");
    
	}
    
    mysql_close($con);
    
?>

