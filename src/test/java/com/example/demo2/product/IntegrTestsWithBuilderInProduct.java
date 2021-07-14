package com.example.demo2.product;

        import com.example.demo2.entity.product.Product;
        import com.example.demo2.repository.product.ProductRepository;
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

        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest //@WebMvcTest вместо @SpringBootTest, при желании запустить Unit-тестирование (при этом нужно будет сделать еще, кроме заглушки сетевого соединения, заглушку для репозитория, сервиса и т.д.)
//@AutoConfigureTestDatabase  //для работы не с реальной, а с виртуально БД + нужно будет внести изменения в файлы "pom.xml" и "application.properties", см. в папке Литература/Spring в файле "Spring Boot - руководство New.docx" объяснение к аннотации @DataJpaTest
//@TestPropertySource(locations = "classpath:myTestApplication.properties") //подгружает нестандартный "application.properties", необходимый "myTestApplication.properties"
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
        Product product = Product.builder().name("Ann").price(300).build();

        mockMvc.perform(post("/products")
                .content(objectMapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Ann"))
                .andExpect(jsonPath("$.price").value("300.0"));
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
        Integer id = createTestProduct("Jack", 432).getId(); //создаем в БД новую запись, далее получаем сохраненную объект/сущьность уже с заполненным (не "null") id номером, и получаем id

        mockMvc.perform(put("/products/{id}", id)
                .content(objectMapper.writeValueAsString(Product.builder().id(id).name("Max").price(555).build())) //создаем объект/сущьность с заполненным (не "null") номером id.
                // Преобразовываем полученный объект/сущность, в строку JSON и отправляем его в контроллер, находящийся на листе "/products", в метод азоглавленный аннотацией @PutMapping("/{id}").
                // Этот метод просто перезапишет еще раз эту же сущность в БД, т.к. исользуется метод "save", который если находит уже существующий такой id сущности в БД, то просто
                // перезапишет эту сущность, с предлагаемыми для записи полями, под этим же id (см. в папке "Литература/Spring" в файле "Spring Boot-руководство New.docx" в главе Spring Data JPA)
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
