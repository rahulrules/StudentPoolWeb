
     var content=document.getElementById('content');
     document.getElementById('about').addEventListener('click',showcontent);
        
        function showcontent(){
    
            var contentdiv1= document.createElement("div");
            var contentdiv1para= document.createElement("p");
            contentdiv1para.innerHTML="1.Only email ending  @ucmo.edu are eligible to register";
            
            var contentdiv2= document.createElement("div");
            var contentdiv2para= document.createElement("p");
            contentdiv2para.innerHTML="2.Primarly for students";
            
            var contentdiv3= document.createElement("div");
            var contentdiv3para= document.createElement("p");
            contentdiv3para.innerHTML="3.Enjoy our service";
            
            contentdiv1.setAttribute('class','info-points');
            contentdiv2.setAttribute('class','info-points');
            contentdiv3.setAttribute('class','info-points');
            
            contentdiv1.appendChild(contentdiv1para);
            contentdiv2.appendChild(contentdiv2para);
            contentdiv3.appendChild(contentdiv3para);
            content.appendChild(contentdiv1);
            content.appendChild(contentdiv2);
            content.appendChild(contentdiv3);
        }
        
        function clearBox(elementID)
        {
            document.getElementById(elementID).innerHTML="";
        }
        