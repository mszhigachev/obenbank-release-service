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
class InstructionControllerTest {

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
                "delete from tb_instructions;" +
                "delete from tb_hotfixes;" +
                "delete from tb_releases;" +
                "delete from tb_versions;" +
                "delete from tb_services;" +
                "ALTER SEQUENCE tb_services_id_seq RESTART WITH 1;" +
                "ALTER SEQUENCE tb_versions_id_seq RESTART WITH 1;" +
                "ALTER SEQUENCE tb_hotfixes_id_seq RESTART WITH 1;" +
                "ALTER SEQUENCE tb_releases_id_seq RESTART WITH 1;" +
                "ALTER SEQUENCE tb_instructions_id_seq RESTART WITH 1;");
    }

    @Test
    void getReleaseInstruction() throws Exception {
        LocalDateTime testTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);

        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate(String.format("" +
                "insert into tb_services(name,description) values('service-1','service desc');" +
                "insert into tb_versions(service_id, version, created_date) VALUES ( 1, 'v1', '%1$s');" +
                "insert into tb_releases(name, description, date_start, date_end, date_freeze) VALUES ('release-name', 'desc', '%1$s', '%1$s', '%1$s');" +
                "insert into tb_instructions(release_id, service_id, version_id, description, is_hotfix) VALUES (1, 1, 1, 'instruction desc', false);", testTime));

        RestAssured.given()
                .cookies(cookie)
                .get("/instructions/releases/1").then()
                .assertThat().statusCode(200)
                .assertThat().body("releaseId", Matchers.equalTo(1))
                .assertThat().body("services", Matchers.hasSize(1))
                .assertThat().body("dateStart", Matchers.equalTo(testTime.toString()))
                .assertThat().body("dateEnd", Matchers.equalTo(testTime.toString()))
                .assertThat().body("dateFreeze", Matchers.equalTo(testTime.toString()))
                .assertThat().body("dateStart", Matchers.equalTo(testTime.toString()))
                .assertThat().body("services[0].name", Matchers.equalTo("service-1"))
                .assertThat().body("services[0].version", Matchers.equalTo("v1"))
                .assertThat().body("services[0].description", Matchers.equalTo("instruction desc"))
                .assertThat().body("hotfix", Matchers.is(false));

    }

    @Test
    void getHotfixInstruction() throws Exception {
        LocalDateTime testTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);

        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate(String.format("" +
                "insert into tb_services(name,description) values('service-1','service desc');" +
                "insert into tb_versions(service_id, version, created_date) VALUES ( 1, 'v1', '%1$s');" +
                "insert into tb_releases(name, description, date_start, date_end, date_freeze) VALUES ('release-name', 'desc', '%1$s', '%1$s', '%1$s');" +
                "insert into tb_hotfixes(release_id, date_fix, description) VALUES (1, '%1$s', 'desc');" +
                "insert into tb_instructions(release_id, service_id, version_id, description, is_hotfix) VALUES (1, 1, 1, 'instruction desc', true);", testTime));

        RestAssured.given()
                .cookies(cookie)
                .get("/instructions/hotfixes/1").then()
                .assertThat().statusCode(200)
                .assertThat().body("releaseId", Matchers.equalTo(1))
                .assertThat().body("description", Matchers.equalTo("desc"))
                .assertThat().body("services", Matchers.hasSize(1))
                .assertThat().body("dateHotfix", Matchers.equalTo(testTime.toString()))
                .assertThat().body("services[0].name", Matchers.equalTo("service-1"))
                .assertThat().body("services[0].version", Matchers.equalTo("v1"))
                .assertThat().body("services[0].description", Matchers.equalTo("instruction desc"))
                .assertThat().body("hotfix", Matchers.is(true));
    }

    @Test
    void saveHotfixInstruction() throws Exception {
        LocalDateTime testTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);

        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate(String.format("" +
                "insert into tb_services(name,description) values('service-1','service desc');" +
                "insert into tb_versions(service_id, version, created_date) VALUES ( 1, 'v1', '%1$s');" +
                "insert into tb_releases(name, description, date_start, date_end, date_freeze) VALUES ('release-name', 'desc', '%1$s', '%1$s', '%1$s');" +
                "insert into tb_hotfixes(release_id, date_fix, description) VALUES (1, '%1$s', 'desc');", testTime));

        String json = "{\n" +
                "    \"releaseId\": 1,\n" +
                "    \"hotfix\": true,\n" +
                "    \"services\": [\n" +
                "        {\n" +
                "            \"serviceId\": 1,\n" +
                "            \"versionId\": 1,\n" +
                "            \"description\": \"test\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        RestAssured.given().header("Content-Type", "application/json")
                .body(json)
                .cookies(cookie)
                .post("/instructions")
                .then()
                .assertThat().statusCode(201)
                .assertThat().body("releaseId", Matchers.equalTo(1))
                .assertThat().body("hotfix", Matchers.is(true))
                .assertThat().body("services", Matchers.hasSize(1))
                .assertThat().body("services[0].instructionId", Matchers.equalTo(1))
                .assertThat().body("services[0].serviceId", Matchers.equalTo(1))
                .assertThat().body("services[0].versionId", Matchers.equalTo(1))
                .assertThat().body("services[0].description", Matchers.equalTo("test"));
    }

    @Test
    void saveReleaseInstruction() throws Exception {
        LocalDateTime testTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);

        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate(String.format("" +
                "insert into tb_services(name,description) values('service-1','service desc');" +
                "insert into tb_versions(service_id, version, created_date) VALUES ( 1, 'v1', '%1$s');" +
                "insert into tb_releases(name, description, date_start, date_end, date_freeze) VALUES ('release-name', 'desc', '%1$s', '%1$s', '%1$s');", testTime));

        String json = "{\n" +
                "    \"releaseId\": 1,\n" +
                "    \"hotfix\": false,\n" +
                "    \"services\": [\n" +
                "        {\n" +
                "            \"serviceId\": 1,\n" +
                "            \"versionId\": 1,\n" +
                "            \"description\": \"test\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        RestAssured.given().header("Content-Type", "application/json")
                .body(json)
                .cookies(cookie)
                .post("/instructions")
                .then()
                .assertThat().statusCode(201)
                .assertThat().body("releaseId", Matchers.equalTo(1))
                .assertThat().body("hotfix", Matchers.is(false))
                .assertThat().body("services", Matchers.hasSize(1))
                .assertThat().body("services[0].instructionId", Matchers.equalTo(1))
                .assertThat().body("services[0].serviceId", Matchers.equalTo(1))
                .assertThat().body("services[0].versionId", Matchers.equalTo(1))
                .assertThat().body("services[0].description", Matchers.equalTo("test"));
    }
}