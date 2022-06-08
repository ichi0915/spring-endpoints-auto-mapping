package com.ichi0915.Endpoint.Auto.Mapping.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

@RestController
@RequestMapping("/rest")
public class RestMapping {

	@GetMapping("/hi")
	public ResponseEntity<String> hiGetNull() {
		return ResponseEntity.ok("Get Null hi");
	}

	@GetMapping("/hi/{payload}")
	public ResponseEntity<String> hiGet(@PathVariable final String payload) {
		return ResponseEntity.ok("Get hi " + payload);
	}

	@PostMapping("/hi")
	public ResponseEntity<String> hiPost(@RequestBody @Nullable final String payload) {
		if (null != payload)
			return ResponseEntity.ok("Post hi " + payload);
		else
			return ResponseEntity.ok("Post Null hi");
	}

}
