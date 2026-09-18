package com.celiagodfrin.pixelrevealback.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Palette {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer legendNumber;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ColoringSheet coloringSheet;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Color color;
}
