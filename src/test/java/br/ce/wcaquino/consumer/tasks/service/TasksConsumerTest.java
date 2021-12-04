package br.ce.wcaquino.consumer.tasks.service;


import br.ce.wcaquino.consumer.tasks.model.Task;
import br.ce.wcaquino.consumer.utils.RequestHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class TasksConsumerTest {

    @InjectMocks
    private TasksConsumer tasksConsumer = new TasksConsumer("aaaaaa");

    @Mock
    private RequestHelper helper;

    @Test
    public void testThenReturnNullWhenNoTaskFound() throws IOException {

        Task actual = tasksConsumer.getTask(1L);
        assertNull(actual);

    }

    @Test
    public void testThenReturnTask1WhenTaskFound() throws IOException {

        Map<String, String> resultMock = new HashMap<>();
        resultMock.put("id", "1");
        resultMock.put("task", "task");
        resultMock.put("dueDate", "dueDate");

        Mockito.when(helper.get(Mockito.anyString())).thenReturn(resultMock);

        Task actual = tasksConsumer.getTask(1L);

        assertNotNull(actual);
        Assert.assertEquals(Long.valueOf(1L), actual.getId());
        Assert.assertEquals("task", actual.getTask());
        Assert.assertEquals("dueDate", actual.getDueDate());

    }

}