package controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Film;
import models.FilmDAO;

import java.util.ArrayList;

/**
 * Servlet implementation class TestServerlet
 */
@WebServlet("/webInterface")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public HomeController() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get all the films and save in local ArrayList Variable
		FilmDAO filmDAO = new FilmDAO();
		ArrayList<Film> films = filmDAO.getAllFilms();
		
		// Send all film results to the View as an attribute
		request.setAttribute("listFilms", films);
		
		// Set view as film-list.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("film-list.jsp");
		dispatcher.forward(request, response);
	}
}
