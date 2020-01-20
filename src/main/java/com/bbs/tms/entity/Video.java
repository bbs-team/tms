package com.bbs.tms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Video Entity
 */
@Entity
@Getter
@Setter
@ToString
public class Video {

  @Id()
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "mediumint(8) unsigned")
  private int idx;

  @Column(length = 50, nullable = false)
  @ColumnDefault("''")
  private String name;

  @Column(length = 10, nullable = false)
  private String kind;

}