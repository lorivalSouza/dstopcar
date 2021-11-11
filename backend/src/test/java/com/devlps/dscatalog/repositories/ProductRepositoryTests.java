package com.devlps.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devlps.dscatalog.entities.Product;
import com.devlps.dscatalog.tests.Factory;
@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private Long idExists;
	private Long idNotExists;
	private Long countTotalProducts;
			
	
	@BeforeEach
	void setUp() throws Exception {
		
		idExists = 1L;
		idNotExists = 158L;
		
		countTotalProducts = repository.count();
		
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenByIdExists() {
		
		repository.deleteById(idExists);
		
		Optional<Product> result =  repository.findById(idExists);
		
		Assertions.assertFalse(result.isPresent());;
		
		
	}
	
	@Test
	public void deleteShouldThrowDeleteObjectWhenByIdDoNotExists() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(idNotExists);
		});	
		
	}	
	
	@Test
	public void insertShouldPersistObjectWhenNullId() {
		
		Product prod = Factory.createProduct();
		prod.setId(null);
		
		prod = repository.save(prod);
		Assertions.assertNotNull(prod);
		Assertions.assertEquals(countTotalProducts + 1, prod.getId());
		
	}
	
	@Test
	public void findByIdShouldFindObjectWhenByIdExists() {
		
		Optional<Product> result =  repository.findById(idExists);
		
		Assertions.assertTrue(result.isPresent());
		
	}
	
	@Test
	public void findByIdShouldThrowDoNotFindObjectWhenByIdDoNotExists() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.findById(idNotExists);
		});	
		
	}	

}
