package common;

import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import models.Film;

public class HelperMethods {
	private boolean validForm;
	private static Gson gson = new Gson();
	
	//Validate the input variables from a form
    public boolean ValidateParamsForm(Map<String, String[]> params) {
    	//assume the form is not valid
    	validForm = false;
    	
    	//loop each param to cheack if it is valid
    	params.forEach((key, value) -> {
    		//make sure the inputs are not empty
    		if(!value[0].equals("") || !value[0].isEmpty() || !value[0].isBlank()) {
    			//check if year is a number
    			if(key.toLowerCase().contains("year") || key.toLowerCase().contains("id")) {
    				try {
    					Integer.parseInt(value[0]);
    					//if the year can be parsed to a number set the form to valid
    					validForm = true;
    				}catch (Exception e) {
    					//if the form can not be parsed to a number set the form to invalid and escape loop
    					validForm = false;
    					return; // end the loop
    				}
    			}
    		}else {
    			//if any of the inputs are empty then set the form to invalid and escape the loop
    			validForm = false;
    			return; // end the loop
    		}
    	});
    	
    	return validForm;
    }
    
    public static String CustomJson(HashMap<String, String> propperties) {
        Type gsonType = new TypeToken<HashMap>(){}.getType();
        String gsonString = gson.toJson(propperties, gsonType);
    	return gsonString;
    }
    
    public static String CustomXML(HashMap<String, String> propperties) {
    	try {
        	DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder build = dFact.newDocumentBuilder();
            Document doc = build.newDocument();
            
            //create the root element
            Element root = doc.createElement("Result");
            doc.appendChild(root);
            
            
            propperties.forEach((key, value) -> {
                //Add all elements of a film to the XML document
                Element element = doc.createElement(key.replaceAll(" ", ""));
                element.appendChild(doc.createTextNode(String.valueOf(value)));
                root.appendChild(element);
            });
            

            //Create as a Document Object Model
            DOMSource source = new DOMSource(doc);

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer); // this is where the XML document will be stored
            
            //Format the XML document ready to send to the user
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //add the formating to the DOM source and save in StreamResult
            transformer.transform(source, result);
            
            return writer.toString(); // return the DOM as a string
    	}catch (Exception e) {
    		System.out.println(e);
    		return e.toString(); // return the error
    	}
    }
    
    public static String ParseToXML(Film film) {
    	// I have manually created an XML document because I was getting a ClassNotFound error with Jaxb and Xstream which I could not resolve
    	// This alternative solution was inspired by https://stackoverflow.com/questions/21125998/how-to-write-an-arraylist-to-an-xml-file
    	try {
        	DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder build = dFact.newDocumentBuilder();
            Document doc = build.newDocument();
            
            //create the root element
            Element root = doc.createElement("Film");
            doc.appendChild(root);
            
            //Add all elements of a film to the XML document
            Element id = doc.createElement("ID");
            id.appendChild(doc.createTextNode(Integer.toString(film.getId())));
            root.appendChild(id);
            
            Element title = doc.createElement("Title");
            title.appendChild(doc.createTextNode(film.getTitle()));
            root.appendChild(title);

            Element year = doc.createElement("Year");
            year.appendChild(doc.createTextNode(Integer.toString(film.getYear())));
            root.appendChild(year);

            Element director = doc.createElement("Director");
            director.appendChild(doc.createTextNode(film.getDirector()));
            root.appendChild(director);
            
            Element stars = doc.createElement("Stars");
            stars.appendChild(doc.createTextNode(film.getStars()));
            root.appendChild(stars);
            
            Element review = doc.createElement("Review");
            review.appendChild(doc.createTextNode(String.valueOf(film.getReview().replaceAll("[\n\r]", "")))); //remove carrige return
            root.appendChild(review);
            

            //Create as a Document Object Model
            DOMSource source = new DOMSource(doc);

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer); // this is where the XML document will be stored
            
            //Format the XML document ready to send to the user
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //add the formating to the DOM source and save in StreamResult
            transformer.transform(source, result);
            
            return writer.toString(); // return the DOM as a string
    	}catch (Exception e) {
    		System.out.println(e);
    		return e.toString(); // return the error
    	}
    }
    
    public static String ParseToXML(ArrayList<Film> films) {
    	// I have manually created an XML document because I was getting a ClassNotFound error with Jaxb and Xstream which I could not resolve
    	// This alternative solution was inspired by https://stackoverflow.com/questions/21125998/how-to-write-an-arraylist-to-an-xml-file
    	try {
        	DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder build = dFact.newDocumentBuilder();
            Document doc = build.newDocument();
            
            Element root = doc.createElement("Films");
            doc.appendChild(root);

            for (Film film : films) {
            	
                Element Film = doc.createElement("Film");
                root.appendChild(Film);
            	
                Element id = doc.createElement("ID");
                id.appendChild(doc.createTextNode(String.valueOf(Integer.toString(film.getId()))));
                Film.appendChild(id);

                Element title = doc.createElement("Title");
                title.appendChild(doc.createTextNode(String.valueOf(film.getTitle())));
                Film.appendChild(title);
                
                Element year = doc.createElement("Year");
                year.appendChild(doc.createTextNode(String.valueOf(Integer.toString(film.getYear()))));
                Film.appendChild(year);

                Element director = doc.createElement("Director");
                director.appendChild(doc.createTextNode(String.valueOf(film.getDirector())));
                Film.appendChild(director);
                
                Element stars = doc.createElement("Stars");
                stars.appendChild(doc.createTextNode(String.valueOf(film.getStars())));
                Film.appendChild(stars);
                
                Element review = doc.createElement("Review");
                review.appendChild(doc.createTextNode(String.valueOf(film.getReview().replaceAll("[\n\r]", "")))); //remove carriage returns
                Film.appendChild(review);

            }
            
           
            //Create as a Document Object Model
            DOMSource source = new DOMSource(doc);

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer); // this is where the XML document will be stored
            
            //Format the XML document ready to send to the user
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //add the formating to the DOM source and save in StreamResult
            transformer.transform(source, result);
            
            return writer.toString(); // return the DOM as a string
    	}catch (Exception e) {
    		System.out.println(e);
    		return e.toString(); // return the error
    	}
    }
}
