package com.bbs.tms.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.ColumnTransformer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * User
 */
@Entity
@Getter
@Setter
@ToString
public class User {

  @Id()
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "MEDIUMINT(8) UNSIGNED")
  private int idx;

  @Column(length=50, unique = true, nullable = false, updatable = false)
  private String username;

  @Column(length = 200, nullable = false)
  @ColumnTransformer(write = "PASSWORD(?)")
  private String password;

  @Column(length = 50, nullable = false)
  private String nickname;

  @Column(nullable = false, insertable = false, updatable = false)
  @ColumnDefault(value = "CURRENT_TIMESTAMP")
  private Timestamp createDate;

}