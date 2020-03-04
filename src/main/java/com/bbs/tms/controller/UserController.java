package com.bbs.tms.controller;

import java.util.Optional;

import com.bbs.tms.entity.User;
import com.bbs.tms.repository.UserRepository;
import com.bbs.tms.result.Result;
import com.bbs.tms.util.ModelUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController
 */
@RestController
@RequestMapping("/user")
@Component
public class UserController {

  @Autowired
  private UserRepository userRepo;

  /**
   * Get all user list
   * @return userList
   */
  @GetMapping(value = "")
  public Iterable<User> getUserList() {
    return userRepo.findAll();
  }

  /**
   * Get user by idx
   * @param idx
   * @return user
   */
  @GetMapping(value = "/{idx}")
  public Object getUser(@PathVariable int idx) {
    Optional<User> user = userRepo.findById(idx);
    return user.isPresent() ? user.get()
                            : Result.USER_NOT_FOUND.toResponse(HttpStatus.BAD_REQUEST);
  }

  /**
   * Add user
   * @param user
   * @return ResponseEntity 200 or 401
   */
  @PostMapping(value = "")
  public Object addUser(@RequestBody User user) {
    if (userRepo.existsByUsername(user.getUsername())) {
      return Result.USER_EXISTS.toResponse(HttpStatus.BAD_REQUEST);
    }

    userRepo.save(user);
    return Result.SUCCESS.toString();
  }

  /**
   * Update user
   * @param idx
   * @param user
   * @return ResponseEntity 200 or 401
   */
  @PutMapping(value = "/{idx}")
  public Object updateUser(@PathVariable int idx, @RequestBody User user) {
    Optional<User> oriUser = userRepo.findById(idx);
    if (!oriUser.isPresent()) {
      return Result.USER_NOT_FOUND.toResponse(HttpStatus.BAD_REQUEST);
    }
  
    ModelUtils.fillNewModel(user, oriUser.get());
    userRepo.save(user);
    return Result.SUCCESS.toResponse(HttpStatus.OK);
  }

  /**
   * Remove user
   * @param idx
   * @return ResponseEntiry 200 or 401
   */
  @DeleteMapping(value = "/{idx}")
  public Object removeUser(@PathVariable int idx) {
    if (!userRepo.existsById(idx)) {
      return Result.USER_NOT_FOUND.toResponse(HttpStatus.BAD_REQUEST);
    }

    userRepo.deleteById(idx);
    return Result.SUCCESS.toResponse(HttpStatus.OK);
  }

}