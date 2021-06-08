package com.example.LanguageSchool.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="basket")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="course_id")
    private int courseId;
    @Column(name="user_id", nullable = false)
    private int userId;
    @Column(name="course_count")
    private int courseCount;

}