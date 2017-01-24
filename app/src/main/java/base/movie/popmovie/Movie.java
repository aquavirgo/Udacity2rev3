package base.movie.popmovie;

import android.os.Parcel;

/**
 * Created by Jakub on 2017-01-14.
 */

public class Movie {


    int    id;
    String title;
    String originalTitle;
    String overview;
    String popularity;
    String releaseDate;
    String posterPath;
    String backdropPath;
    String originalLanguage;
    String voteAverage;
    String reviewAuthor;
    String reviewContent;
    String trailerTitle;
    String trailerKey;




    public Movie() {
    }
    private Movie(Parcel parcel){
        this.id = parcel.readInt();
        this.title = parcel.readString();
        this.originalTitle = parcel.readString();
        this.backdropPath = parcel.readString();
        this.posterPath = parcel.readString();
        this.overview = parcel.readString();
        this.voteAverage = parcel.readString();
        this.releaseDate = parcel.readString();
        //Movie Review
        this.reviewAuthor = parcel.readString();
        this.reviewContent = parcel.readString();
        //Movie Traile
        this.trailerTitle = parcel.readString();
        this.trailerKey = parcel.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String original_title) {
        this.originalTitle = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }



    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String release_date) {
        this.releaseDate = release_date;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String poster_path) {
        this.posterPath = poster_path;
    }



    public void setBackdropPath(String backdrop_path) {
        this.backdropPath = backdrop_path;
    }



    public void setOriginalLanguage(String original_language) {
        this.originalLanguage = original_language;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String vote_average) {
        this.voteAverage = vote_average;
    }


    public String getReviewAuthor(){return reviewAuthor;}
    public void setReviewAuthor(String reviewAuthor) {this.reviewAuthor = reviewAuthor;}


    public String getReviewContent(){return reviewContent;}
    public void setReviewContent(String reviewContent) {this.reviewContent = reviewContent;}

    public String getTrailerTitle(){return trailerTitle;}
    public void setTrailerTitle(String trailerTitle) {this.trailerTitle = trailerTitle;}

    public String getTrailerKey(){return trailerKey;}
    public void setTrailerKey(String trailerKey) {this.trailerKey = trailerKey;}

}
