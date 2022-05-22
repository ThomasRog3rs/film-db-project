    	//Set up required elements
	    const sendRequest = document.getElementById("sendRequest");
	    const outputDiv = document.getElementById("outputDiv");
	    const outputBox = document.getElementById("outputBox");
	    const methodInput = document.getElementById("methodSelect");
	    const idInput = document.getElementById("idInput");
	    const acceptTypeInput = document.getElementById("acceptFormat");
	    const contentTypeInput = document.getElementById("contentType");
	    const inputFormDiv = document.getElementById("inputFormDiv");
	    
	    //if the methodInput is a method that needs additonal data then show a form
	    methodInput.addEventListener("change", e => {
	    	if(methodInput.value === "post" || methodInput.value === "put"){
	    		//show form
	    		inputFormDiv.style.opacity = "1";
	    		inputFormDiv.style.height = "100%";
	    		inputFormDiv.style.pointerEvents = "auto";
	    		
	    		//if the form is to update then enable the ID field
	    		if(methodInput.value == "put"){
	    			document.getElementById("PostFilmId").disabled = false;
	    		}else{ //disable ID field if it is for post
	    			document.getElementById("PostFilmId").value = null;
	    			document.getElementById("PostFilmId").disabled = true;
	    		}
	    		
	    	}else{
	    		//hide form
	    		inputFormDiv.style.opacity = "0";
	    		inputFormDiv.style.height = "0px";
	    		inputFormDiv.style.pointerEvents = "none";
	    	}
	    });
	    
	    //create http ajax libary object (global)
	    const http = new HttpAjax();
	    
	    //When the send btn is clicked then send the request
	    sendRequest.addEventListener("click", e => {
	    	//get method
	    	const method = methodInput.value;
	    	
	    	//get movieId (from URL input string in UI, irrelivent for PUT)
	    	const id = idInput.value;
	    	
	    	//get output data format
	    	const accept = acceptTypeInput.value;
	    	
	    	//get input data format
	    	const contentType = contentTypeInput.value;
	    	
	    	//construct URL
	    	const url = `http://localhost:8080/FilmProject/Films?FilmID=${id}`;
	    	
	    	//Send a request depending on the selected method
			switch(method){
				case "get":
					httpGet(url, accept);
					break;
				case "post":
					httpPost(url,contentType, accept)
					break;
				case "put":
					httpPut(url, contentType, accept)
					break;
				case "delete":
					httpDelete(url, accept)
					break;
				default:
					alert("This is an unkown method");
			}
	    });
	    
	    
	    //Get Function
	    function httpGet(url, accept){
	    	http
			  .get(url, accept)
			  .then((result) => {
				  console.log(result);
				  if(accept === "application/json"){
					  outputBox.innerHTML = JSON.stringify(result, null, 4);
				  }else if(accept === "text/xml"){
					  let xmlText = new XMLSerializer().serializeToString(result);
					  outputBox.innerHTML = xmlText;
				  }else{
					  outputBox.innerHTML = result;
				  }
			  })
			  .catch(err => console.log(err));
	    	
	    	//show the output box
	    	outputDiv.style.opacity = "1";
	    	outputDiv.style.pointerEvents = "auto";
	    }
	    
	    //Delete Function
	    function httpDelete(url, contentType){
	    	http
			  .delete(url, contentType)
			  .then((result) => {
				  console.log(result);
				  if(contentType === "application/json"){
					  outputBox.innerHTML = JSON.stringify(result, null, 4);
				  }else if(contentType === "text/xml"){
					  let xmlText = new XMLSerializer().serializeToString(result);
					  outputBox.innerHTML = xmlText;
				  }else{
					  outputBox.innerHTML = result;
				  }
			  })
			  .catch(err => console.log(err));
	    	
	    	//show the output box
	    	outputDiv.style.opacity = "1";
	    	outputDiv.style.pointerEvents = "auto";
	    }
	    
	    //post function (url = endpoint, content = data format sending, accept = data format returning)
	    function httpPost(url, contentType, accept){
	    	//get data from the form
	    	const title = document.getElementById("PostFilmTitle").value;
	    	const year = document.getElementById("PostFilmYear").value;
	    	const director = document.getElementById("PostFilmDirector").value;
	    	const stars = document.getElementById("PostFilmStars").value;
	    	const review = document.getElementById("PostFilmReview").value
	    	
	    	//Create a variable to store the details of the film
	    	let data;
	    	
	    	if(contentType === "application/json"){
			    //Create a JSON object that needs to be sent as the body (this holds the data for the film)
				data ={
				    "title": title,
				    "year": year,
				    "director": director,
				    "stars": stars,
				    "review": review
				};
			}else if(contentType === "text/xml"){
				//Create a document and add the details as XML into the document
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
			}

	    	//send the http request to post a film
	    	http
			  .post(url, data, contentType, accept)
			  .then((result) => {
				  console.log(result);
				  //Get the result, convert to string and display to  the user
				  if(accept === "application/json"){
					  outputBox.innerHTML = JSON.stringify(result, null, 4);
				  }else if(accept=== "text/xml"){
					  let xmlText = new XMLSerializer().serializeToString(result);
					  outputBox.innerHTML = xmlText;
				  }else{
					  outputBox.innerHTML = result;
				  }
			  })
			  .catch(err => console.log(err));
	    	
	    	//show the output box
	    	outputDiv.style.opacity = "1";
	    	outputDiv.style.pointerEvents = "auto";
	    	
	    }
	    
	    //Update Funtion
	    function httpPut(url, contentType, accept){
	    	//get data from the form
	    	const id = document.getElementById("PostFilmId").value;
	    	const title = document.getElementById("PostFilmTitle").value;
	    	const year = document.getElementById("PostFilmYear").value;
	    	const director = document.getElementById("PostFilmDirector").value;
	    	const stars = document.getElementById("PostFilmStars").value;
	    	const review = document.getElementById("PostFilmReview").value;
	    		    
	    	//Create a variable to store the details of the film	
	    	let data;
	    	
	    	if(contentType === "application/json"){
				//Create data object that needs to be sent as the body (this holds the data for the film)
				data ={
		    		"id": id,
				    "title": title,
				    "year": year,
				    "director": director,
				    "stars": stars,
				    "review": review
				};
				
				//add data into an array to make removal of unused fileds easier
			    let dataArray = []
			    dataArray.push(["id", id]);
			    dataArray.push(["title", title]);
			    dataArray.push(["year", year]);
			    dataArray.push(["director", director]);
			    dataArray.push(["stars", stars]);
			    dataArray.push(["review", review]);
		    	
		    	//if the field is empty the remove from payload (data)
		    	dataArray.forEach((field) => {
		    		console.log(field);
		    		if(field[1] == ""){
		    			delete data[field[0]];
		    		}
		    	});
			}else if(contentType === "text/xml"){
				//Create a document and add the details as XML into the document
				let doc = document.implementation.createDocument("", "", null);
				let filmElement = doc.createElement("Film");
				
				let idElement = doc.createElement("ID");
				idElement.innerHTML = id;
				filmElement.appendChild(idElement);
				
				//If any of the details are not filled in, do not add them to the document
				if(title != ""){
					let titleElement = doc.createElement("Title");
					titleElement.innerHTML = title;
					filmElement.appendChild(titleElement);
				}

				if(year != ""){
					let yearElement = doc.createElement("Year");
					yearElement.innerHTML = year;
					filmElement.appendChild(yearElement);
				}
				
				if(director != ""){
					let directorElement = doc.createElement("Director");
					directorElement.innerHTML = director;
					filmElement.appendChild(directorElement);
				}
				
				if(stars != ""){
					let starsElement = doc.createElement("Stars");
					starsElement.innerHTML = stars;
					filmElement.appendChild(starsElement);
				}
				
				if(review != ""){
					let reviewElement = doc.createElement("Review");
					reviewElement.innerHTML = review;
					filmElement.appendChild(reviewElement);
				}
				
				
				//Save the parent element in the data variable
				data = filmElement;
				console.log(data);
			}

	    	//send the http request to update a film
	    	http
			  .put(url, data, contentType, accept)
			  .then((result) => {
				  console.log(result);
				  if(accept === "application/json"){
					  outputBox.innerHTML = JSON.stringify(result, null, 4);
				  }else if(accept === "text/xml"){
					  let xmlText = new XMLSerializer().serializeToString(result); //change xml to string to show in the output box
					  outputBox.innerHTML = xmlText;
				  }else{
					  outputBox.innerHTML = result;
				  }
			  })
			  .catch(err => console.log(err));
	    	
	    	//show the output box
	    	outputDiv.style.opacity = "1";
	    	outputDiv.style.pointerEvents = "auto";
	    }