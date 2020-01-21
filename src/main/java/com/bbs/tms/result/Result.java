package com.bbs.tms.result;

import com.google.gson.JsonObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;

/**
 * Result
 */
@Getter
public enum Result {

  SUCCESS(0, "success"),
  FAIL(1, "fail"),
  USER_EXISTS(1001, "The username already exists."),
  USER_NOT_FOUND(1002, "The user could not found."),
  QUERY_EXISTS(1003,"The queryname already exists.");

  private int code;
  private String msg;
  
  private Result(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  @Override
  public String toString() {
    JsonObject json = new JsonObject();
    json.addProperty("code", this.code);
    json.addProperty("msg", this.msg);
    return json.toString();
  }

  public ResponseEntity<String> toResponse(HttpStatus status) {
    return new ResponseEntity<String>(this.toString(), status);
  }

}