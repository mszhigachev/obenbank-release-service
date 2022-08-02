package ru.openbank.releasesservice.controller;

import io.restassured.RestAssured;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mssql.InsertIdentityOperation;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationServiceControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseDataSourceConnection databaseDataSourceConnection;

    @Before
    public void setUp() throws Exception {

        FlatXmlDataSetBuilder dataSetBuilder = new FlatXmlDataSetBuilder();
        dataSetBuilder.setColumnSensing(true);

        IDataSet testPSDataset = new CompositeDataSet(new IDataSet[]{
                dataSetBuilder.build(getClass().getResourceAsStream("test-db-data/dataset.xml"))
        });

        new InsertIdentityOperation(DatabaseOperation.CLEAN_INSERT).execute(databaseDataSourceConnection, new CompositeDataSet(testPSDataset));

        RestAssured.port = port;
        RestAssured.basePath = "/services";
    }

    @Test
    void getAll() throws Exception {

        given().when().get("").then();

    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void testSave() {
    }

    @Test
    void getVersionById() {
    }
}