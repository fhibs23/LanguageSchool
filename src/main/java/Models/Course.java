package Models;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="name", nullable = false)
    private String name;
    @Column(name="price")
    private int price;
    @Column(name="description")
    private String description;
}