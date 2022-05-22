package filmAPI;

//import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import common.HelperMethods;
import models.Film;
import models.FilmDAO;

/**	
 * Servlet implementation class JsonAPI
 */
@WebServlet("/Films")
public class FilmCrudAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson(); // create Gson object
	private FilmDAO filmDAO = new FilmDAO(); // create DAO
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FilmCrudAPI() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * Get Films, takes optional parameter of filmID (either return all movies or single movie by ID)
	 * GET /Films - returns list all films (JSON default)
	 * GET /Films?FilmID - returns specific film (JSON default)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Prevent CORS issues in the browser
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		//Get Film Id from URL
		String filmID = request.getParameter("FilmID");
		
		//find out how the client wants the data to be formatted
		String accept = request.getHeader("Accept");
		response.setContentType(accept); //set the content type (the return type) to what the client accepts
		
		//if there is no accept header then set null to a string with the value of null so the switch default works
		if(accept == null) {
			accept = "null";
		}
		//Set content type of what the user has requested
		response.setCharacterEncoding("UTF-8");
		
		//Prepare JSON string
		String output = "";
		
		//Prepare variables for films
		ArrayList<Film> films = new ArrayList<Film>();
		Film film = null;
		
		
		// If there is no ID return all films, else get single film from Id
		if(filmID == null || filmID.isEmpty()) {
			films = filmDAO.getAllFilms();
		}else {
			try {
				film = filmDAO.getFilmByID(filmID); // get film from ID
				System.out.println(film.toString());
			}catch (Exception e) {
				//This means a film was not found so inform the user and stop the action
				System.out.println("No movies found (SQL Exception)");
				
				//Create a HashMap to fill in with details of the error for the user if no film was found
				HashMap<String, String> errorProps = new HashMap<String, String>();
				errorProps.put("Message", "No Films found with ID: '" + filmID + "'");
				errorProps.put("ID", filmID);
				
				// If no movies are found then send error message to user (Custom returns for error feedback)
				switch(accept) {
					case "application/json":
						output = HelperMethods.CustomJson(errorProps);
						break;
					case "text/xml":
						//response.setContentType(accept);
						output = HelperMethods.CustomXML(errorProps);
						break;
					case "text/plain":
						//response.setContentType(accept);
						output = errorProps.toString();
						break;
					default:
						response.setContentType("application/json"); // default is JSON if the client does not define an accept type
						output = HelperMethods.CustomJson(errorProps); // Transform Film Object to JSON String if a film is found
				}
				//Return result(s)
			    response.getWriter().write(output);
			    
			    //stop
			    return;
			}
		}
		
		//set result depending on the length of the ArrayList
		var result = (films.size() == 0) ? film: films;
		
		//System.out.println(films.size());
		
		response.setContentType(accept);
		//return data if the catch does not hit
		switch(accept) {
			case "application/json":
				output = gson.toJson(result); // Transform Film Object to JSON String if a film is found
				break;
			case "text/xml":
				//Try to cast to film and if it can't then it must be a list so pass in the ArrayList
				try {
					output = HelperMethods.ParseToXML((Film) result); // Convert Film Object to XML if a film is found
				}catch(ClassCastException e) {
					output = HelperMethods.ParseToXML(films);
				}
				
				break;
			case "text/plain":
				output = result.toString(); // Convert Film Object to String if a film is found
				System.out.println(result.toString());
				break;
			default:
				response.setContentType("application/json");
				output = gson.toJson(result); // Transform Film Object to JSON String if a film is found
		}
		
		//Return result(s)
	    response.getWriter().write(output);
	}
	
	/**
	 * Post Films, takes the body of the post (JSON as default), validates the input and inserts to database if valid input
	 * POST /Films - returns posted film 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Prevent CORS issues in the browser
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		//Create new film object to post to DAO
		Film postFilm = new Film(0, "", 0, "", "", "");
		
		//get the type of data the client is sending in the body
		String contentType = request.getHeader("Content-Type");
		if(contentType == null) {
			contentType = "null";
		}
		
		//find out how the client wants the data to be formatted
		String accept = request.getHeader("Accept");
		//if there is no accept header then set null to a string with the value of null so the switch default works
		if(accept == null) {
			accept = "null";
		}
		//Create a HashMap to fill in with details of the error or success
        HashMap<String, String> returnProps = new HashMap<String, String>();
        
        System.out.println("Content-Type: " + contentType);
        System.out.println("Accept: " + accept);
		
        //Process the body depending on the format the client says it is
        if(contentType.equals("application/json")) {
			//Instantiate the JSON Parser and JsonElement to store parsed data
			JsonParser parser = new JsonParser();
			JsonObject json = new JsonObject(); // This is to hold the body content
			
			//Get body content from post request
			StringBuffer stringBuffer = new StringBuffer();
			String currentLine = null;
			try {
				//get reader and for each iteration on the loop set the current line to reader.readline to get the content
				BufferedReader reader = request.getReader();
				while ((currentLine = reader.readLine()) != null) {
					stringBuffer.append(currentLine); // add to buffer if the line is not null
				}
			 } catch (Exception e) { 
				 System.out.println(e.getMessage()); //show the error in the console for developers
				 returnProps.clear();
				 returnProps.put("Message", "One or more required params could be missing or in the wrong format/data type");
				 returnProps.put("Server Message", e.getMessage()); // include catch error message
			 }
	
			// Parse the string buffer into a JsonObject
			try {
				json = parser.parse(stringBuffer.toString()).getAsJsonObject();
			} catch (Exception e) {
				System.out.println(e.getMessage()); //show the error in the console for developers
				returnProps.clear();
				returnProps.put("Message", "One or more required params could be missing or in the wrong format/data type");
				returnProps.put("Server Message", e.getMessage()); // include catch error message
			}
	
	        
			try {
	    		//Set required data for post (Database will auto increment ID) if the data is valid
	            postFilm.setTitle(json.get("title").getAsString());
	            postFilm.setYear(Integer.parseInt(json.get("year").getAsString()));
	            postFilm.setDirector(json.get("director").getAsString());
	            postFilm.setStars(json.get("stars").getAsString());
	            postFilm.setReview(json.get("review").getAsString());
	            
	            filmDAO.PostFilm(postFilm);
	    	}catch(Exception e){
	    		System.out.println(e.getMessage()); //show the error in the console for developers
	    		returnProps.clear();
	    		returnProps.put("Message", "One or more required params could be missing or in the wrong format/data type");
	    		returnProps.put("Server Message", e.getMessage()); // include catch error message
	    	}
        }else if(contentType.equals("text/xml")) {
        	//process the body as XML
            HashMap<String, String> filmDetails = new HashMap<String, String>();
        	try {       
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();
                Document doc = documentBuilder.parse(request.getInputStream()); //Store body as a document
                
                NodeList details = doc.getDocumentElement().getChildNodes(); //Save the body as a NodeList to use
                
                for(int i = 0; i < details.getLength(); i++) { //Loop over the details
                	if(!details.item(i).getNodeName().equals("#text")) { //If the node is just blank text then skip over until the next node
                		filmDetails.put(details.item(i).getNodeName(), details.item(i).getTextContent()); // add the film detail to the filmDetails HashMap
                		System.out.println(details.item(i).getNodeName() + ": " + details.item(i).getTextContent());
                	}
                }
        	}catch (Exception e){
        		returnProps.put("Server Message", e.getMessage());
        	}
        	
            try {
	    		//Set required data for post (Database will auto increment ID) if the data is valid
	            postFilm.setTitle(filmDetails.get("Title"));
	            postFilm.setYear(Integer.parseInt(filmDetails.get("Year").toString()));
	            postFilm.setDirector(filmDetails.get("Director"));
	            postFilm.setStars(filmDetails.get("Stars"));
	            postFilm.setReview(filmDetails.get("Review"));
	            
	            filmDAO.PostFilm(postFilm);
	    	}catch(Exception e){
	    		System.out.println(e.getMessage()); //show the error in the console for developers
	    		returnProps.clear();
	    		returnProps.put("Message", "One or more required params could be missing or in the wrong format/data type");
	    		returnProps.put("Server Message", e.getMessage()); // include catch error message
	    	}
        	
        }else { // other types
        	returnProps.put("Message", "The server does not yet support contentType of " + contentType);
        }
        
        //if there are no properties to return to the user then there must be no errors so tell them the film as been added
		if(returnProps.size() == 0) {
			returnProps.put("Message", "Movie has been added to DB");
		}
		
		//setup output variable
		String output = "";
		
		response.setContentType(accept); 
		//return the data in the requested format (the accept header)
		switch(accept) {
			case "application/json":
				output = HelperMethods.CustomJson(returnProps);
				break;
			case "text/xml":
				//response.setContentType(contentType);
				output = HelperMethods.CustomXML(returnProps);
				break;
			case "text/plain":
				//response.setContentType(contentType);
				output = returnProps.toString();
				break;
			default:
				response.setContentType("application/json");
				output = HelperMethods.CustomJson(returnProps); // Transform Film Object to JSON String if a film is found
		}
		
		response.getWriter().write(output);
	}
	
	//Returns the film that was updated if successful and null if failed
	/**
	 * Put Films, takes the body of the update (JSON as default), validates the input and inserts to database
	 * PUT /Films - returns success message and ID of film updated 
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Prevent CORS issues in the browser
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		//Create new film object to post to DAO
		Film editedFilm = new Film(0, "", 0, "", "", "");
		
		//get the type of data the client is sending in the body
		String contentType = request.getHeader("Content-Type");
		//if there is no content type then set null to a string with the value of null so the switch default works
		if(contentType == null) {
			contentType = "null";
		}
		
		//find out how the client wants the data to be formatted
		String accept = request.getHeader("Accept");
		if(accept == null) {
			accept = "null";
		}
		
		//Create a HashMap to fill in with details of the error or success
        HashMap<String, String> returnProps = new HashMap<String, String>();
        
        String editID = "";
				
        if(contentType.equals("application/json")) {
			//Instantiate the JSON Parser and JsonElement to store parsed data
			JsonParser parser = new JsonParser();
			JsonObject json = new JsonObject(); // This is to hold the body content
			
			//Get body content from post request
			StringBuffer stringBuffer = new StringBuffer();
			String currentLine = null;	
			try {
				//get reader and for each iteration on the loop set the current line to reader.readline to get the content
				BufferedReader reader = request.getReader();
				while ((currentLine = reader.readLine()) != null) {
					stringBuffer.append(currentLine); // add to buffer if the line is not null
				}
			} catch (Exception e) { 
				 System.out.println(e.getMessage()); //show the error in the console for developers
				 //Create props to show the user the error
				 returnProps.clear();
				 returnProps.put("Message", "Something has gone wrong when reading the body content");
				 returnProps.put("Server Message", e.getMessage()); // include catch error message
	    	}
	
			// Parse the string buffer into a JsonObject
			try {
				json = parser.parse(stringBuffer.toString()).getAsJsonObject();
			} catch (Exception e) {
				System.out.println(e.getMessage()); //show the error in the console for developers
				//create props to show the user the error
				returnProps.clear();
				returnProps.put("Message", "Something has gone wrong parsing the body content");
				returnProps.put("Server Message", e.getMessage()); // include catch error message
			}
			
			try {
				//Make sure the updating movie exists
				editID = json.get("id").getAsString();
				System.out.println(editID);
				Film film = filmDAO.getFilmByID(editID); // get film from ID
				if(film == null) {
					throw new Exception("Film with ID '" + editID + "' does not exsist so it can not be updated");
				}
				
				//If the JSON attribute does not exist use the existing data, otherwise update
				String title = (json.get("title") == null) ? film.title: json.get("title").getAsString();
				Integer year = (json.get("year") == null) ? film.year: json.get("year").getAsInt();
				String director = (json.get("director") == null) ? film.director: json.get("director").getAsString();
				String stars = (json.get("stars") == null) ? film.stars: json.get("stars").getAsString();
				String review = (json.get("review") == null) ? film.title: json.get("review").getAsString();
		
				//Set Updated films data as data passed into from the body content
		        editedFilm.setId(Integer.parseInt(editID));
		        editedFilm.setTitle(title);
		        editedFilm.setYear(year);
		        editedFilm.setDirector(director);
		        editedFilm.setStars(stars);
		        editedFilm.setReview(review);
		        
		        //Call the update film method
		        filmDAO.updateFilm(editedFilm);	
			} catch(Exception e) {
	    		System.out.println(e.getMessage()); //show the error in the console for developers
	    		//create props to show the user what the error is
	    		returnProps.clear();
	    		returnProps.put("Message", "One or more required params could be missing or in the wrong format/data type");
	    		returnProps.put("Server Message", e.getMessage()); // include catch error message
			}
				
        }else if (contentType.equals("text/xml")){
        	//process the body as XML
            HashMap<String, String> filmDetails = new HashMap<String, String>();
        	try {       
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();
                Document doc = documentBuilder.parse(request.getInputStream());
                
                NodeList details = doc.getDocumentElement().getChildNodes();
                
                for(int i = 0; i < details.getLength(); i++) {
                	if(!details.item(i).getNodeName().equals("#text")) {
                		filmDetails.put(details.item(i).getNodeName(), details.item(i).getTextContent());
                		System.out.println(details.item(i).getNodeName() + ": " + details.item(i).getTextContent());
                	}
                }
        	}catch (Exception e){
        		returnProps.put("Server Message", e.getMessage());
        	}
        	
            try {
				//Make sure the updating movie exists
            	editID = filmDetails.get("ID").toString();
				System.out.println(editID);
				Film film = filmDAO.getFilmByID(editID); // get film from ID
				if(film == null) {
					throw new Exception("Film with ID '" + editID + "' does not exsist so it can not be updated");
				}
				
            	//If the JSON attribute does not exist use the existing data, otherwise update
				String title = (filmDetails.get("Title") == null) ? film.title: filmDetails.get("Title").toString();
				Integer year = (filmDetails.get("Year") == null) ? film.year: Integer.parseInt(filmDetails.get("Year").toString());
				String director = (filmDetails.get("Director") == null) ? film.director: filmDetails.get("Director").toString();
				String stars = (filmDetails.get("Stars") == null) ? film.stars: filmDetails.get("Stars").toString();
				String review = (filmDetails.get("Review") == null) ? film.review: filmDetails.get("Review").toString();
		
				//Set Updated films data as data passed into from the body content
		        editedFilm.setId(Integer.parseInt(editID));
		        editedFilm.setTitle(title);
		        editedFilm.setYear(year);
		        editedFilm.setDirector(director);
		        editedFilm.setStars(stars);
		        editedFilm.setReview(review);
	            
		        //Call the update film method
		        filmDAO.updateFilm(editedFilm);	
	    	}catch(Exception e){
	    		System.out.println(e.getMessage()); //show the error in the console for developers
	    		returnProps.clear();
	    		returnProps.put("Message", "One or more required params could be missing or in the wrong format/data type");
	    		returnProps.put("Server Message", e.getMessage()); // include catch error message
	    	}
        }else {
        	returnProps.put("Message", "The server does not yet support contentType of " + contentType);
        }
        
		//if there are no properties to return to the user then there must be no errors so tell them the film as been added
		if(returnProps.size() == 0) {
			returnProps.put("Message", "Movie has been updated in the DB");
			returnProps.put("ID", editID);
		}
        
		//setup output variable
		String output = "";
		
		response.setContentType(accept); 	
		
		//return the data in the requested format
		switch(accept) {
			case "application/json":
				output = HelperMethods.CustomJson(returnProps);
				break;
			case "text/xml":
				//response.setContentType(contentType);
				output = HelperMethods.CustomXML(returnProps);
				break;
			case "text/plain":
				//response.setContentType(contentType);
				output = returnProps.toString();
				break;
			default:
				response.setContentType("application/json");
				output = HelperMethods.CustomJson(returnProps); // Transform Film Object to JSON String if a film is found
		}
				
		response.getWriter().write(output);
	}
	
	/**
	 * Delete Film, takes required parameter of FilmID
	 * DELETE /Films?FilmID - returns FilmID with a message
	 */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Prevent CORS issues in the browser
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		// Get the filmId from URL
		String id = request.getParameter("FilmID");
		
		//get the return type the user needs
		String accept = request.getHeader("Accept");
		//if there is no accept header then set null to a string with the value of null so the switch default works
		if(accept == null) {
			accept = "null";
		}
		
		if(id == null) {
			id = "";
		}
		
		//Create a HashMap to fill in with details of the error or success
        HashMap<String, String> returnProps = new HashMap<String, String>();
	    				
	    try {
	    	//Make sure the updating movie exists
			System.out.println(id);
			Film film = filmDAO.getFilmByID(id); // get film from ID
			if(film == null) {
				throw new Exception("Film with ID '" + id + "' does not exsist so it can not be deleted");
			}
			
			// Call the deleteFilm Method
			FilmDAO filmDAO = new FilmDAO();
			filmDAO.deleteFilmByID(Integer.parseInt(id));
	    }catch (Exception e){
	    	System.out.println(e.getMessage());
	    	returnProps.clear();
	    	//returnProps.put("Message", "One or more required params could be missing or in the wrong format/data type");
			returnProps.put("ID", id.toString());
			returnProps.put("Message", e.getMessage());
	    }
	    
	    //If there is no ID at all, ask the user for a FilmID
	    if(id == null || id.isEmpty() || id.isBlank()) {
	    	returnProps.clear();
			returnProps.put("Message", "Please provide a FilmID to delete");
	    }
	    
	    //if there are no properties to return to the user then there must be no errors so tell them the film as been added
	  	if(returnProps.size() == 0) {
	  		returnProps.put("Message", "Film ["+id.toString()+"] has been deleted from DB");
	  		returnProps.put("ID", id.toString());
	  	}
	  				
	  	//setup output variable
	  	String output = "";
	  				
	  	response.setContentType(accept); 
	  	
	  	//return the data in the requested format
	  	switch(accept) {
	  		case "application/json":
	  			output = HelperMethods.CustomJson(returnProps);
	  			break;
	  		case "text/xml":
	  			//response.setContentType(contentType);
	  			output = HelperMethods.CustomXML(returnProps);
	  			break;
	  		case "text/plain":
	  			//response.setContentType(contentType);
	  			output = returnProps.toString();
	  			break;
	  		default:
	  			response.setContentType("application/json");
	  			output = HelperMethods.CustomJson(returnProps); // Transform Film Object to JSON String if a film is found
	  	}
	  				
	  	response.getWriter().write(output);
	}
	
	

}
