package resilience4j.ysng.real_situation_retrospect.skill_practice.circuit_breaker;

import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import resilience4j.ysng.real_situation_retrospect.skill_practice.exception.IgnoreException;
import resilience4j.ysng.real_situation_retrospect.skill_practice.exception.RecordException;

@Slf4j
@Service
public class CircuitBreakerService {

	private static final String SIMPLE_CIRCUIT_BREAKER_CONFIG = "simpleCircuitBreakerConfig";

	@CircuitBreaker(name = SIMPLE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "fallback")
	public String process(String param) throws InterruptedException {
		return callAnotherServer(param);
	}

	private String callAnotherServer(String param) throws InterruptedException {
		if ("a".equals(param))
			throw new RecordException("record exception");
		else if ("b".equals(param))
			throw new IgnoreException("ignore exception");
		else if ("c".equals(param)) // 3초 이상 걸리는 경우도 실패로 간주
			Thread.sleep(4000);

		return param;
	}

	// private String fallback(String param, Exception ex) {
	// 	// fallback은 ignoreException이 발생해도 실행된다.
	// 	log.info("fallback! your request is " + param);
	// 	return "Recovered: " + ex.toString();
	// }

	// fallback 은 예외 종류에 따라 다르게 호출할 수 있다.
	private String fallback(String param, RecordException ex) {
		log.info("RecordException fallback! your request is {}", param);

		return "Recovered: " + ex.toString();
	}

	private String fallback(String param, IgnoreException ex) {
		log.info("IgnoreException fallback! your request is {}", param);

		return "Recovered: " + ex.toString();
	}

	private String fallback(String param, CallNotPermittedException ex) {
		log.info("CallNotPermittedException fallback! your request is {}", param);

		return "Recovered: " + ex.toString();
	}
}
