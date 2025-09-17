package acceptance;

import checkout.component.app.CheckoutComponent;
import checkout.component.dto.Receipt;
import io.restassured.RestAssured;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.path.json.mapper.factory.DefaultJackson2ObjectMapperFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CheckoutComponent.class)
public class SystemTests {

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    void fullCheckoutScenario() {

        String cookie = RestAssured.given()
                .when().post("/api/checkout/start")
                .then().statusCode(201)
                .extract().header("Set-Cookie");

        float total = RestAssured.given().cookie(cookie)
                .when().post("/api/checkout/add?item_id=Test_A&quantity=27")
                .then().statusCode(200)
                .extract()
                .as(Float.class);
        assertEquals(205f, total);

        total = RestAssured.given().cookie(cookie)
                .when().post("/api/checkout/add?item_id=Test_A&quantity=11")
                .then().statusCode(200)
                .extract()
                .as(Float.class);
        assertEquals(285f, total);

        total = RestAssured.given().cookie(cookie)
                .when().post("/api/checkout/add?item_id=Test_B&quantity=15")
                .then().statusCode(200)
                .extract()
                .as(Float.class);
        assertEquals(299f, total);

        Receipt receipt = RestAssured.given().cookie(cookie)
                .when().get("/api/checkout/receipt")
                .then().statusCode(200)
                .extract()
                .as(Receipt.class, new Jackson2Mapper(new DefaultJackson2ObjectMapperFactory()));
        assertEquals(total, receipt.getTotalPrice());
        assertEquals(2, receipt.getRecords().size());
    }
}
