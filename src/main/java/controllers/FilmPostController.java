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
 * Servlet implementation class FilmPost
 */
@WebServlet("/FilmPost")
public class FilmPostController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FilmPostController() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Create a new film Object to store update data in
        Film postFilm = new Film(0, null, 0, null, null, null);
        FilmDAO filmDAO = new FilmDAO();
        
        //Get inputs from the form
        Map<String, String[]> params = request.getParameterMap();
        
        //Create Helpers Object
        HelperMethods helpers = new HelperMethods();
        
        //validate the form
        boolean valid = helpers.ValidateParamsForm(params);
        
        if(valid) {
        	try {
                //Set post film data as data passed in from the form if the data is valid
                postFilm.setTitle(request.getParameter("PostFilmTitle"));
                postFilm.setYear(Integer.parseInt(request.getParameter("PostFilmYear")));
                postFilm.setDirector(request.getParameter("PostFilmDirector"));
                postFilm.setStars(request.getParameter("PostFilmStars"));
                postFilm.setReview(request.getParameter("PostFilmReview"));
                
                //Call the update film method
                filmDAO.PostFilm(postFilm);
        	}catch (Exception e) {
        		System.out.println(e.getMessage()); //show the error in the console for developers
        		response.sendRedirect("/FilmProject/error.html"); //show the error page for users
            	return;
        	}

        }else {
        	//redirect to error page if the form is not valid
        	System.out.println("Form valid status: " + valid);
        	response.sendRedirect("/FilmProject/error.html");
        	return;
        }
        
        //Go back to the web interface controller when film is posted to database
        response.sendRedirect("/FilmProject/webInterface");
	}

}
