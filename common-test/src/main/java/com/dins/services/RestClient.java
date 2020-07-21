package com.dins.services;

import com.dins.entities.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RestClient {
    private String HOST = "jsonplaceholder.typicode.com";
    private RestAssuredConfig restAssuredConfig;

    public RestClient() {
        ObjectMapper mapper = new ObjectMapper();
        restAssuredConfig = RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (aClass, s) -> mapper
        ));
    }

    public List<Post> getAllPosts() {
        Response response = requestSpecification()
                .get("http://" + HOST + "/posts")
                .andReturn();
        assertThat(response.getStatusCode(), is(200));
        return asList(response.getBody().as(Post[].class));
    }

    public Post getSinglePost(long id) {
        Response response = requestSpecification()
                .get("http://" + HOST + "/posts/" + id)
                .andReturn();
        assertThat(response.getStatusCode(), is(200));
        return response.getBody().as(Post.class);
    }

    public Response getSinglePostWithNoStatusCheck(Object id) {
        return requestSpecification()
                .get("http://" + HOST + "/posts/" + id)
                .andReturn();
    }

    private RequestSpecification requestSpecification() {
        return given()
                .config(restAssuredConfig)
                .contentType("application/json");
    }

    public List<Post> getWithFilterByField(Map<String, Object> fields) {
        String filters = "?";
        for (Map.Entry<String, Object> field : fields.entrySet()) {
            filters += field.getKey() + "=" + field.getValue() + "&";
        }

        Response response = requestSpecification()
                .get("http://" + HOST + "/posts" + filters)
                .andReturn();
        assertThat(response.getStatusCode(), is(200));
        return asList(response.getBody().as(Post[].class));
    }
}
