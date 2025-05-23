package resilience4j.ysng.real_situation_retrospect.skill_practice.retry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class RetryController {

	private final RetryService retryService;

	@GetMapping("/api/retry-demo/call")
	public String callRetryDemo() {
		return retryService.process();
	}

	@GetMapping("/api/retry-demo/my-call")
	public String callMyRetry() throws InterruptedException {
		return retryService.callMyRetry();
	}
}
