package com.devsuperior.movieflix.dto;

import com.devsuperior.movieflix.projections.MovieCardProjection;

public class MovieCardDTO {

    private Long id;
    private String title;
    private String subTitle;
    private Integer movieYear;
    private String imgUrl;

    public MovieCardDTO(Long id, String title, String subTitle, Integer movieYear, String imgUrl) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.movieYear = movieYear;
        this.imgUrl = imgUrl;
    }

    public MovieCardDTO(MovieCardProjection m) {
        this.id = m.getId();
        this.title = m.getTitle();
        this.subTitle = m.getSubTitle();
        this.movieYear = m.getMovieYear();
        this.imgUrl = m.getImgUrl();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Integer getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(Integer movieYear) {
        this.movieYear = movieYear;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
