package com.devlps.dscatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devlps.dscatalog.dto.ProductDTO;
import com.devlps.dscatalog.services.ProductService;
import com.devlps.dscatalog.services.exceptions.DatabaseException;
import com.devlps.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devlps.dscatalog.tests.Factory;
import com.devlps.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTest {

//	@InjectMocks
//	private ProductResource resource;
//	
	private Long idExists;
	private Long idNotExists;
	private Long dependentId;
//	
	private PageImpl<ProductDTO> page;
//	private Product product;
//	private Category category;
	ProductDTO productDTO;	
	
	private String username;
	private String password;
//
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ProductService service;	
	
	
	@BeforeEach
	void setUp() throws Exception {
//
		idExists = 1L;
		idNotExists = 158L;
		dependentId = 4L;
		
		username = "maria@gmail.com";
		password = "123456";
//
//		product = Factory.createProduct();
//		category = Factory.createCategory();
		productDTO = Factory.createProductDTO();
		
		page = new PageImpl<>(List.of(productDTO));
		Mockito.when(service.findAllPaged(ArgumentMatchers.any(), ArgumentMatchers.any(), (Pageable)ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(service.findById(idExists)).thenReturn(productDTO);
		Mockito.when(service.findById(idNotExists)).thenThrow(ResourceNotFoundException.class);
		Mockito.when(service.update(ArgumentMatchers.eq(idExists), ArgumentMatchers.any())).thenReturn(productDTO);
		Mockito.when(service.update(ArgumentMatchers.eq(idNotExists), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
		Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(productDTO);
		
		Mockito.doNothing().when(service).deleteById(idExists);
		Mockito.doThrow(ResourceNotFoundException.class).when(service).deleteById(idNotExists);
		Mockito.doThrow(DatabaseException.class).when(service).deleteById(dependentId);
		
	}
	
	
	
	@Test
	public void findAllPagedShouldFindPagedObject() throws Exception{
		
		//This way more advanced
		//mockMvc.perform(get("/products")).andExpect(status().isOk());
		
		
		//This way more simple to undestand
		ResultActions result =  mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
	}
	
	@Test
	public void findByIdShouldReturnedProductObjectWhenIdExist() throws Exception{		
		
		ResultActions result =  mockMvc.perform(get("/products/{id}", idExists)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void findByIdShouldReturnedNotFoundWhenIdNotExist() throws Exception{		
		
		ResultActions result =  mockMvc.perform(get("/products/{id}", idNotExists)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnedProductObjectWhenIdExist() throws Exception{
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		String jsonBody =  objectMapper.writeValueAsString(productDTO);
		
		ResultActions result =  mockMvc.perform(put("/products/{id}", idExists)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void updateShouldReturnedNotFoundWhenIdNotExist() throws Exception{
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);		
		
		String jsonBody =  objectMapper.writeValueAsString(productDTO);
		
		ResultActions result =  mockMvc.perform(put("/products/{id}", idNotExists)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void insertShouldReturnedProductObject() throws Exception{
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		String jsonBody =  objectMapper.writeValueAsString(productDTO);
		
		ResultActions result =  mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void deleteShouldReturnedProductObjectWhenIdExist() throws Exception{
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);		
		
		ResultActions result =  mockMvc.perform(delete("/products/{id}", idExists)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MediaType.APPLICATION_JSON));	
		
		result.andExpect(status().isNoContent());
		
	}
	
	@Test
	public void deleteShouldReturnedResourceNotFoundExceptionWhenIdNotExist() throws Exception{
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);		
		
		ResultActions result =  mockMvc.perform(delete("/products/{id}", idNotExists)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldReturnedDatabaseExceptionWhenExistDependency() throws Exception{
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);		
		
		ResultActions result =  mockMvc.perform(delete("/products/{id}", dependentId)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isBadRequest());
	}
	

}
