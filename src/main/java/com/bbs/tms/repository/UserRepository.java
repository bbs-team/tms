package com.bbs.tms.repository;

import com.bbs.tms.entity.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

  public boolean existsByUsername(String username);

}