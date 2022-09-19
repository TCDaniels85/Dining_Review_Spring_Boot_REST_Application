package com.chris.MyDiningReviewProject.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.chris.MyDiningReviewProject.model.User;
import com.chris.MyDiningReviewProject.repository.UserRepository;


/**
 * Controller class containing methods to return data from the data base containing user entities.
 * Experimented with passing ResponseStatusException messages with status codes in this class
 * @author chris
 *
 */
@RequestMapping("/USERS") //endpoint to set the base path for this class 
@RestController
public class UserController {
	private final UserRepository userRepo;

	public UserController(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}
	
	/**
	 * Returns all users, used for testing purposes
	 * @return list of user objects
	 */
	@GetMapping
	public Iterable<User> getAllUsers(){
		return this.userRepo.findAll();
	}
	
	/**
	 * Creates a new user, validates display name to ensure it is 
	 * unique. Returns a user object or throws exception depending on validation failure
	 * @param user object 
	 * @return created user object
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User createNewUser(@RequestBody User user) {
		validateUserName(user);		
		User newUser = this.userRepo.save(user);
		return newUser;
		
	}
	
	/**
	 * Finds a user in the database using the display name entered.
	 * Returns a user object or throws exception depending on validation
	 * @param displayName entered by user
	 * @return User object if found in the the database
	 */
	@GetMapping("/{displayName}")
	public User getUserByDisplayName(@PathVariable String displayName) {		
		Optional<User> userToFind = this.userRepo.findByDisplayName(displayName);
		if(!userToFind.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
		return userToFind.get();
	}
	
	/**
	 * Updates a user's details, validates the user exists by passing the displayname to the 
	 * getUserByDisplayName method.
	 * @param user display name to update
	 * @param user object containing details to be updated
	 * @return updated user
	 */
	@PutMapping("/{displayName}")
	public User updateUser(@PathVariable("displayName") String displayName, @RequestBody User user) {
		User userToUpdate = getUserByDisplayName(displayName); //returns user entity using the display name entered
		//checks the entity fields present, and updates the fields that are not null
		if(user.getCity() != null) {
			userToUpdate.setCity(user.getCity());
		}
		if(user.getCounty() !=null) {
			userToUpdate.setCounty(user.getCounty());
		}
		if(user.getPostcode() !=null) {
			userToUpdate.setPostcode(user.getPostcode());
		}
		if(user.getIsInterestedPeanut() != null) {
			userToUpdate.setIsInterestedPeanut(user.getIsInterestedPeanut())	;		
		}
		if(user.getIsInterestedEgg() != null) {
			userToUpdate.setIsInterestedEgg(user.getIsInterestedEgg())	;		
		}
		if(user.getIsInterestedDairy() != null) {
			userToUpdate.setIsInterestedDairy(user.getIsInterestedDairy())	;		
		}
		User updatedUser = this.userRepo.save(userToUpdate);
		return updatedUser;
	}
	
	/**
	 * Checks if the created display name exists in the database, if it does the method throws a
	 * response status exception. Exception is also thrown if the display name entered is blank.
	 * @param user object
	 */
	private void validateUserName(User user) {
		Optional<User> isDisplayNameUnique = this.userRepo.findByDisplayName(user.getDisplayName());//looks for display name in database
		if(user.getDisplayName().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Display name is blank");
		} else if (isDisplayNameUnique.isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Display name already exists");
		}		
	}
	
	

}
