package com.celiagodfrin.pixelrevealback.dao;

import com.celiagodfrin.pixelrevealback.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneDao extends JpaRepository<Zone, Long> {
}