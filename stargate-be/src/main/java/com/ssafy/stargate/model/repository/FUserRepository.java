package com.ssafy.stargate.model.repository;

import com.ssafy.stargate.model.entity.FUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 팬 유저 레포지토리
 */
public interface FUserRepository extends JpaRepository<FUser, String> {
}
