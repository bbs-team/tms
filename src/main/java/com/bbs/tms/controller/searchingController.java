package com.bbs.tms.controller;

import com.bbs.tms.entity.Searching;
import com.bbs.tms.repository.SearchingRepository;
import com.bbs.tms.result.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 토렌트 검색 항목 관리 컨트롤러
 */
@RestController
@RequestMapping("/api/query")
public class searchingController {

  @Autowired
  private SearchingRepository searchingRepo;

  /**
   * get all query list
   * @return quertList
   */
  @GetMapping(value = "")
  public Iterable<Searching> getAllQueryList() {

    return searchingRepo.findAll();

  }

  /**
   * get query by name
   * @param name
   * @return query
   */
  @GetMapping(value = "/{name}")
  public Object getQueryList(@PathVariable String name) {
    if(searchingRepo.existsByName(name)){
      return Result.QUERY_EXISTS.toResponse(HttpStatus.BAD_REQUEST);
    }

    return searchingRepo.findByName(name);

  }

  /**
   * add query
   * @return ResponseEntity 200 or 401
   */
  @PostMapping(value = "/add")
  public Object addQuery(@RequestBody Searching searching) {

    if(searchingRepo.existsByName(searching.getName())){
      return Result.QUERY_EXISTS.toResponse(HttpStatus.BAD_REQUEST);
    }

    return Result.SUCCESS.toString();

  }

  /**
   * delete query
   * @param idx
   * @return ResponseEntity 200 or 401
   */
  @DeleteMapping(value = "/{idx}")
  public Object deleteQuery(@PathVariable int idx){

    if(searchingRepo.existsById(idx)){
      return Result.QUERY_EXISTS.toResponse(HttpStatus.BAD_REQUEST);
    }

    searchingRepo.deleteById(idx);

    return Result.SUCCESS.toString();

  }

}