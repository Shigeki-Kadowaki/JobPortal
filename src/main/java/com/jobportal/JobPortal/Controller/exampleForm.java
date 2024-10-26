package com.jobportal.JobPortal.Controller;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "test")
public class exampleForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "body")
    private String body;
}
