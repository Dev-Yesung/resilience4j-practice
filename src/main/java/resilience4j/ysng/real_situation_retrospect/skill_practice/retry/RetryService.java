package resilience4j.ysng.real_situation_retrospect.skill_practice.retry;

import org.springframework.stereotype.Service;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import resilience4j.ysng.real_situation_retrospect.skill_practice.exception.IgnoreException;
import resilience4j.ysng.real_situation_retrospect.skill_practice.exception.RetryException;

@Slf4j
@Service
public class RetryService {

	@Retry(name = "simpleRetryConfig", fallbackMethod = "fallback")
	public String process(
		final String message
	) {
		return callAnotherServer(message);
	}

	private String callAnotherServer(
		final String param
	) {
		throw new RetryException("retry exception");
		// throw new IgnoreException("ignore exception");
	}

	private String fallback(String param, Exception e) {
		log.info("fallback! your request is {}", param);
		return "Recovered: " + e.toString();
	}
}
