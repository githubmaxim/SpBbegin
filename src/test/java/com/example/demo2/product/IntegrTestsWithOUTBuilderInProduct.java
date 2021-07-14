package com.example.demo2.product;

import com.example.demo2.entity.product.Product;
import com.example.demo2.repository.product.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
//@WebMvcTest //вместо @SpringBootTest, при желании запустить Unit-тестирование (при этом нужно будет сделать еще, кроме заглушки сетевого соединения, заглушку для репозитория)
//@AutoConfigureTestDatabase
////для работы не с реальной, а с виртуально БД + нужно будет внести изменения в файлы "pom.xml" и "application.properties"
//@TestPropertySource(locations = "classpath:myTestApplication.properties") //подгружает не стаедартный "application.properties", необходимый "myTestApplication.properties"
@AutoConfigureMockMvc
class IntegrTestsWithOUTBuilderInProduct {
	@Autowired
	private ObjectMapper objectMapper; //обертка встроенной библиотеки Jackson для работы с Json
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

	//метод для преобразования объекта Java в строку JSON, который используется во 2-м типе создания тестов
	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	//метод для преобразования строки JSON в объект Java, который используется во 2-м типе создания тестов
	protected <T> T mapFromJson(String json, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}
	
    //метод создания и записи нового объекта в БД, который используется во 1-м типе создания тестов
	private Product createTestProduct(String name, float price) {
		Product emp = new Product(name, price);
		return repository.save(emp);
	}

	//
	//1-й тип создания тестов через проверку полученного ответа с помощью команды ".andExpect(...)"
	//
	@Test
	public void add() throws Exception {
		Product product = new Product("Nick", 500); //создаем в БД новую запись + получаем сохраненную объект/сущьность уже с заполненным (не "null") id номером

		mockMvc.perform(post("/products")
					.content(objectMapper.writeValueAsString(product)) //преобразовываем полученный объект/сущность, с заполненным (не "null") номером id, в строку JSON и
				// отправляем его в контроллер, находящийся на листе "/products", в метод азоглавленный аннотацией @PostMapping. Этот метод просто перезапишет еще раз эту же сущность в БД,
				// т.к. исользуется метод "save", который если находит уже существующий такой id сущности в БД, то просто перезапишет эту сущность с предлагаемыми для записи полями под этим
				// же id (см. в папке "Литература/Spring" в файле "Spring Boot-руководство New.docx" в главе Spring Data JPA)
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
				.content(objectMapper.writeValueAsString(new Product(id, "Max", 555)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.name").value("Max"))
				.andExpect(jsonPath("$.price").value(555));
	}

	@Test
	public void deleteId() throws Exception {
		Product product = createTestProduct("Nick", 1000);

		mockMvc.perform(delete("/products/{id}", product.getId())) //тут мы значение {id} подставляем после запятой в этих же скобках
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(product)));
	}

	@Test
	public void deleteAll() throws Exception {
		Product product = createTestProduct("Nick", 1000);

		mockMvc.perform(delete("/products?param1={param}", "all")) //тут мы значение param1={param1} подставляем после запятой в этих же скобках
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("You Deleted All !!!"));
	}

    //
	//2-й тип создания тестов через получение всей информации результата выполненного запроса
	//(методом .endReturn()), выборке из нее необходимой части и последующей проверке этой части инфы.
	// через assertEquals. Но как
	//


	@Test
	public void createProduct() throws Exception {
		Product product = createTestProduct("Emma", 7);
		String inputJson = mapToJson(product);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/products")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson))
				.andReturn();

		int status =     mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString(); //получаем все тело ответа
		Product pr = mapFromJson(content, Product.class); //десериализуем тело ответа из Json-формата обратно в объект Product-класса
		assertEquals(pr.getId(), product.getId()); //сравниваем значение id до сериализации со значением сериализованного id отправляемого в ответе
	}

	@Test
	public void getProductsList() throws Exception {
		Product product = createTestProduct("Emma", 7);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/products")
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Product[] productlist = mapFromJson(content, Product[].class);
		assertTrue(productlist.length > 0);
	}
}
