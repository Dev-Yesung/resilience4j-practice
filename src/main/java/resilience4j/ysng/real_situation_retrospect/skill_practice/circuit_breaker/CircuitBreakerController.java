package resilience4j.ysng.real_situation_retrospect.skill_practice.circuit_breaker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CircuitBreakerController {

	private final CircuitBreakerService circuitBreakerService;

	@GetMapping("/api/circuit-breaker/call")
	public String call(
		@RequestParam String param
	) throws InterruptedException {
		return circuitBreakerService.process(param);
	}
}
