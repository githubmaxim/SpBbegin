package com.example.demo2.product;

        import com.example.demo2.entity.Product;
        import com.example.demo2.repository.ProductRepository;
        import com.fasterxml.jackson.databind.ObjectMapper;
        import org.junit.jupiter.api.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
        import org.springframework.boot.test.context.SpringBootTest;
        import org.springframework.http.MediaType;
        import org.springframework.test.web.servlet.MockMvc;
        import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

        import java.util.Arrays;

        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
//@WebMvcTest //вместо @SpringBootTest, при желании запустить Unit-тестирование (при этом нужно будет сделать еще, кроме заглушки сетевого соединения, заглушку для репозитория, сервиса и т.д.)
//@AutoConfigureTestDatabase  //для работы не с реальной, а с виртуально БД + нужно будет внести изменения в файлы "pom.xml" и "application.properties"
@AutoConfigureMockMvc

class IntegrTestsWithBuilderInProduct {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    //	@MockBean //вместо @Autowired заглушка для репозитория при @WebMvcTest
    private ProductRepository repository;
    @Autowired
    private MockMvc mockMvc;

//Применяется если БД для тестов отдельная
//	@AfterEach
//	public void resetDb() {
//		repository.deleteAll();
//	}

    private Product createTestProduct(String name, float price) {
        Product emp = Product.builder().name(name).price(price).build();
        return repository.save(emp);
    }

    //при @WebMvcTest и @MockBean нужно будет в каждом тестовом методе вначале создавать заглушку для поведения
    @Test
    public void add() throws Exception {
        Product product = Product.builder().name("Nick").price(500).build();

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
    public void get() throws Exception {
        Product product = createTestProduct("Nick", 1000);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", product.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value("Nick"))
                .andExpect(jsonPath("$.price").value("1000.0"));
    }

    @Test
    public void getAll() throws Exception {
        repository.deleteAll();

        Product p1 = createTestProduct("Nick", 1000);
        Product p2 = createTestProduct("Max", 100);

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(p1, p2))));

    }

    @Test
    void update() throws Exception {
        Integer id = createTestProduct("Jack", 432).getId();

        mockMvc.perform(put("/products/{id}", id)
                .content(objectMapper.writeValueAsString(Product.builder().id(id).name("Max").price(555).build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Max"))
                .andExpect(jsonPath("$.price").value(555));
    }

    @Test
    public void delete() throws Exception {
        Product product = createTestProduct("Nick", 1000);

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", product.getId())) //тут мы значение {id} подставляем после запятой в этих же скобках
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(product)));
    }

    @Test
    public void deleteAll() throws Exception {
        Product product = createTestProduct("Nick", 1000);

        mockMvc.perform(MockMvcRequestBuilders.delete("/products?param1={param}", "all")) //тут мы значение param1={param1} подставляем после запятой в этих же скобках
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("You Deleted All !!!"));
    }


}
