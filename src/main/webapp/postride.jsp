<%--
  Created by IntelliJ IDEA.
  User: rahuljanga
  Date: 11/28/19
  Time: 10:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <link rel="stylesheet" type="text/css" href="StyleSheets/stylerides.css">
    <link rel="stylesheet" type="text/css" href="StyleSheets/grid.css">
    <link rel="stylesheet" type="text/css" href="StyleSheets/normalize.css">
    <link href="https://fonts.googleapis.com/css?family=Lato:100,300,300i,400&display=swap" rel="stylesheet">

</head>
<body>

<section class="section-form">
    <div class="row">
        <h2>Offer New Ride</h2>
    </div>
    <div class="row">
        <form  id="addPostForm" action="#" class="contact-form">
            <div class="row">
                <div class="col span-1-of-3">
                    <label for="end_to">To Place</label>
                </div>
                <div class="col span-2-of-3 ">
                    <input type="text" name="end_to" id="end_to" placeholder="Destination" required>
                </div>
            </div>
            <div class="row">
                <div class="col span-1-of-3">
                    <label for="start_from">From Place</label>
                </div>
                <div class="col span-2-of-3 ">
                    <input type="text" name="start_from" id="start_from" placeholder="Origin" required>
                </div>
            </div>

            <div class="row">
                <div class="col span-1-of-3">
                    <label for="ride_date">Ride Date</label>
                </div>
                <div class="col span-2-of-3 ">
                    <input type="date" name="ride_date" id="ride_date" placeholder="Pick a Date" required min="2019-12-01" max="2020-12-31">
                </div>
            </div>

            <div class="row">
                <div class="col span-1-of-3">
                    <label for="ride_time">Ride Time</label>
                </div>
                <div class="col span-2-of-3 ">
                    <input type="time" name="ride_time" id="ride_time" placeholder="HH:MM" required min="00:00" max="23:59" step="00:10">
                </div>
            </div>

            <div class="row">
                <div class="col span-1-of-3">
                    <label for="slots_offered">Slots Offered</label>
                </div>
                <div class="col span-2-of-3 ">
                    <input type="number" name="slots_offered" id="slots_offered" placeholder="No." required min="0" max="10" value="1">
                </div>
            </div>

            <div class="row">
                <div class="col span-1-of-3">
                    <label for="instructions">Instructions</label>
                </div>
                <div class="col span-2-of-3 ">
                    <textarea type="message" placeholder="Your message" name="instructions" id="instructions"></textarea>
                </div>
            </div>

            <div class="row">
                <div class="col span-1-of-3">
                    <label>&nbsp;</label>
                </div>
                <div class="col span-2-of-3 ">
                    <input type="submit" value="Send it!">
                    <input type="reset" value="Reset">
                </div>
            </div>
        </form>
    </div>


</section>

<script>

    document.getElementById('addPostForm').addEventListener('submit',addPost)

    function addPost(event) {
        event.preventDefault();

        let end_to=document.getElementById('end_to').value;
        let start_from=document.getElementById('start_from').value;
        let ride_date=document.getElementById('ride_date').value;
        let ride_time=document.getElementById('ride_time').value;
        let slots_offered=document.getElementById('slots_offered').value;
        let instructions=document.getElementById('instructions').value;
        var user_id='rxj08740@ucmo.edu';



        const myPost={
            user_id: user_id,
            end_to: end_to,
            start_from: start_from,
            ride_date: ride_date,
            ride_time: ride_time,
            slots_offered: slots_offered,
            instructions: instructions
        }
        fetch('http://localhost:8080/StudentPoolWeb_war/webapi/rides_offered', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Origin':'http://127.0.0.1:51700/Rides/postride.html'
            },
            body: JSON.stringify(myPost)
        })
            .then((res) => {
                if(res.status==201){
                    return res.json()
                }else{
                    return Promise.reject({status: res.status, statusText:res.statusText})
                }
            })
            .then((data) => console.log(data))
            .catch(err=>console.log('Error message:',err.statusText))
    }

</script>
<script>
    function activateplacesearch(){
        var inputdest= document.getElementById('end_to');
        var inputstart= document.getElementById('start_from');
        var autocomplete= new google.maps.places.Autocomplete(inputdest);
        var autocomplete2= new google.maps.places.Autocomplete(inputstart);
    }

</script>

<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCmxbqLcvs51LDc6dIJbeEua1H7dpO_COc&libraries=places&callback=activateplacesearch"></script>

</body>
</html>
