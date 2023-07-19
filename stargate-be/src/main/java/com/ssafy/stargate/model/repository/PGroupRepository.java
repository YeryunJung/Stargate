package com.ssafy.stargate.model.repository;

import com.ssafy.stargate.model.entity.PGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PGroupRepository extends JpaRepository<PGroup,Long> {
}
