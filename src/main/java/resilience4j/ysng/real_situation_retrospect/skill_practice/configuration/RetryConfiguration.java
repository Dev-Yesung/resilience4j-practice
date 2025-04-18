package resilience4j.ysng.real_situation_retrospect.skill_practice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RetryConfiguration {

	@Bean
	public RegistryEventConsumer<Retry> registryEventConsumer() {
		return new DefaultRegistryEventConsumer();
	}

	private static class DefaultRegistryEventConsumer implements RegistryEventConsumer<Retry> {

		@Override
		public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) {
			log.info("RegistryEventConsumer.onEntryAddedEvent");
			entryAddedEvent.getAddedEntry().getEventPublisher()
				.onEvent(event -> log.info(event.toString()));
		}

		@Override
		public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {
			log.info("RegistryEventConsumer.onEntryRemovedEvent");
		}

		@Override
		public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {
			log.info("RegistryEventConsumer.onEntryReplacedEvent");
		}
	}
}
