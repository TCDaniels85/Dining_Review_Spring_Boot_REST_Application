package com.chris.MyDiningReviewProject.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.chris.MyDiningReviewProject.model.User;


/**
 * User repository interface to make use of the CRUD repository methods. Custom queries
 * required by the application have been added to this interface.
 * @author Chris Daniels
 */
public interface UserRepository extends CrudRepository<User,Long>{
	Optional<User> findByDisplayName(String displayName);

}
