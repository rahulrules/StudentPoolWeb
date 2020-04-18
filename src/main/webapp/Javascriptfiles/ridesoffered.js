
document.getElementById('ro').addEventListener('click',fetchrideoptions)

document.addEventListener('click',function(e){
    if(e.target && e.target.id=='vor'){
        clearBox('filterdiv');
        fetchridefilters();
    }
});

document.addEventListener('click',function(e){
    if(e.target && e.target.id=='va'){
        clearBox('ridesdata');
        var url='http://localhost:8080/StudentPoolWeb_war/webapi/rides_offered'
        fetchridesdata(url);
    }
});


function querychange(){
    var url='http://localhost:8080/StudentPoolWeb_war/webapi/rides_offered?to='+document.getElementById('end_to').value;


}


document.addEventListener('submit',function(e){
    if(e.target && e.target.id=='queryform'){
        var url='http://localhost:8080/StudentPoolWeb_war/webapi/rides_offered?to='+document.getElementById('end_to').value;
        clearBox('ridesdata');

        fetchridesdata(url)
//        '<p>'+ '</p>' document.getElementById('ridesdata').innerHTML=url
        ;
    }
});





function fetchrideoptions(){

    var contentdiv1= document.getElementById("content");

    var output ='<div id="vordiv" class="col span-1-of-2 options">'+'<a id="vor" class="btn-opt" href="#" >View Offered Rides</a>'+

        '</div>';

    output+='<div id="onrdiv" class="col span-1-of-2 options">'+'<a id="onr" class="btn-opt" href="http://localhost:8080/StudentPoolWeb_war/postride.jsp">Offer New Ride</a>'+'</div>'

        +'<div id="filterdiv" class="filterdiv">'
    ;



    contentdiv1.innerHTML=output;

}

function fetchridefilters(){

    var contentFilterdiv=document.getElementById('filterdiv');
    var output ='<div class="filters">'+'<a id="va" class="btn-opt" href="#">View All Rides</a>'+'</div>';

    output+='<form id="queryform" class="filters">'+
        '<div class="filters">'+'<input type="text" name="end_to" id="end_to" onchange="querychange()" placeholder="Destination" required>'+'</div>'
        +
        '<div class="filters">'+'<input id="submitbtn" type="submit" value="submit">'+'</div>' +
        '</form>';

    contentFilterdiv.innerHTML=output;
}


function clearBox(elementID)
{
    document.getElementById(elementID).innerHTML="";
}




function fetchridesdata(param){
    fetch(param)
        .then(response => {
            if(response.ok){
                return response.json()
            }else{
                return Promise.reject({status: response.status, statusText:response.statusText})
            }
        })
        .then(rides =>{
            let output='<h2>Rides On Offer</h2>'

            rides.forEach(function(ride) {
                output+='<div class="row ridedata">';
                output+=
                    '<div class="dest col span-1-of-6">'+'<label>Destination</label><br>'
                    +
                    `${ride.end_to}`
                    +
                    '</div>'

                    +

                    '<div class="start col span-1-of-6">'+'<label>Origin</label><br>'
                    +
                    `${ride.start_from}`
                    +
                    '</div>'
                    +

                    '<div class="date col span-1-of-6">'+'<label>Date</label><br>'
                    +
                    `${ride.ride_date}`
                    +
                    '</div>'
                    +

                    '<div class="time col span-1-of-6">'+'<label>Time</label><br>'
                    +
                    `${ride.ride_time}`
                    +
                    '</div>'
                    +

                    '<div class="slots col span-1-of-6">'+'<label>Slots Offered</label><br>'
                    +
                    `${ride.slots_offered}`
                    +
                    '</div>'
                    +

                    '<div class="col span-1-of-6">'+'<label>&nbsp;</label><br><a class="btn btn-full" href="#">View Details</a><br>'
                    +

                    '</div>'

                ;

                output+='</div>'

            });

            document.getElementById("ridesdata").innerHTML=output;
        })
        .catch(err=>console.log('Error message:',err.statusText))

}