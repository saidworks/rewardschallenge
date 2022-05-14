package com.fetchrewards.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fetchrewards.model.Reward;
import com.fetchrewards.model.RewardRepository;

@RestController
public class RewardsController {
	private final RewardRepository repository;

	public RewardsController(RewardRepository repository) {
		this.repository = repository;
	}
	
	@PostMapping("/rewards")
	Reward addReward(@RequestBody Reward newReward) {
		return repository.save(newReward);
	}
	
	
}
