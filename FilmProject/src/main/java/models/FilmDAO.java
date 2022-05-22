package models;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;


public class FilmDAO {
	
	Film oneFilm = null;
	Connection conn = null;
    Statement stmt = null;
    PreparedStatement preparedStmt = null;
	String user = "";
    String password = "";
    String url = "jdbc:mysql:/:6306/"+user;
    
	public FilmDAO() {}

	private void openConnection(){
		// loading jdbc driver for mysql
		try{
		    Class.forName("com.mysql.jdbc.Driver");
		} catch(Exception e) { 
			System.out.println(e); 
		}

		// connecting to database
		try{
			// connection string for demos database, username demos, password demos
 			conn = DriverManager.getConnection(url, user, password);
		    stmt = conn.createStatement();
		} catch(SQLException se) { 
			System.out.println(se); 
		}	   
    }
	
	private void closeConnection(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Film getNextFilm(ResultSet rs){
    	Film thisFilm=null;
    	
		try {
			thisFilm = new Film(
					rs.getInt("id"),
					rs.getString("title"),
					rs.getInt("year"),
					rs.getString("director"),
					rs.getString("stars"),
					rs.getString("review"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return thisFilm;		
	}
	
	// === GET all films ===
	public ArrayList<Film> getAllFilms(){
		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();
		
		try{
			// Create select statement and execute it
			String selectSQL = "select * from films";
		    ResultSet result = stmt.executeQuery(selectSQL);
		    
		    // Retrieve the results and add them to the ArrayList of films
		    while(result.next()){
		    	oneFilm = getNextFilm(result);
		    	allFilms.add(oneFilm);
		    }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { 
			System.out.println(se); 
		}

	   return allFilms;
	}

	// === GET film byID ===
	public Film getFilmByID(String filmId){
		oneFilm=null;
		openConnection();
	    
		try{
			// Create select statement and execute it
		    String selectSQL = "select * from films where id="+filmId;
		    ResultSet result = stmt.executeQuery(selectSQL);
		    
		    // Get the film from the result and place into Film object
		    while(result.next()){
		    	oneFilm = getNextFilm(result);
		    }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return oneFilm;
   }
   
   // === DELETE film by ID === 
   public int deleteFilmByID(int filmId) {
	   int status = 0;
	   openConnection();
	   
	   try {
		   //create delete statement and execute
		   String deleteSQL = "delete from films where id="+filmId;
		   status = stmt.executeUpdate(deleteSQL);
		   
		   stmt.close();
		   closeConnection();
	   }catch(SQLException se) {
		   System.out.println(se);
	   }
	   
	   return status;
   }
   
   // === UPDATE film ===
   public int updateFilm(Film filmObj) {
	   int status = 0;
       openConnection();
      
       try {
    	   //create update statement and execute
           String updateSQL = "UPDATE films SET title=?, year=?, director=?, stars=?, review=? WHERE id=?";
           
           preparedStmt = conn.prepareStatement(updateSQL);
           preparedStmt.setString(1, filmObj.getTitle());
           preparedStmt.setInt(2, filmObj.getYear());
           preparedStmt.setString(3, filmObj.getDirector());
           preparedStmt.setString(4, filmObj.getStars());
           preparedStmt.setString(5, filmObj.getReview());
           preparedStmt.setInt(6, filmObj.getId());
           
           status = preparedStmt.executeUpdate();
           
		   stmt.close();
		   closeConnection();
       } catch (SQLException se) {
           System.out.println(se);
       }
       
       return status;
   }
   
   public boolean PostFilm(Film filmObj) {
	   boolean status = false;
       openConnection();

       try {
    	   //create update statement and execute (ID will be set by the database (Auto Increment))
           String insertSQL = "insert into films( title,year,director,stars,review) values (?, ?, ?, ?, ?)";
           
           preparedStmt = conn.prepareStatement(insertSQL);
           preparedStmt.setString(1, filmObj.getTitle());
           preparedStmt.setInt(2, filmObj.getYear());
           preparedStmt.setString(3, filmObj.getDirector());
           preparedStmt.setString(4, filmObj.getStars());
           preparedStmt.setString(5, filmObj.getReview());
           
           status = preparedStmt.execute();
           
		   stmt.close();
		   closeConnection();
       } catch (SQLException se) {
           System.out.println(se);
       }
       
       return status;
   }
}
