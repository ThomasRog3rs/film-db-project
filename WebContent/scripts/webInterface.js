//Add Jquery dataTables plugin (a UI libary) to the table of films
    $(document).ready( function () {
        $('#filmsTables').DataTable();
    } );
  	
    //create http ajax libary object
    const http = new HttpAjax();
    
   //Get the film id for selected movie on edit btn click
   //(select all the edit buttons and add an eventlistener for each on to get the film ID)
   let editFilmId = null;
   let editBtns = document.querySelectorAll('.btn.btn-sm.btn-primary');
    editBtns.forEach(function(btn, index){
    	btn.addEventListener("click", function(e){
    		//console.log(btn.parentElement.parentElement.childNodes[1].innerText);
    		editFilmId = btn.parentElement.parentElement.childNodes[1].innerText;
    	})
    })
    
   //Get the film id for selected movie on delete click
   //(select all the delete buttons and add an eventlistener for each on to get the film ID)
   let deleteFilmId = null;
   let deleteBtns = document.querySelectorAll('.btn.btn-sm.btn-danger');
   deleteBtns.forEach(function(btn, index){
    	btn.addEventListener("click", function(e){
    		//console.log(btn.parentElement.parentElement.childNodes[1].innerText);
    		deleteFilmId = btn.parentElement.parentElement.childNodes[1].innerText;
    	})
    })
    
    
    //When the edit modal is opended then fill the form in with current film details
    //(modal opens with bootstrap data toggles that targets the modal, this is making use of the bootstrap UI libary)
    let editModal = document.getElementById('editModal');
    editModal.addEventListener('shown.bs.modal', function (e) {
		// Get Film by ID
		http
		  .get('http://localhost:8080/FilmProject/Films?FilmID='+editFilmId)
		  .then((res) => {
			  //Insert date of slected film into the edit form
			  console.log(res);
			  if(res.message != null){
				  alert(res.message);
			  }
			  
			  document.getElementById("FilmID").setAttribute("value", res.id);
			  document.getElementById("FilmTitle").setAttribute("value", res.title);
			  document.getElementById("FilmYear").setAttribute("value", res.year);
			  document.getElementById("FilmDirector").setAttribute("value", res.director);
			  document.getElementById("FilmStars").setAttribute("value", res.stars);
			  document.getElementById("FilmReview").innerText = res.review;
		  })
		  .catch(err => alert(err));
    });
    
    //When delete modal is opened fill in the film Id and film Title
    //(modal opens with bootstrap data toggles that targets the modal, this is making use of the bootstrap UI libary)
    let deleteModal = document.getElementById('deleteModal');
    deleteModal.addEventListener('shown.bs.modal', function (e) {
    	document.getElementById("FilmIDDelete").setAttribute("value", deleteFilmId);
    	
    	//Get Film by Id so the name can be filled in on the modal
    	http
		  .get('http://localhost:8080/FilmProject/Films?FilmID='+deleteFilmId)
		  .then((res) => {
			  console.log(res);
			  console.log(res.title + " ID: " + res.id);
			  document.getElementById("FilmDisplayNameDelete").innerText = "'" + res.title + "'";
		  })
		  .catch(err => alert(err));
    });
    
    //Validate new film post form, when the user clicks on the submit button
    document.getElementById("postNewFilmBtn").addEventListener("click", function(e){
    	//get the form from element the DOM
    	let form = document.getElementById("createNewFilm");
    	
    	//this stops the form from submitting before being validated
    	e.preventDefault();
    	
    	//Get values from form
    	let title = document.getElementById("PostFilmTitle").value;
    	let year = document.getElementById("PostFilmYear").value;
    	let director = document.getElementById("PostFilmDirector").value;
    	let stars = document.getElementById("PostFilmStars").value;
    	let review = document.getElementById("PostFilmReview").value;
    	
    	//validate form
    	if(title == "" || year == "" || director == "" || stars == "" || review == ""){
    		//show an error toast to tell the user there is an issue with their input
    		document.getElementById("formError").classList += " show";
    		
    		//console.error("Form submit error");
    		
    		//hide error toast after 5 seconds
    		setTimeout(function(){
    			document.getElementById("formError").classList.remove("show");
    		}, 5000);
    	}else{
    		form.submit(); // this submits the form to the action (the servlet) when the form is valid
    	}
    });
    
    //Validate edit film form
    document.getElementById("editFilmBtn").addEventListener("click", function(e){
    	//get the form from the DOM
    	let form = document.getElementById("editFilmForm");
    	
    	//this stops the form from submitting before being validated
    	e.preventDefault();
    	
    	//Get values from form
    	let title = document.getElementById("FilmTitle").value;
    	let year = document.getElementById("FilmYear").value;
    	let director = document.getElementById("FilmDirector").value;
    	let stars = document.getElementById("FilmStars").value;
    	let review = document.getElementById("FilmReview").value;
    	
    	//validate form
    	if(title == "" || year == "" || director == "" || stars == "" || review == ""){
    		//show an error toast to tell the user there is an issue with their input
    		document.getElementById("formError").classList += " show";
    		
    		//console.error("Form submit error");
    		
    		//hide error after 5 seconds
    		setTimeout(function(){
    			document.getElementById("formError").classList.remove("show");
    		}, 5000);
    		
    	}else{
    		form.submit(); // this submits the form to the action (the servlet) when the form is valid
    	}
    });