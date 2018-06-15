package com.equitybot.trade.ws.controller.property;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equitybot.trade.db.mongodb.property.domain.KiteProperty;
import com.equitybot.trade.db.mongodb.property.repository.PropertyRepository;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

@RestController
@RequestMapping("/api")
public class PropertyController {
	@Autowired
	PropertyRepository propertyRepository;
	@GetMapping("/property/v1.0/{userId}")
	public List<KiteProperty> findByUserId(@PathVariable("userId") String userId) {
		return propertyRepository.findByUserId(userId);
	}

	@PostMapping({ "/property/v1.0" })
	public KiteProperty add(@RequestBody KiteProperty property) {
		return propertyRepository.save(property);
	}

	@PutMapping("/property/v1.0/{userId}/{requestToken}/{access_token}/{public_token}/{refresh_token}")
	public UpdateResult update(@PathVariable("userId") String userId,
			@PathVariable("requestToken") String requestToken,
			@PathVariable("access_token") String access_token,
			@PathVariable("public_token") String public_token) {
		return propertyRepository.updatePropertyByUserId(userId, requestToken, access_token, public_token);
	}

	@DeleteMapping({ "/property/v1.0/{userId}" })
	public DeleteResult delete(@PathVariable("userId") String userId) {
		return propertyRepository.deleteByUserId(userId);
	}

	@GetMapping("/property/v1.0")
	public List<KiteProperty> findAll() {

		return propertyRepository.findAll();
	}
}
