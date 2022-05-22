package controllers;


import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.HelperMethods;
import models.Film;
import models.FilmDAO;
/**
 * Servlet implementation class FilmUpdate
 */
@WebServlet("/edit")
public class FilmUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FilmUpdateController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: There is no get method");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Create a new film Object to store update data in
        Film editedFilm = new Film(0, null, 0, null, null, null);
        FilmDAO filmDAO = new FilmDAO();
        
        //Get inputs from the form
        Map<String, String[]> params = request.getParameterMap();
        
        //Create Validation Object
        HelperMethods helpers = new HelperMethods();
        
        //validate the form
        boolean valid = helpers.ValidateParamsForm(params);
        
        if(valid) {
        	try {
                //Set Updated films data as data passed in from the form
                editedFilm.setId(Integer.parseInt(request.getParameter("FilmID")));
                editedFilm.setTitle(request.getParameter("FilmTitle"));
                editedFilm.setYear(Integer.parseInt(request.getParameter("FilmYear")));
                editedFilm.setDirector(request.getParameter("FilmDirector"));
                editedFilm.setStars(request.getParameter("FilmStars"));
                editedFilm.setReview(request.getParameter("FilmReview"));
                
                //Call the update film method
                filmDAO.updateFilm(editedFilm);
        	}catch (Exception e) {
        		System.out.println(e.getMessage());
            	response.sendRedirect("/FilmProject/error.html");
            	return;
        	}
        }else {
        	//redirect to error page if the form is not valid
        	System.out.println("Form valid status: " + valid);
        	response.sendRedirect("/FilmProject/error.html");
        	return;
        }


        //Go back to the web interface controller
        response.sendRedirect("/FilmProject/webInterface");
    }


}
