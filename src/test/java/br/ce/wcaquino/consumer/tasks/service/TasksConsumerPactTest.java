package br.ce.wcaquino.consumer.tasks.service;


import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.ce.wcaquino.consumer.tasks.model.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TasksConsumerPactTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TasksConsumerPactTest.class);

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("test_provider", "localhost", 4321, this);

    @Pact(provider = "test_provider", consumer = "test_consumer")
    public RequestResponsePact createPactThenReturnNullWhenNoTaskFound(PactDslWithProvider builder) {
        return builder
                .given("Then Return Null When No Task Found")
                .uponReceiving("ExampleJavaConsumerPactRuleTest test interaction")
                .path("/todo/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body("")
                .toPact();
    }

    @Pact(provider = "test_provider", consumer = "test_consumer")
    public RequestResponsePact createPactThenReturnTask1WhenTaskFound(PactDslWithProvider builder) {
        return builder
                .given("Then Return Task 1 When Task Found")
                .uponReceiving("ExampleJavaConsumerPactRuleTest test interaction")
                .path("/todo/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body("{\"id\":\"1\",\"task\":\"task\",\"dueDate\":\"dueDate\"}")
                .toPact();
    }

    private TasksConsumer tasksConsumer;

    @Before
    public void setUp() {
        tasksConsumer = new TasksConsumer(mockProvider.getUrl());
    }

    @Test
    @PactVerification(value = "test_provider",fragment = "createPactThenReturnNullWhenNoTaskFound")
    public void testThenReturnNullWhenNoTaskFound() throws IOException {
        LOGGER.info("Config: " + mockProvider.getConfig());
        Task actual = tasksConsumer.getTask(1L);
        assertNull(actual);

    }

    @Test
    @PactVerification(value = "test_provider",fragment = "createPactThenReturnTask1WhenTaskFound")
    public void testThenReturnTask1WhenTaskFound() throws IOException {

        Task actual = tasksConsumer.getTask(1L);

        assertNotNull(actual);
        Assert.assertEquals(Long.valueOf(1L), actual.getId());
        Assert.assertEquals("task", actual.getTask());
        Assert.assertEquals("dueDate", actual.getDueDate());

    }

}