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
	public String process() {
		return callAnotherServer();
	}

	public String callMyRetry() throws InterruptedException {
		try {
			int retryCount = 0;
			while (retryCount < 3) {
				try {
					log.info("Retry count: {}", retryCount);
					return callAnotherServer();
				} catch (RetryException e) {
					if (retryCount == 2) {
						throw e;
					}
					Thread.sleep(1000L);
					retryCount++;
				}
			}
		} catch (RetryException | IgnoreException e) {
			return fallback(e);
		}

		return "ok";
	}

	private String callAnotherServer() {
		throw new RetryException("retry exception");
		// throw new IgnoreException("ignore exception");
	}

	private String fallback(Exception e) {
		log.info("fallback! your request is done");
		return "Recovered: " + e.toString();
	}
}
