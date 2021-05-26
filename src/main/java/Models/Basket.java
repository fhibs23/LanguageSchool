package Models;

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
    private int Id;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "course_id")
    private int productId;
    @Column(name = "course_count")
    private int productCount;
}