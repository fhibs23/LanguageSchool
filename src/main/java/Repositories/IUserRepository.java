package Repositories;

import Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    Long deleteById(int id);
}