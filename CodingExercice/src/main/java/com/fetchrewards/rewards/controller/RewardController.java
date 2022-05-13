package com.fetchrewards.rewards.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fetchrewards.rewards.model.Payer;
import com.fetchrewards.rewards.model.Reward;
import com.fetchrewards.rewards.repository.RewardRepository;

@RestController
public class RewardController {
	private final RewardRepository repository;
	
	
	
	public RewardController(RewardRepository repository) {
		this.repository = repository;
	}
	
	//helper function 
	Map<String,Long> sumUp(List<Payer> rewards){
		Iterator<Payer> elements = rewards.iterator();
		Map<String,Long> map = new HashMap<String,Long> ();
		while(elements.hasNext()) {
			Payer current = elements.next();
			String key = (String) current.getPayer();
			if(key.length()>0) {
				if(!map.containsKey(key)) {
					map.put(key,current.getPoints());
				}
				else {
					long value = map.get(key);
					value += current.getPoints();
					map.put(key,value);
					System.out.println(key+value);
					}
				
			}
		}
		return map;
	}
	

	
	
	@PostMapping("/rewards")
	Reward addReward(@RequestBody Reward newReward) {
		return repository.save(newReward);
	}
	
	@PostMapping("/spend")
	Map<String,Long>  spend(@RequestBody Map<String,Long> points) {
		/* post an amount of point 
		 * check dates for transactions
		 * spend with FIFO in timestamps in mind
		 *  if amount of points for a payer is smaller then the spend then get just a small fraction from it
		 *  if amount of points for a payer is bigger then the spend points then get the biggest portion from that respective payer
		 * */
		long spend = points.get("points");
		List<Reward> rewards = this.all();
		List<Payer> payers = new ArrayList<>();
		
		//order the list of rewards by date ASC
		Collections.sort(rewards, new Comparator<Reward>() {
			  public int compare(Reward o1, Reward o2) {
			      if (o1.getTimestamp() == null || o2.getTimestamp() == null)
			        return 0;
			      return o1.getTimestamp().compareTo(o2.getTimestamp());
			  }
			});
		
		Iterator<Reward> elements = rewards.iterator();
		while(elements.hasNext()) {
			Reward current = elements.next();
//			System.out.println(current);
			if(current.getPoints()<=spend && spend>0) {
				Payer payer = new Payer();
				spend -= current.getPoints();
				payer.setPayer(current.getPayer());
				payer.setPoints(-current.getPoints());
				payers.add(payer);
				current.setPoints(0);
				System.out.println(">:" +current);
				System.out.println(spend);
				repository.save(current);
				
			}
			else if(current.getPoints()>spend && spend>0) {
				Payer payer = new Payer();
				current.setPoints(current.getPoints()-spend);
				System.out.println(">" + current);
				repository.save(current);
				System.out.println(spend);
				payer.setPayer(current.getPayer());
				payer.setPoints(-spend);
				payers.add(payer);
				break;
			}
		}
		System.out.println(points.get("points"));
		
		Map<String,Long> payersMap = sumUp(payers);
		return payersMap;
	}
	
	
	@GetMapping("/transactions")
	List<Reward> all(){
		List<Reward> rewards = repository.findAll();
		return rewards;
	}
	
	@GetMapping("/balance")
	Map<String,Long> balance(){
		List<Reward> rewards = repository.findAll();
		Iterator<Reward> elements = rewards.iterator();
		Map<String,Long> rewardsMap = new HashMap<String,Long> ();
		while(elements.hasNext()) {
			Reward current = elements.next();
			String key = current.getPayer();
			if(key.length()>0) {
				if(!rewardsMap.containsKey(key)) {	
					rewardsMap.put(key,current.getPoints());
				}
				else {
					long value = rewardsMap.get(key);
					value += current.getPoints();
					System.out.println(key+value);
					rewardsMap.put(key,value);
					}
			}
		}
		return rewardsMap;
	}
	
}
