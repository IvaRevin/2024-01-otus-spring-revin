package ru.otus.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.models.Caterpillar;
import ru.otus.hw.models.Chrysalis;

import java.util.List;

@MessagingGateway
public interface TransformationGateway {
    @Gateway(requestChannel  = "caterpillarChannel", replyChannel = "chrysalisChannel")
    List<Chrysalis> process(List<Caterpillar> caterpillars);
}
