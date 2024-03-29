/*
 * ProductManager
 * Version 1.0
 * August 14, 2021 
 * Copyright 2021 Tecnologico de Monterrey
 */
package mx.tec.web.lab.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.tec.web.lab.manager.ProductManager;
import mx.tec.web.lab.vo.ProductVO;

/**
 * Product REST Controller
 * @author Enrique Sanchez
 * @version 1.0
 */
@RestController
@RequestMapping("/ecom/api/v1")
@Validated
public class ProductController {
	private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);	
	
	/** A reference to the Product Manager */
	@Resource
	private ProductManager productManager;

	/**
	 * The end point for GET {url}/products
	 * @return a json list of all the products
	 */
	@GetMapping("/products")
	public ResponseEntity<List<ProductVO>> getProducts() {
		LOG.info("Getting all the products");
		List<ProductVO> products = productManager.getProducts();
		return new ResponseEntity<>(products, HttpStatus.OK);
	}	
	
	/**
	 * The end point for GET {url}/products/{id}
	 * @param id Product id
	 * @return a json containing the product info and status 200 if the product is found or status 204 if the product is not found
	 */
	@GetMapping("/products/{id}")
	public ResponseEntity<ProductVO> getProduct(@PathVariable(value = "id") @Min(value = 0, message = "The id must be positive") long id) {
		LOG.info("Getting the product by id: {}", id);
		ResponseEntity<ProductVO> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		Optional<ProductVO> product = productManager.getProduct(id);
		
		if (product.isPresent()) {
			responseEntity = new ResponseEntity<>(product.get(), HttpStatus.OK);
		} else {
			LOG.warn("Product with id {} not found ", id);
		}

		return responseEntity;
	}

	/**
	 * The end point for GET {url}/products?search={pattern}
	 * @param search Pattern to search
	 * @return a json list of all the products matching the pattern
	 */
	@GetMapping(value="/products", params="search")
	public ResponseEntity<List<ProductVO>> getProducts(@RequestParam String search) {
		LOG.info("get products by name: {}", search);
		List<ProductVO> products = productManager.getProducts(search);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}	
	
	/**
	 * The end point for POST {url}/products/
	 * @param newProduct a json containing the info for the new product
	 * @return If the product is created successfully then status 201 and the product info is returned, otherwise it returns status 400 
	 */
	@PostMapping("/products")
	public ResponseEntity<ProductVO> addProduct(@Valid @RequestBody ProductVO newProduct) {
		LOG.info("add a new product: {}", newProduct);
		ProductVO product = productManager.addProduct(newProduct);		
		return new ResponseEntity<>(product, HttpStatus.CREATED);
	}

	/**
	 * The end point for PUT {url}/products/{id}
	 * @param id Product id
	 * @param modifiedProduct a json containing the info for the modified product
	 * @return status 200 if the product is found and updated or status 204 if the product is not found
	 */
	@PutMapping("/products/{id}")
	public ResponseEntity<ProductVO> updateProduct(@PathVariable(value = "id") long id, @RequestBody ProductVO modifiedProduct) {
		LOG.info("delete an existing product by product: {}", modifiedProduct);
		ResponseEntity<ProductVO> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		Optional<ProductVO> product = productManager.getProduct(id);
		
		if (product.isPresent()) {
			productManager.updateProduct(id, modifiedProduct);
			responseEntity = new ResponseEntity<>(HttpStatus.OK);
		} else {
			LOG.warn("Product not found");
		}
		
		return responseEntity;
	}

	/**
	 * The end point for DELETE {url}/products/{id}
	 * @param id Product id
	 * @return status 200 if the product is found and deleted or status 204 if the product is not found
	 */
	@DeleteMapping("/products/{id}")
	public ResponseEntity<ProductVO> deleteProduct(@PathVariable(value = "id") long id) {
		LOG.info("delete an existing product by id: {}", id);
		ResponseEntity<ProductVO> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		Optional<ProductVO> product = productManager.getProduct(id);
		
		if (product.isPresent()) {
			productManager.deleteProduct(product.get());
			responseEntity = new ResponseEntity<>(HttpStatus.OK);
		}
		
		return responseEntity;
	}
	
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> onConstraintViolationException(final ConstraintViolationException cve) {
    	LOG.error("Invalid parameter", cve);
        return new ResponseEntity<>(cve.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> onMethodArgumentNotValidException(final MethodArgumentNotValidException manve) {
    	LOG.error("Invalid input", manve);
    	
    	List<String> messages = new ArrayList<>();
    	
    	List<ObjectError> errors = manve.getAllErrors();
    	for (ObjectError error : errors) {
    		LOG.debug(error.getDefaultMessage());
        	messages.add(error.getDefaultMessage());
    	}

    	return new ResponseEntity<>(messages.toString(), HttpStatus.BAD_REQUEST);
    }    
}
