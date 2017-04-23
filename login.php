<?php
    $database="id1434324_myandroidapp";
    $servername = "localhost";
    $user = "id1434324_victorgz";
    $password = "malaga123";
   
    $con = mysqli_connect($servername, $user, $password,$database);
    
    $email = $_POST["email"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM User WHERE email = '$email' AND password = '$password'");
    
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userName, $name, $email, $password);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
        $response["userName"] = $userName;
        $response["name"] = $name;
        $response["email"] = $email;
        $response["password"] = $password;
    }
    
    echo json_encode($response);
?>
