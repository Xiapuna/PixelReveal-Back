package com.celiagodfrin.pixelrevealback.dao;

import com.celiagodfrin.pixelrevealback.model.ColoringSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorinSheetDao extends JpaRepository<ColoringSheet, Long> {
}