<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Login</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
  <div class="main-container">
    <form id="fors" method="get" action="verify">
      <div class="form-group">
        <div class="input-group">
          <div class="input-group-prepend">
            <span class="input-group-text">
              <i class="fas fa-user"></i>
            </span>
          </div>
          <input type="text" class="form-control" name="userDisplayName" placeholder="Enter name">
        </div>
      </div>
      <div class="form-group">
        <div class="input-group">
          <div class="input-group-prepend">
            <span class="input-group-text">
              <i class="fas fa-lock"></i>
            </span>
          </div>
          <input type="password" id="pass" class="form-control" name="userPassword" placeholder="Enter password">
        </div>
        <div class="form-check">
          <input type="checkbox" class="form-check-input" onclick="toggleVisibility()">
          <label class="form-check-label">Show Password</label>
        </div>
      </div>
      <div class="form-group">
        <input type="submit" class="btn btn-primary" value="Login">
        <button onclick="forgotPassword()" class="text-button">Forgot Password</button>
      </div>
    </form>
  </div>
</body>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  <script>
    function toggleVisibility() {  
      var getPassword = document.getElementById("pass");  
      if (getPassword.type === "password") {  
        getPassword.type = "text";  
      } else {  
        getPassword.type = "password";  
      }  
    }  

    function forgotPassword() {
      var form = document.getElementById("fors").action="getEmail";
      form.submit();
    }
  </script>
  
  <style>
    body {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }
    .text-button {
      background: none;
      border: none;
      font: inherit;
      cursor: pointer;
      color: blue;
      text-decoration: none;
      width: 100%;
      padding: 10px;
      font-size: 16px;
      margin-top: 10px;
      text-align: center;
      outline: none !important;
    }
    .main-container {
      width: 300px;
      padding: 20px;
      border: 1px solid #ccc;
      border-radius: 5px;
      background-color: #f9f9f9;
    }
    .input-group-prepend .input-group-text {
      border-top-left-radius: 5px;
      border-bottom-left-radius: 5px;
    }
    .input-group-text {
      background-color: #f9f9f9;
    }
    input[type="text"],
    input[type="password"] {
      border-top-right-radius: 5px;
      border-bottom-right-radius: 5px;
    }
    input[type="text"],
    input[type="password"] {
      width: 100%;
      padding: 10px;
      margin-bottom: 10px;
      font-size: 16px;
    }
    input[type="checkbox"] {
      margin-top: 10px;
    }
    input[type="submit"],
    a {
      display: block;
      width: 100%;
      padding: 10px;
      font-size: 16px;
      margin-top: 10px;
      text-align: center;
    }
  </style>
</html>