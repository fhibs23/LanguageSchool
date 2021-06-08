package com.example.LanguageSchool.Repositories;

import com.example.LanguageSchool.Models.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITypeRepository extends JpaRepository<Type, Integer> {

}