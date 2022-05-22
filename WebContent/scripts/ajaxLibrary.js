//This is an ajax libary that I have modified to work with XML and text (https://github.com/ThomasRog3rs/easyHTTP - my own github)
class HttpAjax {
	//This function is to format the data in the correct content type
	async formatData(accept, data){
		let formattedData;
		switch(accept){
			case "application/json":
				formattedData = await data.json();
				break;
			case "text/xml":
				formattedData = await data.text();
				let parser = new DOMParser();
				formattedData = parser.parseFromString(formattedData, accept);
				break;
			case "text/plain":
				formattedData = await data.text();
				break;
			default:
		}
		
		return formattedData;
	}
	
  	// Make an HTTP GET Request
  	async get(url, accept = "application/json") {
    	const response = await fetch(url, {
      		method: 'GET',
      		headers: {
        		'Accept': accept
      		}
    	});
    	
    	const formatResponse = this.formatData(accept, response);
    	return formatResponse;
  	}

  	// Make an HTTP POST Request
  	async post(url, data, contentType = "application/json", accept = "application/json") {
		if(contentType === "application/json"){
			data = JSON.stringify(data);
		}
		
		if(contentType === "text/xml"){
			data = data.outerHTML;
		}
		
    	const response = await fetch(url, {
      		method: 'POST',
      		headers: {
        		'Content-Type': contentType,
        		'Accept': accept
      		},
      		body: data
    	});

    	const formatResponse = this.formatData(accept, response);
    	return formatResponse;
  	}

    // Make an HTTP PUT Request
  	async put(url, data, contentType = "application/json", accept = "application/json") {
		if(contentType === "application/json"){
			data = JSON.stringify(data);
		}
		
		if(contentType === "text/xml"){
			data = data.outerHTML;
		}
		
    	const response = await fetch(url, {
      		method: 'PUT',
      		headers: {
        		'Content-Type': contentType,
        		'Accept': accept
      		},
      		body: data
    	});
	
	    const formatResponse = this.formatData(accept, response);
	    return formatResponse;
	}

   // Make an HTTP DELETE Request
   async delete(url, accept = "application/json") {
      const response = await fetch(url, {
	      method: 'DELETE',
	      headers: {
	        'Accept': accept
	      }
	  });

	  const formatResponse = this.formatData(accept, response);
	  return formatResponse;
   }
}