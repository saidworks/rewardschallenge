package com.fetchrewards.rewards.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fetchrewards.rewards.model.Reward;
import com.fetchrewards.rewards.repository.RewardRepository;

@RestController
public class RewardController {
	private final RewardRepository repository;

	public RewardController(RewardRepository repository) {
		this.repository = repository;
	}
	
	@PostMapping("/rewards")
	Reward addReward(@RequestBody Reward newReward) {
		return repository.save(newReward);
	}
	
	@GetMapping("/rewards")
	List<Reward> all(){
		return repository.findAll();
	}
	
	
	
}
