package com.example.LanguageSchool.Services;

import com.example.LanguageSchool.Models.Course;
import com.example.LanguageSchool.Repositories.ICourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private ICourseRepository icourseRepository;
    @Autowired
    public CourseService(ICourseRepository icourseRepository){
        this.icourseRepository=icourseRepository;
    }
    public List<Course> getAllCoursesByTypeId(int typeId){
        if (typeId != 0)
            return icourseRepository.findAllByTypeId(typeId);
        else
            return getAllCourses();
    }
    public List<Course> getAllCourses(){return  icourseRepository.findAll();}
    public Course getCourseById(int id){
        return icourseRepository.findById(id);
    }
}