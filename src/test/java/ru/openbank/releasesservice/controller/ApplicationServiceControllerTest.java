package ru.openbank.releasesservice.controller;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
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
class ApplicationServiceControllerTest {
    private Map<String,String> cookie;
    @LocalServerPort
    int port;

    @Autowired
    DatabaseDataSourceConnection databaseDataSourceConnection;

    @BeforeEach
    public void setUp() throws Exception {
        RestAssured.port = port;
        cookie= RestAssured.given().auth().basic("bob", "bobspassword").get("/").getCookies();
    }

    @AfterEach
    public void setTerDown() throws Exception {
        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate("delete from tb_versions;" +
                "delete from tb_services;" +
                "ALTER SEQUENCE tb_services_id_seq RESTART WITH 1;" +
                "ALTER SEQUENCE tb_versions_id_seq RESTART WITH 1;");
    }


    @Test
    void save() throws Exception {
        String json = "{\n" +
                "    \"name\": \"service-1\",\n" +
                "    \"description\": \"service-1 desc\"\n" +
                "}";

        RestAssured.given().body(json)
                .header("Content-Type", "application/json")
                .cookies(cookie)
                .post("/services").then()
                .assertThat().statusCode(200)
                .assertThat().body("id", Matchers.equalTo(1))
                .assertThat().body("name", Matchers.equalTo("service-1"))
                .assertThat().body("description", Matchers.equalTo("service-1 desc"));
        Connection conn = databaseDataSourceConnection.getConnection();
    }


    @Test
    void getAll() throws Exception {
        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate("insert into tb_services (name,description) values('service-1','service-1 desc')");

        RestAssured.given()
                .cookies(cookie)
                .get("/services").then()
                .assertThat().statusCode(200)
                .assertThat().body("$", Matchers.hasSize(1))
                .assertThat().body("[0].id", Matchers.equalTo(1))
                .assertThat().body("[0].name", Matchers.equalTo("service-1"))
                .assertThat().body("[0].description", Matchers.equalTo("service-1 desc"));
    }


    @Test
    void update() throws Exception {
        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate("insert into tb_services (name,description) values('service-1','service-1 desc')");

        String json = "{\n" +
                " \"description\": \"My New Service\"\n" +
                "}\n";

        RestAssured.given().body(json)
                .header("Content-Type", "application/json")
                .cookies(cookie)
                .put("/services/1").then()
                .assertThat().statusCode(200)
                .assertThat().body("id", Matchers.equalTo(1))
                .assertThat().body("description", Matchers.equalTo("My New Service"));
    }

    @Test
    void saveVersionWhenServiceNotExists() throws Exception {

        String json = "{\n" +
                "    \"name\": \"My New Service1\",\n" +
                "    \"version\": \"v1\"\n" +
                "}";

        RestAssured.given().body(json)
                .header("Content-Type", "application/json")
                .cookies(cookie)
                .post("/services/versions").then()
                .assertThat().statusCode(200)
                .assertThat().body("id", Matchers.equalTo(1))
                .assertThat().body("version", Matchers.equalTo("v1"))
                .assertThat().body("$", Matchers.hasKey("createdDate"));
    }

    @Test
    void saveVersionWhenServiceIsExists() throws Exception {
        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate("insert into tb_services (name,description) values('service-1','service-1 desc')");

        String json = "{\n" +
                "    \"name\": \"service-1\",\n" +
                "    \"version\": \"v1\"\n" +
                "}";

        RestAssured.given().body(json)
                .header("Content-Type", "application/json")
                .cookies(cookie)
                .post("/services/versions").then()
                .assertThat().statusCode(200)
                .assertThat().body("id", Matchers.equalTo(1))
                .assertThat().body("version", Matchers.equalTo("v1"))
                .assertThat().body("$", Matchers.hasKey("createdDate"));

    }

    @Test
    void getVersionById() throws Exception {
        LocalDateTime testTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);
        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate(String.format("insert into tb_services (name,description) values('service-1','service-1 desc');" +
                "INSERT INTO public.tb_versions(\n" +
                "\tservice_id, version, created_date)\n" +
                "\tVALUES ( 1, 'v1', '%s');", testTime));

        RestAssured.given()
                .cookies(cookie)
                .get("/services/1/versions").then()
                .assertThat().statusCode(200)
                .assertThat().body("pageNumber", Matchers.equalTo(0))
                .assertThat().body("pageSize", Matchers.equalTo(10))
                .assertThat().body("totalPages", Matchers.equalTo(1))
                .assertThat().body("totalElements", Matchers.equalTo(1))
                .assertThat().body("content", Matchers.hasSize(1))
                .assertThat().body("content[0].id", Matchers.equalTo(1))
                .assertThat().body("content[0].serviceId", Matchers.equalTo(1))
                .assertThat().body("content[0].version", Matchers.equalTo("v1"))
                .assertThat().body("content[0].createdDate", Matchers.equalTo(testTime.toString()));
    }
}