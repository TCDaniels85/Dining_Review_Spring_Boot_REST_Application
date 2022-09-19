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
 * Dining Review class, this creates dining review entities within the database. JPA annotations have been used
 * to map the user object to the database.
 * 
 * Lombok java library has been implemented to minimise code required by automatically generating
 * getter, setter and constructor methods using simple annotations.
 * 
 * @author chris
 */
@Entity
@Table(name="DINING_REVIEW")
@Getter
@Setter
@RequiredArgsConstructor
public class DiningReview {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private long id;
	@Column(name="SUBMISSION_NAME")
	private String submissionName;
	@Column(name="RESTAURANT_ID")
	private Long restaurantId;
	@Column(name="PEANUT_SCORE")
	private Integer peanutScore;
	@Column(name="EGG_SCORE")
	private Integer eggScore;
	@Column(name="DAIRY_SCORE")
	private Integer dairyScore;
	@Column(name="COMMENTS")
	private String comments;
	@Column(name="REVIEW_STATUS")
	private ReviewStatus reviewStatus;
	

}
