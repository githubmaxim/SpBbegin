package com.example.demo2.product.controller;

import com.example.demo2.controller.product.ProductController;
import com.example.demo2.entity.product.Product;
import com.example.demo2.repository.product.ProductRepository;
import com.example.demo2.repository.product.ProductRepositoryUuid;
import com.example.demo2.service.users.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)//вместо @SpringBootTest + говорим создать экземпляр только одного контроллера иначе потребует создать @MockBean-заглушки для всех сервисов и репозиториев остальных контроллеров
//@AutoConfigureTestDatabase //для работы не с реальной, а с виртуально БД + нужно будет внести изменения в файлы "pom.xml" и "application.properties"
//@TestPropertySource(locations = "classpath:myTest.properties") //подгружает не стаедартный "application.properties", необходимый "myTest.properties"
@AutoConfigureMockMvc
class UnitTestWithBuilderInProduct {

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductRepository repo;
    @Autowired
    private MockMvc mockMvc;

    private Product createTestProduct(Integer id, String name, float price) {
        Product emp = Product.builder().id(id).name(name).price(price).build();
//        return repository.save(emp);  //сохранение тут не нужно, т.к. реальной работы с БД не будет (даже со встроенной) и для него тоже нужно будет создавать заглушку
        return emp;
    }

    @Test
    void add() throws Exception {
        Product product = Product.builder().id(2).name("Nick").price(500).build(); //при Unit-тестировании при создании объекта задавать нужно все поля включая "id"

        Mockito.when(repo.save(Mockito.any())).thenReturn(product); //требуем, чтобы при запуске метода "save()" (репозитория "repo") с любыми параметрами, всегда на выходе выдавался объект "product", но запись в БД при этом не происходит

        mockMvc.perform(post("/products")
                .content(objectMapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Nick"))
                .andExpect(jsonPath("$.price").value("500.0"));
    }

    @Test
    void get() throws Exception {
        Product product = Product.builder().id(2).name("Nick").price(1000).build();

        Mockito.when(repo.findById(Mockito.any())).thenReturn(Optional.of(product));

        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", product.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value("Nick"))
                .andExpect(jsonPath("$.price").value("1000.0"));
    }

    @Test
    void getAll() throws Exception {
        repo.deleteAll();

        Product p1 = createTestProduct(1,"Nick", 1000);
        Product p2 = createTestProduct(2,"Max", 100);

        Mockito.when(repo.findAll()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(p1, p2))));
    }

    @Test
    void update() throws Exception {
        Product product = Product.builder().id(2).name("Nick").price(1000).build();

        Mockito.when(repo.save(Mockito.any())).thenReturn(product);
        Mockito.when(repo.findById(Mockito.any())).thenReturn(Optional.ofNullable(product));

        mockMvc.perform(put("/products/2")
                .content(objectMapper.writeValueAsString(Product.builder().id(2).name("Nick").price(1000).build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.name").value("Nick"));
    }

    @Test
    void delete() throws Exception {
        Product product = createTestProduct(1,"Nick", 1000);

        Mockito.when(repo.findById(Mockito.any())).thenReturn(Optional.of(product));

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", product.getId())) //тут мы значение {id} подставляем после запятой в этих же скобках
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(product)));
    }

    @Test
    void deleteAll() throws Exception {
        Product p1 = createTestProduct(1,"Nick", 1000);
        Product p2 = createTestProduct(2,"Max", 100);

        Mockito.when(repo.findAll()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(MockMvcRequestBuilders.delete("/products?param1={param}", "all")) //тут мы значение param1={param1} подставляем после запятой в этих же скобках
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("You Deleted All !!!"));
    }
}