package com.bbs.tms.repository;

import com.bbs.tms.entity.Searching;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * searching query Repository
 */
@Repository
public interface SearchingRepository extends CrudRepository<Searching, Integer> {}