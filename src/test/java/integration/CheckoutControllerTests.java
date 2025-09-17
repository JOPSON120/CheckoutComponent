package integration;

import checkout.component.app.CheckoutComponent;
import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = CheckoutComponent.class)
@AutoConfigureMockMvc
public class CheckoutControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCheckoutService() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/checkout/start"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        assertNotNull(session);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/checkout/add?item_id=Test_A").session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new AssertionMatcher<>() {
                    @Override
                    public void assertion(String actual) throws AssertionError {
                        if (Float.parseFloat(actual) != 10f) {
                            throw new AssertionError("Wrong price response!");
                        }
                    }
                }));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/checkout/receipt").session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_price").value(10f));
    }

    @Test
    void testCheckoutWrongNumber() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/checkout/start"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        assertNotNull(session);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/checkout/add?item_id=Test_A&quantity=0").session(session))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testNoCheckoutWhenNoSession() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/checkout/add?item_id=Test_A"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testNoReceiptWhenNoSession() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/checkout/receipt"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
