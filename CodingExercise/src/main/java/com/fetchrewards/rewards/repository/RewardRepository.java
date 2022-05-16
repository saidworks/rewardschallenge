package com.fetchrewards.rewards.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fetchrewards.rewards.model.Reward;

public interface RewardRepository extends JpaRepository<Reward, Long> {

}
