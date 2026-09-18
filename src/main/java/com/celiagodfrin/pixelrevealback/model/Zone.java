package com.celiagodfrin.pixelrevealback.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String shapeData;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Palette palette;
}
