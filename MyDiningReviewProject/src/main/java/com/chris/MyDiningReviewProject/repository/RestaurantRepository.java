package com.chris.MyDiningReviewProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.chris.MyDiningReviewProject.model.Restaurant;


/**
 * Restaurant repository interface to make use of the CRUD repository methods. Custom queries
 * required by the application have been added to this interface.
 * @author Chris Daniels
 */
public interface RestaurantRepository  extends CrudRepository<Restaurant, Long> {
	Optional<Restaurant> findByName(String name);
	Optional<Restaurant> findByPostcode(String postode);
	Optional<Restaurant> findByNameAndPostcode(String name,String postode);
	List<Restaurant> findByPostcodeAndPeanutScoreNotNullOrderByPeanutScoreDesc(String postcode);
	List<Restaurant> findByPostcodeAndEggScoreNotNullOrderByEggScoreDesc(String postcode);
	List<Restaurant> findByPostcodeAndDairyScoreNotNullOrderByDairyScoreDesc(String postcode);
	

}
