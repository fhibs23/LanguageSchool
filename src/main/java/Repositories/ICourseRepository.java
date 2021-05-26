package Repositories;


import Models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findAllByTypeId(int typeId);
    Course findById(int id);
}
