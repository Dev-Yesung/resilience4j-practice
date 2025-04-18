package resilience4j.ysng.real_situation_retrospect.skill_practice.exception;

public class RetryException extends RuntimeException {

	public RetryException(final String message) {
		super(message);
	}
}
