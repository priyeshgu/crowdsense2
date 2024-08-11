package priyesh.Crowdsense2.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private String name;

    @Column(nullable = false)
    private String pictureUrl;
    private LocalDateTime firstAppeared;
    private  LocalDateTime lastAppeared;
}
