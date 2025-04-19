package resilience4j.ysng.real_situation_retrospect.skill_practice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import lombok.extern.slf4j.Slf4j;

@Configuration
public class CircuitBreakerConfiguration {

	@Bean
	public RegistryEventConsumer<CircuitBreaker> myRegistryEventConsumer() {
		return new RegistryEventConsumerImpl();
	}

	/**
	 * CircuitBreaker 의 상태 변화를 로깅하거나 외부 시스템에 알림 등을 줄 수 있도록 hook 을 걸어주는 것
	 *
	 * @author Yesung Go
	 * @implNote CircuitBreaker 등록/제거/교체 이벤트를 처리하는 부분이다.<br>
	 * onEntryAddedEvent: CircuitBreaker 가 Registry 에 추가될 때 호출되고 추가된 CircuitBreaker 에 이벤트 리스너를 등록하고 있음<br>
	 * onEntryRemovedEvent: CircuitBreaker 가 registry 에서 제거될 때 호출됨
	 * onEntryReplacedEvent: CircuitBreaker 가 다른 인스턴스로 교체될 때 호출됨
	 */
	@Slf4j
	private static class RegistryEventConsumerImpl implements RegistryEventConsumer<CircuitBreaker> {

		/* 활용방안
		 * 1) 이벤트에 따라 로그를 발생시켜 로그 모니터링을 할 수 있도록 만들 수 있음
		 * 2) onStateTransition 을 활용해 다른 분산서버에 장애전파를 할 수 있음 -> 분산 서버에 상황을 공유해 서킷을 Open 해서 요청을 막을 수 있게 함
		 */
		@Override
		public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
			log.info("RegistryEventConsumer.onEntryAddedEvent");
			final CircuitBreaker.EventPublisher eventPublisher = entryAddedEvent.getAddedEntry().getEventPublisher();
			/* onEvent : CircuitBreaker 의 모든 이벤트에 대한 리스너.
			   예: 성공, 실패, 리트라이 등.*/
			eventPublisher.onEvent(event -> log.info(event.toString()));
			// onSuccess : 성공한 요청에 관한 이벤트 리스너
			eventPublisher.onSuccess(event -> log.info("onSuccess {}", event));
			// onCallNotPermitted : 서킷 브레이커의 상태가 Open 이 되어서 차단된 요청이 발생했을 때 이벤트 리스너
			eventPublisher.onCallNotPermitted(event -> log.info("onCallNotPermitted {}", event));
			// onError : 에러가 발생했을 때 이벤트 리스너
			eventPublisher.onError(event -> log.error("onError {}", event));
			// onIgnoredError : 무시되었을 때 발생하는 이벤트 리스너
			eventPublisher.onIgnoredError(event -> log.error("onIgnoredError {}", event));
			// onStateTransition : 서킷 브레이커의 상태가 변경되었을 때의 이벤트 리스너
			eventPublisher.onStateTransition(event -> log.info("onStateTransition {}", event));
			// onSlowCallRateExceeded : 슬로우 콜이 임계치에 달했을 때의 이벤트 리스너
			eventPublisher.onSlowCallRateExceeded(event -> log.info("onSlowCallRateExceeded {}", event));
			/* onFailureRateExceeded : 실패 비율이 threshold 를 넘었을 때 발생하는 이벤트.
			   즉, CircuitBreaker 가 열리는(open) 상황. */
			eventPublisher.onFailureRateExceeded(event -> log.info("{}", event.getEventType()));
		}

		@Override
		public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
			log.info("RegistryEventConsumer.onEntryRemovedEvent");
		}

		@Override
		public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
			log.info("RegistryEventConsumer.onEntryReplacedEvent");
		}
	}
}
