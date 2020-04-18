<%--
  Created by IntelliJ IDEA.
  User: rahuljanga
  Date: 11/25/19
  Time: 12:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="HTML" %>
<html>
<head>
        <link rel="stylesheet" type="text/css" href="StyleSheets/stylelogin.css">
        <link rel="stylesheet" type="text/css" href="StyleSheets/grid.css">
        <link rel="stylesheet" type="text/css" href="StyleSheets/normalize.css">
        <link href="https://fonts.googleapis.com/css?family=Lato:100,300,300i,400&display=swap" rel="stylesheet">
        <title>StudentPool</title>

</head>
<body>
<section class="section-form">
    <form id="loginform"action="" method="post">
        <div class="createform">
            <div class="row">
                <h2>Sign Up!</h2>
            </div>
            <div class="container">
                <input id="user_id" type="text" placeholder="Username@ucmo.edu" name="user_id" required>
                <input id="user_password" type="password" placeholder="Password" name="user_password" required>
                <input id="re-password" type="password" placeholder="Confirm Password" name="re-password" required>
            </div>
            <div class="container login">
                <button type="submit" value="submit" onclick="validatePassword()">Create Account</button>
            </div>

            <div class="container butn" style="background-color:#f1f1f1">
                <button type="reset" class="cancelbtn">Reset</button>
                <button type="button" class="login-btn" ><a href="http://localhost:8080/StudentPoolWeb_war/login.jsp">Login</a></button>
            </div>
            <pre id="output"></pre>
        </div>
    </form>
</section>

<script>
    document.getElementById('createform').addEventListener('submit',addPost)
    function addPost(event) {
        //https://stackoverflow.com/questions/28065963/how-to-handle-cors-using-jax-rs-with-jersey/28067653#28067653
        //above is stackoverflow answer on preflight
        event.preventDefault();

        let user_id=document.getElementById('user_id').value;
        let user_password=document.getElementById('user_password').value;

        const myPost={
            user_id: user_id,
            user_password: user_password
        }
        fetch('http://localhost:8080/StudentPoolWeb_war/webapi/users/createaccount', {
            method: 'POST',

            headers: {
                'Content-Type': 'application/json',
                'Origin':'http://127.0.0.1:51700/Login/login.html'
            },
            body: JSON.stringify(myPost)
        })
            .then((res) => {
                if(res.ok){
                    return res.json()
                }else{
                    return Promise.reject({status: res.status, statusText:res.statusText})
                }
            })
            .then((data) => window.location.replace("http://localhost:8080/StudentPoolWeb_war/login.jsp"))
            .catch(err=>console.log('Error message:',err.statusText))
    }
</script>

</body>
</html>
