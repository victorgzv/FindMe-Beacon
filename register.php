<?php
    $database="id1434324_myandroidapp";
    $servername = "localhost";
    $user = "id1434324_victorgz";
    $password = "malaga123";
   
    $con = mysqli_connect($servername, $user, $password,$database);
    
    
    if(!empty($_POST["userName"]) && !empty($_POST["Name"]) && !empty($_POST["email"]) && !empty($_POST["password"])   ){


    $userName = $_POST["userName"];
    $surname = $_POST["Name"];
    $email = $_POST["email"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "INSERT INTO User (userName, name, email, password) VALUES ('$userName','$surname', '$email','$password')");
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
    }else{
          $response["success"] = false;  
          echo json_encode($response);
    }
    
?>