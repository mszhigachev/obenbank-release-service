package ru.openbank.releasesservice.controller;

import io.restassured.RestAssured;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mssql.InsertIdentityOperation;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class ApplicationServiceControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseDataSourceConnection databaseDataSourceConnection;

    @BeforeEach
    public void setUp() throws Exception {

        FlatXmlDataSetBuilder dataSetBuilder = new FlatXmlDataSetBuilder();
        dataSetBuilder.setColumnSensing(true);

        IDataSet testPSDataset = new CompositeDataSet(new IDataSet[]{
                dataSetBuilder.build(getClass().getResourceAsStream("/test-db-data/dataset.xml"))
        });

        new InsertIdentityOperation(DatabaseOperation.CLEAN_INSERT).execute(databaseDataSourceConnection, new CompositeDataSet(testPSDataset));

        RestAssured.port = port;
        RestAssured.basePath = "/";
    }

    @Test
    void getAll() throws Exception {

        given().get("/services").then()
                .assertThat().statusCode(200)
                .assertThat().body("$", hasSize(1))
                .assertThat().body("[0].id", equalTo(1))
                .assertThat().body("[0].name", equalTo("service-1"))
                .assertThat().body("[0].description", equalTo("service-1 desc"));
    }

    @Test
    void save() throws Exception {
        String json = "{\n" +
                "    \"name\": \"New Service\",\n" +
                "    \"description\": \"New Service desc\"\n" +
                "}";

        given().body(json)
                .post("/services").then()
                .assertThat().statusCode(200)
                .assertThat().body("[0].id", equalTo(2))
                .assertThat().body("[0].name", equalTo("New Service"))
                .assertThat().body("[0].description", equalTo("New Service desc"));
    }

    @Test
    void update() {
    }

    @Test
    void saveVersion() {
    }

    @Test
    void getVersionById() {
    }
}