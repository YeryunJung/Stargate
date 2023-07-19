package com.ssafy.stargate.model.repository;

import com.ssafy.stargate.model.entity.PMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PMemberRepository extends JpaRepository<PMember,Long> {
}
