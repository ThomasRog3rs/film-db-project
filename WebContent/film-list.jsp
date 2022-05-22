<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Page styles -->
    <link rel="stylesheet" href="https://bootswatch.com/5/lux/bootstrap.css">
    <link rel="stylesheet" href="https://bootswatch.com/_vendor/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://bootswatch.com/_vendor/prismjs/themes/prism-okaidia.css">
    <link rel="stylesheet" href="https://bootswatch.com/_assets/css/custom.min.css">
    <!-- DataTables styles -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
    <!-- Custom styles -->
    <link rel="stylesheet" href="/FilmProject/styles/style.css">
    <title>Web Interface</title>
</head>
<body>
    <div class="navbar navbar-expand-lg fixed-top navbar-light bg-light">
        <div class="container">
          <a href="/FilmProject" class="navbar-brand">FilmDB</a>
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarResponsive">
                        <ul class="navbar-nav">
            	<li class="nav-item"><a href="/FilmProject/webInterface" class="nav-link">Web Interface</a></li>
                <li class="nav-item"><a href="/FilmProject/ajaxTestInterface.html" class="nav-link">AJAX Testing</a></li>
                <li class="nav-item"><a href="/FilmProject/ajaxInterface.html" class="nav-link">AJAX Interface</a></li>
            </ul>
          </div>
        </div>
    </div>
    <div class="container">
        <h1 style="text-transform: none;">Web interface</h1>
    	<p>In this view you have access to the data using simple http web service calls with CRUD implemented, interfacing with a MySQL database. This is done with HTML forms being submitted to specific servlets using the 'action' attribute on the form tags.</p>
    	<hr>
    	<button type="button" class="btn btn-success mb-4" id="postModalBtn" data-bs-toggle="modal" data-bs-target="#postModal">Add New Film</button>
    	
        <table id="filmsTables" class="table table-hover border mt-4 display">
            <thead>
                <tr>
                    <th scope="col">Film ID</th>
                    <th scope="col">Film Title</th>
                    <th scope="col">Film Year</th>
                    <th scope="col">Film Director</th>
                    <th scope="col">Film Stars</th>
                    <th scope="col">Film review</th>
                    <th scope="col">Edit Film</th>
                    <th scope="col">Delete Film</th>
                </tr>
            </thead>
            <tbody>
            	<c:forEach var="film" items="${listFilms}">
					<tr>
					    <td>${film.id}</td>
					    <td>${film.title}</td>
					    <td>${film.year}</td>
					    <td>${film.director}</td>
					    <td>${film.stars}</td>
					    <td>${film.review}</td>
					    
	                <td><button type="button" class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#editModal">Edit</button></td>
	                <td><button type="button" class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal">Delete</button></td>
				  	</tr>
			  </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="container">
        <footer id="footer">
            <div class="row">
              <div class="col-lg-12">
               <ul class="list-unstyled">
                  <li class="float-end"><a href="#top">Back to top</a></li>
                  <li><a href="/FilmProject/webInterface">Web Interface</a></li>
                  <li><a href="/FilmProject/ajaxTestInterface.html">AJAX Testing</a></li>
                  <li><a href="/FilmProject/ajaxInterface.html">AJAX Interface</a></li>
                </ul>
                <p>Made by <span class="text-info">Thomas Rogers</a>.</span></p>
                <p>Code developed for MMU <span class="text-info">Web Services Assignment</span>.</p>
    
              </div>
            </div>
          </footer>
    </div>
      
    <!-- page scripts for my chosen bootstrap ui framework theme (from bootswatch.com) -->
    <script src="https://bootswatch.com/_vendor/jquery/dist/jquery.min.js"></script>
    <script src="https://bootswatch.com/_vendor/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://bootswatch.com/_vendor/prismjs/prism.js" data-manual></script>
    <script src="https://bootswatch.com/_assets/js/custom.js"></script>

    <!-- DataTables Jquery plugin for a table UI -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
	<script type="text/javascript" src="http://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
	
	<!-- Bootstrap JavaScript Bundle with Popper -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>

<!-- Edit Modal -->
<div class="modal fade" id="editModal">
    <div class="modal-dialog modal-dialog-scrollable modal-lg">
      <div class="modal-content">
  
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title">Edit Film</h4>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
  
        <!-- Modal body -->
        <div class="modal-body mb-4">
          <!-- Form to edit films -->	
          <form action="/FilmProject/edit" method="POST" id="editFilmForm">
            <div class="form-group">
                <label class="form-label" for="FilmID">Film ID</label>
                <input class="form-control" id="FilmID" type="text" value="" readonly="" name="FilmID" required>
            </div>
  
            <div class="form-group">
                <label class="col-form-label mt-4" for="FilmTitle">Film Title</label>
                <input type="text" class="form-control" placeholder="Film Title" id="FilmTitle" name="FilmTitle" required>
            </div>

            <div class="form-group">
                <label class="col-form-label mt-4" for="FilmYear">Film Year</label>
                <input type="number" class="form-control" placeholder="Film Year" id="FilmYear" name="FilmYear" required>
            </div>

            <div class="form-group">
                <label class="col-form-label mt-4" for="FilmDirector">Film Director</label>
                <input type="text" class="form-control" placeholder="Film Director" id="FilmDirector" name="FilmDirector" required>
            </div>

            <div class="form-group">
                <label class="col-form-label mt-4" for="FilmStars">Film Stars</label>
                <input type="text" class="form-control" placeholder="Film Stars" id="FilmStars" name="FilmStars" required>
            </div>
            
            <div class="form-group">
            	<label class="col-form-label mt-4" for="FilmReview">Film Review</label>
			 	<textarea class="form-control" placeholder="Film Review" id="FilmReview" name="FilmReview" rows="4" required></textarea>
			</div>

            <div class="form-group mt-4">
                <button type="submit" class="btn btn-primary pull-right" id="editFilmBtn">Submit</button>
            </div>
          </form>
        </div>  
      </div>
    </div>
  </div> 
  
<!-- Post Modal -->
<div class="modal fade" id="postModal">
    <div class="modal-dialog modal-dialog-scrollable modal-lg">
      <div class="modal-content">
  
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title">Create a new film</h4>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
  
        <!-- Modal body -->
        <div class="modal-body mb-4">
          <!-- Form to add new forms -->	
          <form action="/FilmProject/FilmPost" method="POST" id="createNewFilm">  
            <div class="form-group">
                <label class="col-form-label" for="PostFilmTitle">Film Title</label>
                <input type="text" class="form-control" placeholder="Film Title" id="PostFilmTitle" name="PostFilmTitle" required>
            </div>

            <div class="form-group">
                <label class="col-form-label mt-4" for="PostFilmYear">Film Year</label>
                <input type="number" class="form-control" placeholder="Film Year" id="PostFilmYear" name="PostFilmYear" required>
            </div>

            <div class="form-group">
                <label class="col-form-label mt-4" for="PostFilmDirector">Film Director</label>
                <input type="text" class="form-control" placeholder="Film Director" id="PostFilmDirector" name="PostFilmDirector" required>
            </div>

            <div class="form-group">
                <label class="col-form-label mt-4" for="PostFilmStars">Film Stars</label>
                <input type="text" class="form-control" placeholder="Film Stars" id="PostFilmStars" name="PostFilmStars" required>
            </div>
            
            <div class="form-group">
            	<label class="col-form-label mt-4" for="PostFilmReview">Film Review</label>
			 	<textarea class="form-control" placeholder="Film Review" id="PostFilmReview" name="PostFilmReview" rows="4" required></textarea>
			</div>

            <div class="form-group mt-4">
                <button type="submit" class="btn btn-primary pull-right" id="postNewFilmBtn">Submit</button>
            </div>
          </form>
        </div>  
      </div>
    </div>
  </div> 

  
<!-- Delete Modal -->
<div class="modal fade" id="deleteModal">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Delete Film</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
            <span aria-hidden="true"></span>
          </button>
        </div>
        <div class="modal-body">
          <p>Are you sure you want to delete <span id="FilmDisplayNameDelete" class="text-info">[Film Name]</span>. This action can not be undone</p>
          <!-- Form to delete films -->
          <form action="/FilmProject/deleteFilm">
          	<div class="form-group">
             	<label class="form-label" for="FilmID">Film ID</label>
                <input class="form-control" id="FilmIDDelete" type="text" readonly="" name="FilmID" required>
            </div>
            <div class="form-group mt-4">
                <button type="submit" class="btn btn-danger pull-right">Delete</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
  
  <div class="toast border-danger" id="formError" style="position: fixed; top: 10px; right: 10px; z-index: 9999;">
    <div class="toast-header">
    	<h6>There was a form error</h6>
    </div>
    <div class="toast-body">
      Please ensure the form is correctly filled in before submitting
    </div>
  </div>
  
  <!-- Import the ajax libary for GET, POST, PUT and DELETE requests, in this case I will only use it to get details to fill in forms for delete and update -->
  <script src="/FilmProject/scripts/ajaxLibrary.js"></script>
  <!-- Page specific js -->
  <script src="/FilmProject/scripts/webInterface.js"></script>

</body>
</html>