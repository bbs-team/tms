package com.bbs.tms.repository;

import com.bbs.tms.entity.Video;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * VideoRepository
 */
@Repository
public interface VideoRepository extends CrudRepository<Video, Integer> {

  public boolean existsByName(String name);
  public Object findByKind(String kind);

}