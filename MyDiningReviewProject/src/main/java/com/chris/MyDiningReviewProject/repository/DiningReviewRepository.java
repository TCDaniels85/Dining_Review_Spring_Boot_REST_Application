package com.chris.MyDiningReviewProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.chris.MyDiningReviewProject.model.DiningReview;
import com.chris.MyDiningReviewProject.model.ReviewStatus;

/**
 * Dining repository interface to make use of the CRUD repository methods. Custom queries
 * required by the application have been added to this interface.
 * @author Chris Daniels
 */
public interface DiningReviewRepository extends CrudRepository<DiningReview, Long> {
	List<DiningReview> findByReviewStatus(ReviewStatus status);

}
