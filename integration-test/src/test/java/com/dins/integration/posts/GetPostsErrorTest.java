package com.dins.integration.posts;

import com.dins.integration.common.AbstractIntegrationTest;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class GetPostsErrorTest extends AbstractIntegrationTest {

    @DataProvider(name="postsNonExistingId")
    public Object[][] postsNonExistingId() {
        return new Object[][]{
                {-1L},
                {0L},
                {101L},
                {"some-string"}
        };
    }

    @Test(dataProvider = "postsNonExistingId")
    public void getNonExistentPostById(Object postId) {
        // given
        // when
        Response actualResponse = restClient.getSinglePostWithNoStatusCheck(postId);
        // then
        assertThat(actualResponse.getStatusCode(), is(404));
    }
}
