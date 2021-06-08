package com.example.LanguageSchool.Repositories;

import com.example.LanguageSchool.Models.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface IBasketRepository extends JpaRepository<Basket, Integer> {
    List<Basket> findAllByUserId(int userId);
    Basket findById(int id);
    Basket findByUserIdAndCourseId(int userId, int courseId);
    Long deleteById(int id);
    @Transactional
    Long deleteAllByUserId(int userId);

}
