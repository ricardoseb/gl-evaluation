package dev.riqui.evaluation.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "phones")
@Data
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long number;

    private Integer citycode;

    private String contrycode;
}
