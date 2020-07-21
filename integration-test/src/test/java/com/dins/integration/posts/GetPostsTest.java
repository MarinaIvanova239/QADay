package com.dins.integration.posts;

import com.dins.entities.Post;
import com.dins.integration.common.AbstractIntegrationTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonMap;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.reflectionassert.ReflectionComparatorMode.LENIENT_ORDER;

public class GetPostsTest extends AbstractIntegrationTest {

    @Test
    public void getAllPosts() {
        // when
        List<Post> actualPosts = restClient.getAllPosts();
        // then
        assertReflectionEquals(postsInSystem, actualPosts, LENIENT_ORDER);
    }

    @DataProvider(name="postsExistingId")
    public Object[][] postsExistingId() {
        return new Object[][]{
                {1L},
                {32L},
                {100L}
        };
    }

    @Test(dataProvider = "postsExistingId")
    public void getSinglePostById(long postId) {
        // given
        Post expectedPost = postsInSystem.stream().filter(post -> post.getId() == postId).findFirst().get();
        // when
        Post actualPost = restClient.getSinglePost(postId);
        // then
        assertReflectionEquals(expectedPost, actualPost, LENIENT_ORDER);
    }

    @DataProvider(name="userNotEmptyFilters")
    public Object[][] userNotEmptyFilters() {
        Map<String, Object> userIdAndIdMap = new HashMap<>();
        userIdAndIdMap.put("userId", 6L);
        userIdAndIdMap.put("id", 58L);

        Map<String, Object> userIdAndTitleAndBody = new HashMap<>();
        userIdAndTitleAndBody.put("userId", 4L);
        userIdAndTitleAndBody.put("title", "doloremque illum aliquid sunt");
        userIdAndTitleAndBody.put("body", "deserunt eos nobis asperiores et hic\nest debitis repellat molestiae optio\nnihil ratione ut eos beatae quibusdam distinctio maiores\nearum voluptates et aut adipisci ea maiores voluptas maxime");

        Map<String, Object> idAndUserIdAndTitleAndBody = new HashMap<>();
        idAndUserIdAndTitleAndBody.put("userId", 1L);
        idAndUserIdAndTitleAndBody.put("id", 1L);
        idAndUserIdAndTitleAndBody.put("title", "sunt aut facere repellat provident occaecati excepturi optio reprehenderit");
        idAndUserIdAndTitleAndBody.put("body", "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto");

        return new Object[][]{
                {singletonMap("userId", 5L)},
                {singletonMap("id", 23L)},
                {singletonMap("title", "non est facere")},
                {singletonMap("body", "quasi excepturi consequatur iste autem temporibus sed molestiae beatae\net quaerat et esse ut\nvoluptatem occaecati et vel explicabo autem\nasperiores pariatur deserunt optio")},
                {userIdAndIdMap},
                {userIdAndTitleAndBody},
                {idAndUserIdAndTitleAndBody}
        };
    }

    @Test(dataProvider = "userNotEmptyFilters")
    public void getNotEmptyListByFilters(Map<String, Object> fieldsMap) {
        // given
        List<Post> expectedPosts = getExpectedPostsBasedOnFilters(fieldsMap);
        // when
        List<Post> actualPosts = restClient.getWithFilterByField(fieldsMap);
        // then
        assertThat(actualPosts, is(not(emptyList())));
        assertReflectionEquals(expectedPosts, actualPosts, LENIENT_ORDER);
    }

    @DataProvider(name="userEmptyFilters")
    public Object[][] userEmptyFilters() {
        Map<String, Object> userIdAndIdMap = new HashMap<>();
        userIdAndIdMap.put("userId", 6L);
        userIdAndIdMap.put("id", 99L);

        Map<String, Object> userIdAndTitleAndBody = new HashMap<>();
        userIdAndTitleAndBody.put("userId", 4L);
        userIdAndTitleAndBody.put("title", "doloremque illum aliquid sunt");
        userIdAndTitleAndBody.put("body", "some-body");

        Map<String, Object> idAndUserIdAndTitleAndBody = new HashMap<>();
        idAndUserIdAndTitleAndBody.put("userId", 1L);
        idAndUserIdAndTitleAndBody.put("id", 6L);
        idAndUserIdAndTitleAndBody.put("title", "sunt aut facere repellat provident occaecati excepturi optio reprehenderit");
        idAndUserIdAndTitleAndBody.put("body",  "i-do-not-exist");

        return new Object[][]{
                {singletonMap("userId", 11L)},
                {singletonMap("userId", "some-string")},
                {singletonMap("id", "some-id")},
                {singletonMap("title", "non-existent-title")},
                {singletonMap("body", "non-existent-body")},
                {userIdAndIdMap},
                {userIdAndTitleAndBody},
                {idAndUserIdAndTitleAndBody}
        };
    }

    @Test(dataProvider = "userEmptyFilters")
    public void getEmptyListByFilters(Map<String, Object> fieldsMap) {
        // when
        List<Post> actualPosts = restClient.getWithFilterByField(fieldsMap);
        // then
        assertThat(actualPosts, is(emptyList()));
    }

    @Test
    public void getListByNonExistentFilter() {
        // given
        Map<String, Object> fieldsMap = singletonMap("non-existent-field", "value");
        // when
        List<Post> actualPosts = restClient.getWithFilterByField(fieldsMap);
        // then
        assertReflectionEquals(postsInSystem, actualPosts, LENIENT_ORDER);
    }

    private List<Post> getExpectedPostsBasedOnFilters(Map<String, Object> fieldsMap) {
        return  postsInSystem.stream()
                .filter(post -> fieldsMap.get("userId") == null || post.getUserId() == (long) fieldsMap.get("userId"))
                .filter(post -> fieldsMap.get("id") == null || post.getId() == (long) fieldsMap.get("id"))
                .filter(post -> fieldsMap.get("title") == null || post.getTitle().equals(fieldsMap.get("title")))
                .filter(post -> fieldsMap.get("body") == null || post.getBody().equals(fieldsMap.get("body")))
                .collect(Collectors.toList());
    }
}
