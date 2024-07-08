package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.hw.models.Butterfly;
import ru.otus.hw.models.ButterflyColour;
import ru.otus.hw.models.Caterpillar;
import ru.otus.hw.models.Chrysalis;
import ru.otus.hw.models.ChrysalisColour;

import java.util.Random;
import java.util.random.RandomGenerator;

@Configuration
@EnableIntegration
public class TransformationIntegrationConfig {

    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    @Bean
    public MessageChannelSpec<?, ?> caterpillarChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> chrysalisChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow insectFlow() {
        return IntegrationFlow.from(caterpillarChannel())
            .split()
            .<Caterpillar, Chrysalis>transform(caterpillar -> new Chrysalis(
                caterpillar.getWeight() * new Random().nextInt(),
                caterpillar.getLength(),
                caterpillar.getDiameter(),
                ChrysalisColour.values()[new Random().nextInt(ChrysalisColour.values().length)])
            )
            .<Chrysalis, Butterfly>transform(chrysalis -> new Butterfly(
                randomGenerator.nextInt(1, 10),
                randomGenerator.nextInt(10, 100),
                ButterflyColour.values()[new Random().nextInt(ButterflyColour.values().length)])
            )
            .<Butterfly>log(LoggingHandler.Level.INFO, "butterfly", Message::getPayload)
            .aggregate()
            .channel(chrysalisChannel())
            .get();
    }
}

