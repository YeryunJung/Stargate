package com.ssafy.stargate.model.repository;

import com.ssafy.stargate.model.entity.FUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FUserRepository extends JpaRepository<FUser, String> {
}
