package com.chris.MyDiningReviewProject.controller;

import java.util.List;
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

import com.chris.MyDiningReviewProject.model.DiningReview;
import com.chris.MyDiningReviewProject.model.Restaurant;
import com.chris.MyDiningReviewProject.model.ReviewStatus;
import com.chris.MyDiningReviewProject.model.User;
import com.chris.MyDiningReviewProject.repository.DiningReviewRepository;
import com.chris.MyDiningReviewProject.repository.RestaurantRepository;
import com.chris.MyDiningReviewProject.repository.UserRepository;


/**
 * Controller class containing methods to return data from the data base containing Dining Review entities.
 * @author chris
 *
 */
@RequestMapping("/DININGREVIEW") //sets base path for class
@RestController
public class DiningReviewController {
	private final DiningReviewRepository reviewRepo;
	private final UserRepository userRepo;
	private final RestaurantRepository restaurantRepo;

	private DiningReviewController(DiningReviewRepository reviewRepo, RestaurantRepository restaurantRepo, UserRepository userRepo) {
		super();
		this.reviewRepo = reviewRepo;
		this.userRepo = userRepo;		
		this.restaurantRepo = restaurantRepo;
	}
	
	/**
	 * Returns all dining reviews in database
	 * @return Iterable list of dining reviews
	 */
	@GetMapping
	public Iterable<DiningReview> getAllReviews(){
		return this.reviewRepo.findAll();
		
	}
	/**
	 * 
	 * Returns all dining reviews with pending status
	 * @return list of poending dining reviews
	 */
	@GetMapping("/PENDING")
	public List<DiningReview> getPendingReviews(){
		return this.reviewRepo.findByReviewStatus(ReviewStatus.PENDING);		
	}
	
	/**
	 * Creates a dining review, calls methods to check the scores given are valid and that
	 * the submission details are valid. These methods throw exceptions if not. 
	 * @param review to be created
	 * @return the created review
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DiningReview createReview(@RequestBody DiningReview review) {
		isSubmissionValid(review);
		areScoresValid(review);
		Optional<Restaurant> doesExist = this.restaurantRepo.findById(review.getRestaurantId());
		if(!doesExist.isPresent()) 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
						
		review.setReviewStatus(ReviewStatus.PENDING); //adds pending review status to dining review
		DiningReview reviewCreated = this.reviewRepo.save(review);
		return reviewCreated;
		
				
	}
	/**
	 * Checks that the review created is from a registered user(by display name), checks that at least one allergy score 
	 * is not present and that other required fields are not blank. 
	 * @param review that has been validated
	 */
	private void isSubmissionValid(DiningReview review) {
		if(review.getRestaurantId().toString().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if(review.getSubmissionName().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if(review.getDairyScore() == null && review.getEggScore() == null && review.getPeanutScore() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		Optional<User> userToValidate = this.userRepo.findByDisplayName(review.getSubmissionName());
		if(!userToValidate.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
				
	}
	
	/**
	 * Checks that the scores given by the user are valid (between 1 and 5), throws an exception otherwise
	 * @param review being validated
	 */
	private void areScoresValid(DiningReview review) {
		if(review.getDairyScore()!=null) {
			if(review.getDairyScore() > 5 || review.getDairyScore() <=0) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		if(review.getEggScore()!=null) {
			if(review.getEggScore() > 5 || review.getEggScore() <=0) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		if(review.getPeanutScore()!=null) {
			if(review.getPeanutScore() > 5 || review.getPeanutScore() <=0) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	/**
	 * Aloows an admin to approve or reject a review, upon approval the average scores are updated (via a call to the 
	 * appropriate method) and the review status is updated.
	 * @param id of dining review to be updated
	 * @param adminReview whether the review has been accepted/rejected
	 * @return dining review that has been updated
	 */
	@PutMapping("/{id}")
	public DiningReview adminAction(@PathVariable("id") long id, @RequestBody DiningReview adminReview) {
		Optional<DiningReview> review = this.reviewRepo.findById(id);
		if(!review.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		DiningReview updateReview = review.get();
		DiningReview reviewed = null; //creates a null review object that can be returned at the end of the method (after being passed the appropriate value)
		
		if(adminReview.getReviewStatus().equals(ReviewStatus.ACCEPTED))	{			
			updateAverageScore(updateReview);
			updateReview.setReviewStatus(ReviewStatus.ACCEPTED);
			reviewed = this.reviewRepo.save(updateReview);
			
		} else if (adminReview.getReviewStatus().equals(ReviewStatus.REJECTED)) {
			updateReview.setReviewStatus(ReviewStatus.REJECTED);
			reviewed = this.reviewRepo.save(updateReview);
		}		
		return reviewed;
		
	}
	
	/**
	 * Updates the restaurants average review scores depending on what scores have been entered in the review
	 * that has just been accepted.
	 * @param review to be accepted
	 */
	private void updateAverageScore(DiningReview review) {
		Optional<Restaurant> rest = this.restaurantRepo.findById(review.getRestaurantId());
		Restaurant restaurant = rest.get();
		
		double peanutAvg = restaurant.getPeanutScore();
		int peanutReviews = restaurant.getNumOfPeanutReviews();
		double dairyAvg = restaurant.getDairyScore();
		int dairyReviews = restaurant.getNumOfDairyReviews();
		double eggAvg = restaurant.getEggScore();
		int eggReviews = restaurant.getNumOfEggReviews();
		
		//Checks if score has been submitted and updates the averages accordingly
		if(review.getPeanutScore() != null) {	
			
			peanutAvg = ((peanutAvg * peanutReviews) + review.getPeanutScore())/(peanutReviews += 1);
			peanutAvg = Math.round(peanutAvg*100);
			peanutAvg = peanutAvg/100;
			restaurant.setNumOfPeanutReviews(peanutReviews);
			restaurant.setPeanutScore(peanutAvg);
		}
		
		if(review.getDairyScore() != null) {
			
			dairyAvg = ((dairyAvg * dairyReviews) + review.getDairyScore())/(dairyReviews += 1);
			dairyAvg = Math.round(dairyAvg*100);
			dairyAvg = dairyAvg/100;
			restaurant.setNumOfDairyReviews(dairyReviews);
			restaurant.setDairyScore(dairyAvg);
		}
		if(review.getEggScore() !=null) {
			
			eggAvg = ((eggAvg * eggReviews) + review.getEggScore())/(eggReviews += 1);
			eggAvg = Math.round(eggAvg*100);
			eggAvg = eggAvg/100;
			restaurant.setNumOfEggReviews(eggReviews);
			restaurant.setEggScore(eggAvg);
		}
		
		//update total reviews average
		int totalReviews = peanutReviews + dairyReviews + eggReviews;
		double overallAvg = ((peanutAvg * peanutReviews) + (dairyAvg * dairyReviews) + (eggAvg * eggReviews))/(totalReviews);
		overallAvg = Math.round(overallAvg * 100);
		overallAvg = overallAvg/100;
		
		restaurant.setOverallReviewScore(overallAvg);
		restaurant.setTotalReviews(totalReviews);
		//saves updated record, may want to return??
		 Restaurant updated = this.restaurantRepo.save(restaurant);		
	}

}
