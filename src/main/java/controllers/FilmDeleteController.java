package controllers;


import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.HelperMethods;
import models.FilmDAO;

/**
 * Servlet implementation class FilmDelete
 */
@WebServlet("/deleteFilm")
public class FilmDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FilmDeleteController() {
        super();
        // TODO Auto-generated constructor stub	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get the filmId from URL
		FilmDAO filmDAO = new FilmDAO();
		
		//Get inputs from the form
        Map<String, String[]> params = request.getParameterMap();
        
		//Create Validation Object
        HelperMethods helpers = new HelperMethods();
        
        //validate the form
        boolean valid = helpers.ValidateParamsForm(params);
		
        if(valid) {
        	try {
        		// Call the deleteFilm Method
        		filmDAO.deleteFilmByID(Integer.parseInt(request.getParameter("FilmID")));
        	}catch (Exception e) {
        		System.out.println(e);
        		response.sendRedirect("/FilmProject/error.html");
            	return;
        	}
        }else {
        	//redirect to error page if the form is not valid
        	System.out.println("Form valid status: " + valid);
        	response.sendRedirect("/FilmProject/error.html");
        	return;
        }

		
		//Go back to the WebInterface Controller
		response.sendRedirect("/FilmProject/webInterface");
	}
}
