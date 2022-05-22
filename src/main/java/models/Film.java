package models;
public class Film {
	//Film properties
	public int id;
	public String title;
	public int year;
	public String director;
	public String stars;
	public String review;
	
	//set film
    public Film(int id, String title, int year, String director, String stars, String review) {
		super();
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
		this.stars = stars;
		this.review = review;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public String getDirector() {
		return director;
	}
	
	public void setDirector(String director) {
		this.director = director;
	}
	
	public String getStars() {
		return stars;
	}
	
	public void setStars(String stars) {
		this.stars = stars;
	}
	
	public String getReview() {
		return review;
	}
	
	public void setReview(String review) {
		this.review = review;
	}
	
	@Override
	public String toString() {
//		return "Film [id=" + id + ", title=" + title + ", year=" + year
//				+ ", director=" + director + ", stars=" + stars + ", review="
//				+ review + "]";
		//this is simlar to CSV but the fields can have commas so I used ;#; as the seperator between fields
		return id + ";#;" + "'"+title+"'"+ ";#;" + year + ";#;" + "'"+director+"'" + ";#;" + "'"+stars+"'" + ";#;" + "'"+review+"'\n";
	}   
}
