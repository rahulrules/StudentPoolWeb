<%--
  Created by IntelliJ IDEA.
  User: rahuljanga
  Date: 11/27/19
  Time: 2:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="StyleSheets/stylereguser.css">
    <link rel="stylesheet" type="text/css" href="StyleSheets/grid.css">
    <link rel="stylesheet" type="text/css" href="StyleSheets/normalize.css">
    <link href="https://fonts.googleapis.com/css?family=Lato:100,300,300i,400&display=swap" rel="stylesheet">
    <title>StudentPool</title>
</head>
<body>

<Section class="section-tab">

    <div class="row">

        <ul class="main-nav">
            <li><a id="about"href="#" onclick="clearBox('content')">About</a></li>
            <li><a id="ro" href="#">Rides Offered </a></li>
            <li><a href="#">Rides Requested </a></li>
            <li><a href="#">My Account </a></li>
        </ul>
    </div>

</Section>
<section class="section-content">
    <div class="row">
        <div id="content" >
        </div>
    </div>
    <section class="section-data">
        <div class="row">
            <div id="ridesdata">

            </div>
        </div>


    </section>

</section>



<script src="Javascriptfiles/about.js"></script>
<script src="Javascriptfiles/ridesoffered.js"></script>

</body>
</html>
