package com.devlps.dscatalog.services;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.devlps.dscatalog.dto.ProductDTO;
import com.devlps.dscatalog.repositories.ProductRepository;
import com.devlps.dscatalog.services.exceptions.ResourceNotFoundException;


@SpringBootTest
@Transactional
public class ProductServiceIT {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository productRepository;
	
	private Long idExists;
	private Long idNotExists;
	private Long dependentId;
	private Long countTotalProducts;
			
	
	@BeforeEach
	void setUp() throws Exception {
		
		idExists = 1L;
		idNotExists = 158L;
		dependentId = 4L;
		
		countTotalProducts = productRepository.count();
		
	}
	
	@Test
	public void deleteSholdDeleteResourceWhenIdExists() {
		
		service.deleteById(idExists);
		
		Assertions.assertEquals(countTotalProducts - 1 , productRepository.count());
		
	}
	
	@Test
	public void deleteSholdReturnThrowResourceNotFoundExceptionWhenIdNotExists() {		
		
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.deleteById(idNotExists);			
		});
		
	}
	
	@Test
	public void findAllPagedShouldFindPagedObject() {
		
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result =  service.findAllPaged("", 0L, pageable);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(countTotalProducts , result.getTotalElements());
		
		
	}
	
	@Test
	public void findAllPagedShouldEmptyPagedObjectWhenPageNumberNotExist() {
		
		Pageable pageable = PageRequest.of(50, 10);
		
		Page<ProductDTO> result =  service.findAllPaged("", 0L, pageable);
		
		Assertions.assertTrue(result.isEmpty());
		
		
	}
	
	@Test
	public void findAllPagedShouldOrderedPageObjectWhenPageOrderDesc() {
		
		Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<ProductDTO> result =  service.findAllPaged("", 0L, pageable);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		
		
	}
	
}
