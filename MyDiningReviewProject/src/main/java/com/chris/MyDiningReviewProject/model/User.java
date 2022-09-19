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
@Entity
@Table(name="USERS")
@Getter
@Setter
@RequiredArgsConstructor
//@AllArgsConstructor //this works, fields must be final for required args, check when implementing
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private long id;
	@Column(name = "display_name")
	private String displayName;
	@Column(name = "city")
	private String city;
	@Column(name = "county")
	private String county;
	@Column(name = "post_code")
	private String postcode;
	@Column(name = "peanut_allergy")
	private Boolean isInterestedPeanut;
	@Column(name = "egg_allergy")
	private Boolean isInterestedEgg;
	@Column(name = "dairy_allergy")
	private Boolean isInterestedDairy;

}
