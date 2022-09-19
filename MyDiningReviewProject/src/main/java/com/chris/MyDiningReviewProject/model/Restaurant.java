package com.chris.MyDiningReviewProject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


/**
 * User class, this creates user entities within the database. JPA annotations have been used
 * to map the user object to the database.
 * 
 * Lombok java library has been implemented to minimise code required by automatically generating
 * getter, setter and constructor methods using simple annotations.
 * 
 * @author chris
 */
@Entity //Defines class as an entity to map to database
@Table(name="RESTAURANT")  //Define database name
@Getter //auto generate getter methods
@Setter //auto generate setter methods
@RequiredArgsConstructor  //auto generate constructor with one parameter for each field
public class Restaurant {	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //defines field as an id, generates unique id automatically
	private long id;
	@Column(name="NAME")//defines column names
	private String name;
	@Column(name="ADDRESS")
	private String address;
	@Column(name="POSTCODE")
	private String postcode;
	@Column(name="TEL_NUMBER")
	private String telNumber;
	@Column(name="EMAIL")
	private String email;
	@Column(name="WEBSITE")
	private String website;	
	@Column(name="OVERALL_SCORE")
	private double overallReviewScore;
	@Column(name="PEANUT_SCORE")
	private double peanutScore;
	@Column(name="DAIRY_SCORE")
	private double dairyScore;
	@Column(name="EGG_SCORE")
	private double eggScore;
	@Column(name="PEANUT_REVIEWS")
	private int numOfPeanutReviews;
	@Column(name="DAIRY_REVIEWS")
	private int numOfDairyReviews;
	@Column(name="EGG_REVIEWS")
	private int numOfEggReviews;
	@Column(name="TOTAL_REVIEWS")
	private int totalReviews;

}
