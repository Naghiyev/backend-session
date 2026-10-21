package az.training.taskmanagement;

import az.training.taskmanagement.dto.CategoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryApiTest {
    @Autowired TestRestTemplate rest;

    private ResponseEntity<String> send(String method, String path, String body) throws Exception {
        var builder = HttpRequest.newBuilder(URI.create(rest.getRootUri() + path))
                .header("Content-Type", "application/json");
        builder.method(method, body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body));
        try (var client = HttpClient.newHttpClient()) {
            var response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            return ResponseEntity.status(response.statusCode()).body(response.body());
        }
    }

    @Test void categoryLifecycle() throws Exception {
        var created = rest.postForEntity("/categories", new az.training.taskmanagement.dto.CreateCategoryRequest("Təhsil"), CategoryResponse.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var id = created.getBody().id();
        assertThat(id).isPositive();
        assertThat(created.getBody().name()).isEqualTo("Təhsil");
        assertThat(rest.getForObject("/categories/" + id, CategoryResponse.class)).isEqualTo(created.getBody());
        assertThat(rest.getForObject("/categories", CategoryResponse[].class)).contains(created.getBody());
        var updated = send("PATCH", "/categories/" + id, "{\"name\":\"İş\"}");
        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(rest.getForObject("/categories/" + id, CategoryResponse.class).name()).isEqualTo("İş");
        var deleted = send("DELETE", "/categories/" + id, null);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(deleted.getBody()).isEmpty();
        assertThat(send("GET", "/categories/" + id, null).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(send("DELETE", "/categories/" + id, null).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(strings = {"{}", "{\"name\":null}", "{\"name\":\"\"}", "{\"name\":\"   \"}", "{", "null"})
    void invalidBodies(String body) throws Exception {
        var post = send("POST", "/categories", body);
        assertThat(post.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(post.getBody()).contains("\"status\":400", "\"path\":\"/categories\"", "\"timestamp\"");
        var created = rest.postForEntity("/categories", new az.training.taskmanagement.dto.CreateCategoryRequest("Valid"), CategoryResponse.class).getBody();
        try {
            assertThat(send("PATCH", "/categories/" + created.id(), body).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(rest.getForObject("/categories/" + created.id(), CategoryResponse.class).name()).isEqualTo("Valid");
        } finally { rest.delete("/categories/" + created.id()); }
    }

    @Test void missingAndMalformedIds() throws Exception {
        for (String method : new String[]{"GET", "PATCH", "DELETE"}) {
            var response = send(method, "/categories/999999", method.equals("PATCH") ? "{\"name\":\"Other\"}" : null);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).contains("Category tapılmadı: id=999999", "\"status\":404", "\"error\":\"Not Found\"");
            assertThat(send(method, "/categories/not-a-number", method.equals("PATCH") ? "{\"name\":\"Other\"}" : null).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test void emptyRepository() {
        var service = new az.training.taskmanagement.service.impl.CategoryServiceImpl(
                new az.training.taskmanagement.repository.inmemory.InMemoryCategoryRepository());
        assertThat(service.getAllCategories()).isEmpty();
    }
}
