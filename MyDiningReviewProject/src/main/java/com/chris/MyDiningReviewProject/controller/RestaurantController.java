package com.chris.MyDiningReviewProject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.chris.MyDiningReviewProject.model.Restaurant;
import com.chris.MyDiningReviewProject.repository.RestaurantRepository;


/**
 * Controller class containing methods to return data from the data base containing Restaurant entities.
 * @author chris
 *
 */
@RequestMapping("/RESTAURANT") //sets base path for class
@RestController
public class RestaurantController {
	public final RestaurantRepository restaurantRepo;

	private RestaurantController(RestaurantRepository restaurantRepo) {
		super();
		this.restaurantRepo = restaurantRepo;
	}
	
	/**
	 * Returns list of restaurant objects contained in the database
	 * @return Iterable list of restaurant objects in database
	 */
	@GetMapping
	public Iterable<Restaurant> getAllRestaurants(){
		return this.restaurantRepo.findAll();		
	}
	
	/**
	 * Create and add a new restaurant to the database, calls the check unique method to ensure the restaurant 
	 * does not already exist (this method throws an exception if it does), then adds the restaurant.
	 * @param restaurant object to be created
	 * @return the restaurant added
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Restaurant createResaurant(@RequestBody Restaurant restaurantToCreate) {
		checkRestaurantUnique(restaurantToCreate.getName(), restaurantToCreate.getPostcode());		
		Restaurant restToAdd = this.restaurantRepo.save(restaurantToCreate);
		return restToAdd;		
	}
	
	/**
	 * Search for a restaurant by it's unique id, if this does not exist, a response status exception of not found is thrown.
	 * @param restaurant unique id
	 * @return restaurant details of the particular restaurant
	 */
	@GetMapping("/{id}")
	public Restaurant getRestaurant(@PathVariable("id") long id) {
		Optional<Restaurant> restaurantToFind = this.restaurantRepo.findById(id);
		if(restaurantToFind.isPresent()) {
			return restaurantToFind.get();
		} else {		
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Searches for restaurants by postcode, to find all restaurants with a rating for a specific allergy
	 * lists restaurants in descending order
	 * @param postcode to search restaurants by
	 * @param allergy scores user is interested in
	 * @return restaurants belonging to a postcode, with required allergy score, in descending order.
	 */
	@GetMapping("/SEARCH")	//Specifies the search endpoint
	public List<Restaurant> searchRestaurants(@RequestParam (name="postcode", required = false) String postcode, @RequestParam (name="allergy", required = false)String allergy){
		if (postcode.isBlank() || allergy.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //exception thrown either parameter is blank
		}
		if(allergy.equals("peanut")) {			
			return this.restaurantRepo.findByPostcodeAndPeanutScoreNotNullOrderByPeanutScoreDesc(postcode);
		} else if (allergy.equals("egg")) {
			return this.restaurantRepo.findByPostcodeAndEggScoreNotNullOrderByEggScoreDesc(postcode);
		} else if(allergy.equals("dairy")) {
			return this.restaurantRepo.findByPostcodeAndDairyScoreNotNullOrderByDairyScoreDesc(postcode);
		} else {
			return null;
		}
		
				
	}
	
	/**
	 * Checks a restaurant is unique by comparing name and postcodes to ensure these are not the 
	 * same as an already existing restaurant. Throws response status exception if the restaurant is not unique
	 * or parameters are missing
	 * @param name of restaurant to be created
	 * @param postcode of restaurant to be created
	 */
	@ResponseStatus(HttpStatus.CREATED)
	private void checkRestaurantUnique(String name, String postcode) {
		if(name.isBlank() || postcode.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			Optional<Restaurant> checkRest = this.restaurantRepo.findByNameAndPostcode(name,postcode);		
			if(checkRest.isPresent())
				throw new ResponseStatusException(HttpStatus.CONFLICT);					
		}
	}
	
	

}
