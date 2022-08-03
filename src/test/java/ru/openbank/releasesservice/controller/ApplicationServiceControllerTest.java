package ru.openbank.releasesservice.controller;

import io.restassured.RestAssured;
import org.dbunit.database.DatabaseDataSourceConnection;
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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class ApplicationServiceControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseDataSourceConnection databaseDataSourceConnection;

    @BeforeEach
    public void setUp() throws Exception {
        RestAssured.port = port;
        RestAssured.basePath = "/";
    }
    @AfterEach
    public void setTerDown() throws Exception{
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

        given().body(json)
                .header("Content-Type", "application/json")
                .post("/services").then()
                .assertThat().statusCode(200)
                .assertThat().body("id", equalTo(1))
                .assertThat().body("name", equalTo("service-1"))
                .assertThat().body("description", equalTo("service-1 desc"));
        Connection conn = databaseDataSourceConnection.getConnection();
    }


    @Test
    void getAll() throws Exception {
        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate("insert into tb_services (name,description) values('service-1','service-1 desc')");

        given().get("/services").then()
                .assertThat().statusCode(200)
                .assertThat().body("$", hasSize(1))
                .assertThat().body("[0].id", equalTo(1))
                .assertThat().body("[0].name", equalTo("service-1"))
                .assertThat().body("[0].description", equalTo("service-1 desc"));
    }


    @Test
    void update() throws Exception {
        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate("insert into tb_services (name,description) values('service-1','service-1 desc')");

        String json = "{\n" +
                " \"description\": \"My New Service\"\n" +
                "}\n";

        given().body(json)
                .header("Content-Type", "application/json")
                .put("/services/1").then()
                .assertThat().statusCode(200)
                .assertThat().body("id", equalTo(1))
                .assertThat().body("description", equalTo("My New Service"));
    }

    @Test
    void saveVersionWhenServiceNotExists() throws Exception {

        String json = "{\n" +
                "    \"name\": \"My New Service1\",\n" +
                "    \"version\": \"v1\"\n" +
                "}";

        given().body(json)
                .header("Content-Type", "application/json")
                .post("/services/versions").then()
                .assertThat().statusCode(200)
                .assertThat().body("id", equalTo(1))
                .assertThat().body("version", equalTo("v1"))
                .assertThat().body("$", hasKey("createdDate"));
    }

    @Test
    void saveVersionWhenServiceIsExists() throws Exception {
        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate("insert into tb_services (name,description) values('service-1','service-1 desc')");

        String json = "{\n" +
                "    \"name\": \"service-1\",\n" +
                "    \"version\": \"v1\"\n" +
                "}";

        given().body(json)
                .header("Content-Type", "application/json")
                .post("/services/versions").then()
                .assertThat().statusCode(200)
                .assertThat().body("id", equalTo(1))
                .assertThat().body("version", equalTo("v1"))
                .assertThat().body("$", hasKey("createdDate"));

    }

    @Test
    void getVersionById() throws Exception {
        Connection conn = databaseDataSourceConnection.getConnection();
        conn.createStatement().executeUpdate("insert into tb_services (name,description) values('service-1','service-1 desc');" +
                "INSERT INTO public.tb_versions(\n" +
                "\tservice_id, version, created_date)\n" +
                "\tVALUES ( 1, 'v1', '2022-01-01 00:00:00');");

        given().get("/services/1/versions").then()
                .assertThat().statusCode(200)
                .assertThat().body("pageNumber", equalTo(0))
                .assertThat().body("pageSize", equalTo(10))
                .assertThat().body("totalPages", equalTo(1))
                .assertThat().body("totalElements", equalTo(1))
                .assertThat().body("content", hasSize(1))
                .assertThat().body("content[0].id", equalTo(1))
                .assertThat().body("content[0].serviceId", equalTo(1))
                .assertThat().body("content[0].version", equalTo("v1"))
                .assertThat().body("content[0].createdDate", equalTo("2022-01-01T00:00:00.000+00:00"));

    }
}