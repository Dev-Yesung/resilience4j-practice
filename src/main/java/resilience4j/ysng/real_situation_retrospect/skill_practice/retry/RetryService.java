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

	private static final int MAX_ATTEMPTS = 3;
	private static final long WAIT_DURATION = 1000L;

	public String callMyRetry() throws InterruptedException {
		String message = null;
		int retryCount = 0;
		while (message == null && retryCount++ < MAX_ATTEMPTS) {
			try {
				log.info("Retry count: {}", retryCount);
				message = callAnotherServer();
			} catch (RetryException | IgnoreException e) {
				if (e instanceof IgnoreException || retryCount == MAX_ATTEMPTS) {
					message = fallback(e);
				}
				Thread.sleep(WAIT_DURATION);
			}
		}

		return message;
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
