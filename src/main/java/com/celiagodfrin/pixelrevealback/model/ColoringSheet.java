package com.celiagodfrin.pixelrevealback.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class ColoringSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    @Column(nullable = false, unique = true)
    private String pdfPath;

    @OneToOne
    @JoinColumn(nullable = false, unique = true)
    private Image image;
}
