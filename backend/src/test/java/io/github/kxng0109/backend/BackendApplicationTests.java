package io.github.kxng0109.backend;

import io.github.kxng0109.backend.controller.AiController;
import io.github.kxng0109.backend.service.AiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }

    @Test
    void controllerBeansArePresent() {
        assertThat(applicationContext.getBean(AiController.class)).isNotNull();
    }

    @Test
    void serviceBeansArePresent() {
        assertThat(applicationContext.getBean(AiService.class)).isNotNull();
    }
}