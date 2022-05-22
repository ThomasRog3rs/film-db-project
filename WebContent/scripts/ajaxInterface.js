	  //create http ajax libary object
	  const http = new HttpAjax();
	  
	  //Data format
	  const dataFormatInput = document.getElementById("contentType");
	  document.getElementById("dataFormatEdit").innerText = dataFormatInput.value;
	  document.getElementById("dataFormatPost").innerText = dataFormatInput.value;
	  document.getElementById("dataFormatGet").innerText = dataFormatInput.value;
	  let dataFormat = dataFormatInput.value;
	    	  
      //Set up required elements for the films table
  	  const filmsTable = document.getElementById("filmsTables");
  	  const filmsTableRecords = document.getElementById("filmsTablesRecords");
      const filmsInterface = document.getElementById("interface");
  	  const loadingFilms = document.getElementById("loadingFilms");
  	  
  	  //toast
  	  const toastAlert = document.getElementById("toastAlert");
  	  const toastTitle = document.getElementById("toastTitle");
  	  const toastBody = document.getElementById("toastBody");
  	  
  	  //buttons
  	  const postFilmBtn = document.getElementById("postNewFilmBtn");
  	  const deleteFilmBtn = document.getElementById("deleteFilmBtn");
  	  const editFilmBtn = document.getElementById("editFilmBtn");
  	  
  	  const editForm = document.getElementById("editFilmForm");
  	  const postForm = document.getElementById("createNewFilm");
  	  
  	  dataFormatInput.addEventListener("change", e =>{
	  	document.getElementById("dataFormatEdit").innerText = dataFormatInput.value;
	  	document.getElementById("dataFormatPost").innerText = dataFormatInput.value;
	  	document.getElementById("dataFormatGet").innerText = dataFormatInput.value;
	  	dataFormat = dataFormatInput.value;
	  });
  	  	  	  
  	  //https://www.datatables.net/manual/ajax - this is the JQuery plugin I am using to display the data to users
  	  let table = $('#filmsTables').DataTable( {
	  	    ajax: {
	  	        url: 'http://localhost:8080/FilmProject/Films', //this takes the input as the data sourse
	  	        dataSrc: '' //This tells DataTables that data stars at the start of an array
	  	    },
	  	  columns: [ //this tells DataTables where to look for each column data
	          { data: 'id' },
	          { data: 'title' },
	          { data: 'year' },
	          { data: 'director' },
	          { data: 'stars' },
	          { data: 'review' }
	      ]
	  	} );
  	  
  	//global film ID
  	let filmId;
  	table.on( 'click', function ( e, dt, type, indexes ) {
  		filmId = e.target.parentNode.childNodes[0].innerText;
  		if(typeof filmId !== "undefined"){
  	  		if(filmId !== "FILM ID"){
  	  			//open edit and delete options for this selection
  	  			$('#editModal').modal('toggle');
  	  		}
  		}
  	} );
  	
  	//Fill in the edit form when the modal is opened
  	editModal.addEventListener('shown.bs.modal', function (e) {        
		//clear the form before adding new values
		editForm.reset();
		// Get Film by ID
		http
		  .get('http://localhost:8080/FilmProject/Films?FilmID='+filmId, dataFormat)
		  .then((res) => {
			  //Insert date of slected film into the edit form
			  console.log(res);
			  if(dataFormat === "application/json"){
				  document.getElementById("FilmID").setAttribute("value", res.id);
				  document.getElementById("FilmTitle").setAttribute("value", res.title);
				  document.getElementById("FilmYear").setAttribute("value", res.year);
				  document.getElementById("FilmDirector").setAttribute("value", res.director);
				  document.getElementById("FilmStars").setAttribute("value", res.stars);
				  document.getElementById("FilmReview").innerText = res.review;		
			  }else if(dataFormat === "text/xml"){ //if the dataFormat is xml then get the data from xml and display in the edit modal
				let details = res.documentElement;
				details = details.children;
				
				for(let i = 0; i<details.length; i++){
					document.getElementById("Film"+details[i].tagName).setAttribute("value", details[i].innerHTML);
					if(details[i].tagName === "Review"){
						document.getElementById("FilmReview").innerText = details[i].innerHTML;	
					}
				}
			}

		  })
		  .catch(err => console.log(err));
    });
  		
  	deleteFilmBtn.addEventListener("click", (e) => {
  		e.preventDefault();
		  http
		  .delete('http://localhost:8080/FilmProject/Films?FilmID=' + filmId, dataFormat)
		  .then((result) => {
			  console.log(result);
				
			  let resultMessage;
			  if(dataFormat === "application/json"){
				resultMessage = result.Message;
			  }else if (dataFormat === "text/xml"){ //if the dataFormat is xml then get the message out as xml
		       	let details = result.documentElement;
		       	details = details.children;
		       	console.log(details);
		       	resultMessage = details[0].innerHTML;
			  }
			  
			  //show message from the server to the user
			  toastAlert.classList += " show";
			  toastTitle.innerText = "Delete Film: " + filmId
			  toastBody.innerText = resultMessage;
			  
	    	  //hide toast after 5 seconds
	    	  setTimeout(function(){
	    	  	toastAlert.classList.remove("show");
	    	  }, 5000);

			  //close modal
			  $(function () {
			  	$('#editModal').modal('hide');
			  	//This is extra code to propperly remove the modal and it's effects
			  	document.querySelector(".modal-backdrop").remove(); 
			  	let bodyTag = document.getElementsByTagName("BODY")[0];
			  	while(bodyTag.attributes.length > 0)
			  		bodyTag.removeAttribute(bodyTag.attributes[0].name);
			  });
			  
			  //update films table
			  table.ajax.reload();
		  })
	      .catch(err => console.log(err));
	  });
  	
  	editFilmBtn.addEventListener("click", (e) => {
  		e.preventDefault();
    	//Get values from form
    	let id = document.getElementById("FilmID").value
    	let title = document.getElementById("FilmTitle").value;
    	let year = document.getElementById("FilmYear").value;
    	let director = document.getElementById("FilmDirector").value;
    	let stars = document.getElementById("FilmStars").value;
    	let review = document.getElementById("FilmReview").value;
    	
    	let inputs = [id, title, year, director, stars, review];
    	
    	if(validInputs(inputs)){
			let data;
			if(dataFormat === "application/json"){
				//build data object
	    		data = {
	    			"id": id,
	    			"title": title,
	    			"year": year,
	    			"director": director,
	    			"stars": stars,
	    			"review": review
	    		};
			}else if(dataFormat === "text/xml"){
				let doc = document.implementation.createDocument("", "", null);
				let filmElement = doc.createElement("Film");
				
				let idElement = doc.createElement("ID");
				idElement.innerHTML = id;
				filmElement.appendChild(idElement);
				
				let titleElement = doc.createElement("Title");
				titleElement.innerHTML = title;
				filmElement.appendChild(titleElement);
				
				let yearElement = doc.createElement("Year");
				yearElement.innerHTML = year;
				filmElement.appendChild(yearElement);
					
				let directorElement = doc.createElement("Director");
				directorElement.innerHTML = director;
				filmElement.appendChild(directorElement);
					
				let starsElement = doc.createElement("Stars");
				starsElement.innerHTML = stars;
				filmElement.appendChild(starsElement);
					
				let reviewElement = doc.createElement("Review");
				reviewElement.innerHTML = review;
				filmElement.appendChild(reviewElement);
				
				//Save the parent element in the data variable
				data = filmElement;
				console.log(data);
			}

    		
    		//send post request
    		http
			  .put('http://localhost:8080/FilmProject/Films', data, dataFormat, dataFormat)
			  .then((result) => {
				  console.log(result);
				  
				  let resultMessage;
				  if(dataFormat === "application/json"){
					resultMessage = result.Message;
				  }else if (dataFormat === "text/xml"){ //if the dataFormat is xml then get the message out as xml
			       	let details = result.documentElement;
			       	details = details.children;
			       	console.log(details);
			       	resultMessage = details[0].innerHTML;
				  }
				  
				  //show message from the server to the user
				  toastAlert.classList += " show";
				  toastTitle.innerText = "Update new film ["+id+"] result";
				  toastBody.innerText = resultMessage;
				  
		    	  //hide toast after 5 seconds
		    	  setTimeout(function(){
		    	  	toastAlert.classList.remove("show");
		    	  }, 5000);

				  //close modal
				  $(function () {
				  	$('#editModal').modal('hide');
				  	//This is extra code to propperly remove the modal and it's effects on the page
				  	document.querySelector(".modal-backdrop").remove(); 
				  	let bodyTag = document.getElementsByTagName("body")[0];
				  	while(bodyTag.attributes.length > 0)
				  		bodyTag.removeAttribute(bodyTag.attributes[0].name);
				  });
				  
				  //update films table
				  table.ajax.reload();
			  })
		      .catch(err => console.log(err));
    		
    	}else{
    		console.log("do not send the form");
    		//show an error toast to tell the user there is an issue with their input
    		document.getElementById("formError").classList += " show";
    		
    		console.log("Form submit error");
    		
    		//hide error toast after 5 seconds
    		setTimeout(function(){
    			document.getElementById("formError").classList.remove("show");
    		}, 5000);
    	}
  });
	  
  	postFilmBtn.addEventListener("click", (e) => {
  		e.preventDefault();
	    	//Get values from form
	    	let title = document.getElementById("PostFilmTitle").value;
	    	let year = document.getElementById("PostFilmYear").value;
	    	let director = document.getElementById("PostFilmDirector").value;
	    	let stars = document.getElementById("PostFilmStars").value;
	    	let review = document.getElementById("PostFilmReview").value;
	    	
	    	let inputs = [title, year, director, stars, review];
	    	
	    	if(validInputs(inputs)){
				let data;
				if(dataFormat === "application/json"){
					//build data object
		    		data = {
		    			"title": title,
		    			"year": year,
		    			"director": director,
		    			"stars": stars,
		    			"review": review
		    		};
				}else if(dataFormat === "text/xml"){
					let doc = document.implementation.createDocument("", "", null);
					let filmElement = doc.createElement("Film");
					
					let titleElement = doc.createElement("Title");
					titleElement.innerHTML = title;
					filmElement.appendChild(titleElement);
					
					let yearElement = doc.createElement("Year");
					yearElement.innerHTML = year;
					filmElement.appendChild(yearElement);
						
					let directorElement = doc.createElement("Director");
					directorElement.innerHTML = director;
					filmElement.appendChild(directorElement);
						
					let starsElement = doc.createElement("Stars");
					starsElement.innerHTML = stars;
					filmElement.appendChild(starsElement);
						
					let reviewElement = doc.createElement("Review");
					reviewElement.innerHTML = review;
					filmElement.appendChild(reviewElement);
					
					//Save the parent element in the data variable
					data = filmElement;
					//console.log(data);
				}
				
				console.log(data);
	    		
	    		//send post request
	    		http
				  .post('http://localhost:8080/FilmProject/Films', data, dataFormat, dataFormat)
				  .then((result) => {
					  console.log(result);
					   
					  let resultMessage;
					  if(dataFormat === "application/json"){
						resultMessage = result.Message;
					  }else if (dataFormat === "text/xml"){ //if the dataFormat is xml then get the message out as xml
				       	let details = result.documentElement;
				       	details = details.children;
				       	console.log(details[0]);
				       	resultMessage = details[0].innerHTML;
					  }
					  //show message from the server to the user
					  toastAlert.classList += " show";
					  toastTitle.innerText = "Post new film result"
					  toastBody.innerText = resultMessage;
					  
			    	  //hide toast after 5 seconds
			    	  setTimeout(function(){
			    	  	toastAlert.classList.remove("show");
			    	  }, 5000);
			    	  
			    	  postForm.reset(); //clear the form after sending the post

					  //close modal
					  $(function () {
					  	$('#postModal').modal('hide');
					  	//This is extra code to propperly remove the modal and it's effects
					  	document.querySelector(".modal-backdrop").remove(); 
					  	let bodyTag = document.getElementsByTagName("body")[0];
					  	while(bodyTag.attributes.length > 0){
					  		bodyTag.removeAttribute(bodyTag.attributes[0].name);
					  	}
					  	
					  	
					  });
					  
					  //update films table
					  table.ajax.reload();
				  })
			      .catch(err => console.log(err));
	    		
	    	}else{
	    		console.log("do not send the form");
	    		//show an error toast to tell the user there is an issue with their input
	    		document.getElementById("formError").classList += " show";
	    		
	    		console.log("Form submit error");
	    		
	    		//hide error toast after 5 seconds
	    		setTimeout(function(){
	    			document.getElementById("formError").classList.remove("show");
	    		}, 5000);
	    	}
	  });
  	  
	  //this function validates form inputs (returns true if valid, false if invalid)
	  function validInputs(inputs){
		  valid = true;
		  inputs.forEach(input => {
			  if(input == ""){
				  valid = false;
			  }
		  });
		  
		  return valid;
	  }