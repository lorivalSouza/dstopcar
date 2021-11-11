package com.devlps.dscatalog.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devlps.dscatalog.dto.ProductDTO;
import com.devlps.dscatalog.entities.Category;
import com.devlps.dscatalog.entities.Product;
import com.devlps.dscatalog.repositories.CategoryRepository;
import com.devlps.dscatalog.repositories.ProductRepository;
import com.devlps.dscatalog.services.exceptions.DatabaseException;
import com.devlps.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devlps.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private Long idExists;
	private Long idNotExists;
	private Long dependentId;
	
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	ProductDTO productDTO;			
	
	@BeforeEach
	void setUp() throws Exception {
		
		idExists = 1L;
		idNotExists = 158L;
		dependentId = 4L;
		
		product = Factory.createProduct();
		category = Factory.createCategory();
		productDTO = Factory.createProductDTO();
		
		page = new PageImpl<>(List.of(product));
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		Mockito.when(repository.findById(idExists)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(idNotExists)).thenReturn(Optional.empty());
		
		Mockito.when(repository.find(ArgumentMatchers.any(),ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.getOne(idExists)).thenReturn(product);
		Mockito.when(repository.getOne(idNotExists)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getOne(idExists)).thenReturn(category);
		Mockito.when(categoryRepository.getOne(idNotExists)).thenThrow(EntityNotFoundException.class);
		
		
		Mockito.doNothing().when(repository).deleteById(idExists);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(idNotExists);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
	}
	
	@Test
	public void deleteShouldDoNothingWhenByIdExists() {				
				
		Assertions.assertDoesNotThrow(() -> {
			service.deleteById(idExists);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(idExists);
		
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenByIdDoNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.deleteById(idNotExists);
		});	
		Mockito.verify(repository).deleteById(idNotExists);
	}	
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenByIdDoNotExists() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.deleteById(dependentId);
		});	
		Mockito.verify(repository).deleteById(dependentId);
	}
	
	@Test
	public void findAllPagedShouldFindPagedObject() {
		
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result =  service.findAllPaged("", 0L, pageable);
		
		Assertions.assertNotNull(result);		
		
	}
	
	@Test
	public void findByIdShouldFindObjectWhenByIdExists() {
		
		productDTO =  service.findById(idExists);
		
		Assertions.assertNotNull(productDTO);
		
		Mockito.verify(repository).findById(idExists);
		
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenByIdNotExists() {
		
		//ProductDTO result =  service.findById(idNotExists);
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(idNotExists);
		});
		
		Mockito.verify(repository).findById(idNotExists);
		
	}
	
	@Test
	public void updateShouldPersistObjectWhenExistsId() {
		
		productDTO = service.update(idExists, productDTO);
		Assertions.assertNotNull(productDTO);
		
	}	
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenByIdNotExists() {
		
		//ProductDTO result =  service.findById(idNotExists);
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(idNotExists, productDTO);
		});
		
	}	
	
	
	
	@Test
	public void insertShouldPersistObjectWhenNullId() {
		
		productDTO = service.insert(productDTO);
		Assertions.assertNotNull(productDTO);
		
	}

}
