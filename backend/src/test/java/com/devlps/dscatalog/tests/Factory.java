package com.devlps.dscatalog.tests;

import java.time.Instant;

import com.devlps.dscatalog.dto.ProductDTO;
import com.devlps.dscatalog.entities.Category;
import com.devlps.dscatalog.entities.Product;

public class Factory {
	
	public static Product createProduct() {
		Instant dataA = Instant.now();
		
		Product prod =  new Product();
		prod.setId(1L);
		prod.setDate(dataA);
		prod.setDescription("Best phone");
		prod.setImgURL("http://teste");
		prod.setName("Phone312");
		prod.setPrice(1223.0);
		
		prod.getCategories().add(createCategory());
		
		return prod;
	}
	
	public static ProductDTO createProductDTO() {
		Product prod = createProduct();
		
		
		return new ProductDTO(prod, prod.getCategories());
	}
	
	public static Category createCategory() {
		
		return new Category(2L, "Electronics");
	}

}
