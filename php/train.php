<?php
    
    if (!empty($_POST['raw']) && !empty($_POST['mood'])) {
        $raw = $_POST['raw'];
        $mood = $_POST['mood'];
        $input_lines = implode(",", $raw);
        ini_set('display_errors',1);
        ini_set('display_startup_errors',1);
        error_reporting(E_ALL);
        echo exec('sudo -u avinash /Applications/MATLAB_R2015b.app/bin/matlab -nodesktop -nosplash -r "cd(\'/Users/avinash/Documents/MATLAB/\');train('.escapeshellarg(serialize($raw)).',\''.$mood.'\');quit"',$ret);
     
        
    }
   
    
    
	
    
?>

