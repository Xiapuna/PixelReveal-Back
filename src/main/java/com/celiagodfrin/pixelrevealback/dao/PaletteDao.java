package com.celiagodfrin.pixelrevealback.dao;

import com.celiagodfrin.pixelrevealback.model.Palette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaletteDao extends JpaRepository<Palette, Long> {
}