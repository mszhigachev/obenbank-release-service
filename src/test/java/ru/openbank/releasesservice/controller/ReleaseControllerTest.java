package ru.openbank.releasesservice.controller;

import io.restassured.RestAssured;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class ReleaseControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseDataSourceConnection databaseDataSourceConnection;

    private Map<String, String> cookie;

    @BeforeEach
    public void setUp() throws Exception {
        RestAssured.port = port;
        cookie = RestAssured.given().auth().basic("bob", "bobspassword").get("/").getCookies();
    }

    @AfterEach
    public void setTerDown() throws Exception {
        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate("" +
                "delete from tb_hotfixes;" +
                "delete from tb_releases;" +
                "ALTER SEQUENCE tb_hotfixes_id_seq RESTART WITH 1;" +
                "ALTER SEQUENCE tb_releases_id_seq RESTART WITH 1;");
    }

    @Test
    void getAll() throws Exception {
        LocalDateTime testTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);

        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate(String.format("" +

                "INSERT INTO tb_releases( name, description, date_start, date_end, date_freeze) VALUES ( 'name', 'desc', '%1$s', '%1$s', '%1$s');", testTime));
        RestAssured.given()
                .cookies(cookie)
                .get("/releases").then()
                .assertThat().statusCode(200)
                .assertThat().body("pageNumber", Matchers.equalTo(0))
                .assertThat().body("pageSize", Matchers.equalTo(10))
                .assertThat().body("totalPages", Matchers.equalTo(1))
                .assertThat().body("totalElements", Matchers.equalTo(1))
                .assertThat().body("content", Matchers.hasSize(1))
                .assertThat().body("content[0].id", Matchers.equalTo(1))
                .assertThat().body("content[0].name", Matchers.equalTo("name"))
                .assertThat().body("content[0].description", Matchers.equalTo("desc"))
                .assertThat().body("content[0].dateStart", Matchers.equalTo(testTime.toString()))
                .assertThat().body("content[0].dateEnd", Matchers.equalTo(testTime.toString()))
                .assertThat().body("content[0].dateFreeze", Matchers.equalTo(testTime.toString()));

    }

    @Test
    void getAllWithHotfix() throws Exception {
        LocalDateTime testTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);

        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate(String.format("" +

                "INSERT INTO tb_releases( name, description, date_start, date_end, date_freeze) VALUES ( 'name', 'desc', '%1$s', '%1$s', '%1$s');" +
                "INSERT INTO public.tb_hotfixes(release_id, date_fix, description) VALUES (1, '%s', 'hotfix_test_desc');", testTime));
        RestAssured.given()
                .cookies(cookie)
                .get("/releases?is_include_hotfixes=true").then()
                .assertThat().statusCode(200)
                .assertThat().body("pageNumber", Matchers.equalTo(0))
                .assertThat().body("pageSize", Matchers.equalTo(10))
                .assertThat().body("totalPages", Matchers.equalTo(1))
                .assertThat().body("totalElements", Matchers.equalTo(1))
                .assertThat().body("content", Matchers.hasSize(1))
                .assertThat().body("content[0].id", Matchers.equalTo(1))
                .assertThat().body("content[0].name", Matchers.equalTo("name"))
                .assertThat().body("content[0].description", Matchers.equalTo("desc"))
                .assertThat().body("content[0].dateStart", Matchers.equalTo(testTime.toString()))
                .assertThat().body("content[0].dateEnd", Matchers.equalTo(testTime.toString()))
                .assertThat().body("content[0].dateFreeze", Matchers.equalTo(testTime.toString()))
                .assertThat().body("content[0].hotFixes", Matchers.hasSize(1))
                .assertThat().body("content[0].hotFixes[0].id", Matchers.equalTo(1))
                .assertThat().body("content[0].hotFixes[0].dateFix", Matchers.equalTo(testTime.toString()))
                .assertThat().body("content[0].hotFixes[0].description", Matchers.equalTo("hotfix_test_desc"));
    }

    @Test
    void save() {
        LocalDateTime testTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);
        String json = String.format("{\n" +
                "    \"name\": \"name\",\n" +
                "    \"description\": \"desc\",\n" +
                "    \"dateStart\": \"%1$s\",\n" +
                "    \"dateEnd\": \"%1$s\",\n" +
                "    \"dateFreeze\": \"%1$s\"\n" +
                "}", testTime);
        RestAssured.given().header("Content-Type", "application/json")
                .body(json)
                .cookies(cookie)
                .post("/releases").then()
                .assertThat().statusCode(201)
                .assertThat().body("id", Matchers.equalTo(1))
                .assertThat().body("name", Matchers.equalTo("name"))
                .assertThat().body("description", Matchers.equalTo("desc"))
                .assertThat().body("dateStart", Matchers.equalTo(testTime.toString()))
                .assertThat().body("dateEnd", Matchers.equalTo(testTime.toString()))
                .assertThat().body("dateFreeze", Matchers.equalTo(testTime.toString()));
    }

    @Test
    void saveHotfix() throws Exception {
        LocalDateTime testTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);

        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate(String.format("" +
                "INSERT INTO tb_releases( name, description, date_start, date_end, date_freeze) VALUES ( 'name', 'desc', '%1$s', '%1$s', '%1$s');", testTime));
        String json = String.format("{\n" +
                "    \"description\": \"hotfix desc\",\n" +
                "    \"dateFix\": \"%s\"\n" +
                "}", testTime);
        RestAssured.given().header("Content-Type", "application/json")
                .body(json)
                .cookies(cookie)
                .post("/releases/1/hotfixes").then()
                .assertThat().statusCode(201)
                .assertThat().body("id", Matchers.equalTo(1))
                .assertThat().body("releaseId", Matchers.equalTo(1))
                .assertThat().body("dateFix", Matchers.equalTo(testTime.toString()))
                .assertThat().body("description", Matchers.equalTo("hotfix desc"));
    }
}