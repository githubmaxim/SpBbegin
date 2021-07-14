package com.example.demo2.product;

import com.example.demo2.entity.product.Product3;
import com.example.demo2.repository.product.ProductRepositoryUuid;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureTestDatabase
//для работы не с реальной, а с виртуально БД + нужно будет внести изменения в файлы "pom.xml" и "application.properties"
@TestPropertySource(locations = "classpath:myTestApplication.properties") //подгружает не стаедартный "application.properties", необходимый "myTestApplication.properties"
@AutoConfigureMockMvc
class IntegrTestsWithOUTBuilderInProduct3ByUuid { //!!!методы репозитория findById() и deleteAll() отказываются работать с UUID!!!!
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepositoryUuid repository;

    @Autowired
    private MockMvc mockMvc;

//Применяется если БД для тестов отдельная
//	@AfterEach
//	public void resetDb() {
//		repository.deleteAll();
//	}

    private Product3 createTestPerson(String name, float price) {
        Product3 emp = new Product3(name, price);
        return repository.save(emp);
    }

    @Test
    public void add() throws Exception {
        Product3 product = new Product3("Nick", 500);

        mockMvc.perform(MockMvcRequestBuilders.post("/products3")
                .content(objectMapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Nick"))
                .andExpect(jsonPath("$.price").value("500.0"));
    }

    @Test
    public void get() throws Exception {
//        Product3 product = createTestPerson("Nick", 1000);
        Product3 product3 = new Product3("Nick", 1500);
        repository.save(product3);

        mockMvc.perform(MockMvcRequestBuilders.get("/products3/{id}", product3.getId()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value("Nick"))
                .andExpect(jsonPath("$.price").value("1500.0"));
    }

    @Test
    public void getAll() throws Exception {
//        repository.deleteAll();

//        Product3 p1 = createTestPerson("Nick", 1000);
//        Product3 p2 = createTestPerson("Max", 100);
        Product3 p1 = new Product3("Nick", 1000);
        repository.save(p1);
        Product3 p2 = new Product3("Max", 100);
        repository.save(p2);


        mockMvc.perform(MockMvcRequestBuilders.get("/products3"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(p1, p2))));

    }


    @Test
    public void delete() throws Exception {
//        Product3 product = createTestPerson("Nick", 1000);
        Product3 product = new Product3("Nick", 999);
        repository.save(product);

        mockMvc.perform(MockMvcRequestBuilders.delete("/products3/{id}", product.getId())) //тут мы значение {id} подставляем после запятой в этих же скобках
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(product)));
    }


}

