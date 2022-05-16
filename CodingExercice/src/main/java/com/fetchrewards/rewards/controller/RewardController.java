package com.fetchrewards.rewards.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fetchrewards.rewards.model.Payer;
import com.fetchrewards.rewards.model.Reward;
import com.fetchrewards.rewards.repository.RewardRepository;

@RestController
public class RewardController {
	private final RewardRepository repository;
	
	
	//jpa repository dependency injection
	public RewardController(RewardRepository repository) {
		this.repository = repository;
	}
	
	//helper function to sum points and put it in array 
	List<Payer> sumUp(List<Payer> rewards){
		Iterator<Payer> elements = rewards.iterator();
		Map<String,Payer> map = new HashMap<> ();
		while(elements.hasNext()) {
			Payer current = elements.next();
			String key = (String) current.getPayer();
			if(key.length()>0) {
				if(!map.containsKey(key)) {
					map.put(key,current);
				}
				else {
					long value = map.get(key).getPoints();
					value += current.getPoints();
					Payer payer = new Payer();
					payer.setPayer(current.getPayer());
					payer.setPoints(value);
					map.remove(key);
					map.put(key,payer);
					}
				
			}
		}
		List<Payer> payers = new ArrayList<>();
		for(Payer p:map.values()) {
			payers.add(p);
		}
		return payers;
	}
	

	
	//add reward endpoint
	@PostMapping("/add")
	Reward addReward(@RequestBody Reward newReward) {
		return repository.save(newReward);
	}
	
	//spend points from payers endpoint 
	@PostMapping("/spend")
	List<Payer>  spend(@RequestBody Map<String,Long> points) {
		/* post an amount of point to spend 
		 * check dates of each transactions in db
		 * spend with FIFO in timestamps in mind
		 *  if amount of points for a payer in a transaction is smaller then the spend value then get it all and set the points for that transaction to zero
		 *  if amount of points for a payer is bigger then the spend points decrease it by the amount of the spend value from that respective payer transaction and break from loop
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
			if(current.getPoints()<=spend && spend>0) {
				Payer payer = new Payer();
				spend -= current.getPoints();
				payer.setPayer(current.getPayer());
				payer.setPoints(-current.getPoints());
				payers.add(payer);
				current.setPoints(0);
				repository.save(current);
				
			}
			else if(current.getPoints()>spend && spend>0) {
				Payer payer = new Payer();
				current.setPoints(current.getPoints()-spend);
				repository.save(current);
				payer.setPayer(current.getPayer());
				payer.setPoints(-spend);
				payers.add(payer);
				break;
			}
		}
		
		//create new list from payers that contains payer name with their total points 
		List<Payer> balance = sumUp(payers);
		//order it by points DSC
		List<Payer> payerSorted =  balance.stream()
						.sorted(Comparator.comparingLong((Payer payer) -> -payer.getPoints())).collect(Collectors.toList());
		return payerSorted;
	}
	
	
	@GetMapping("/transactions")
	List<Reward> all(){
		List<Reward> rewards = repository.findAll();
		return rewards;
	}
	
	@GetMapping("/balance")
	Map<String,Long> balance(){
		List<Reward> rewards = repository.findAll();
		
		//order the list of rewards by date ASC
		Collections.sort(rewards, new Comparator<Reward>() {
			  public int compare(Reward o1, Reward o2) {
			      if (o1.getTimestamp() == null || o2.getTimestamp() == null)
			        return 0;
			      return o1.getTimestamp().compareTo(o2.getTimestamp());
			  }
			});
		
		// put the payers in a map that track order of insertion
		Iterator<Reward> elements = rewards.iterator();
		Map<String,Long> rewardsMap = new LinkedHashMap<String,Long> ();
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
					rewardsMap.put(key,value);
					}
			}
		}
		return rewardsMap;
	}
	
}
