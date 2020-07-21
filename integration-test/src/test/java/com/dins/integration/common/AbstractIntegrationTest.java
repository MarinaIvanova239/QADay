package com.dins.integration.common;

import com.dins.entities.Post;
import com.dins.integration.common.config.IntegrationContext;
import com.dins.services.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.util.List;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;

@ContextConfiguration(classes = IntegrationContext.class)
public class AbstractIntegrationTest extends AbstractTestNGSpringContextTests {
    @Autowired
    protected RestClient restClient;

    protected List<Post> postsInSystem;

    @BeforeMethod(alwaysRun = true)
    public void commonTestsSetUp() throws Exception {
        // should be reading from database here
        ObjectMapper mapper = new ObjectMapper();
        postsInSystem = asList(mapper.readValue(new File("src/test/resources/posts.json"), Post[].class));
    }
}
