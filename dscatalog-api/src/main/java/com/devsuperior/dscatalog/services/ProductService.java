package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.dto.ProductMinDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.projections.ProductProjection;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Transactional(readOnly = true)
	public Page<ProductMinDTO> findAllPaged(Pageable pageable) {
		Page<Product> list = this.repository.findAll(pageable);
		return list.map(ProductMinDTO::new);
	}

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllProductPaged(Pageable pageable) {
		Page<Product> list = this.repository.findAll(pageable);
		return list.map(ProductDTO::new);
	}

	@Transactional(readOnly = true)
	public Page<ProductMinDTO> searchByNamePageable(String name, Pageable pageable) {
		Page<Product> list = this.repository.searchByNamePageable(name, pageable);
		return list.map(ProductMinDTO::new);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<ProductDTO> searchByNameAndIdPageable(String name, String categoryId, Pageable pageable) {
		List<Long> categoryIds = new ArrayList<>();
		if (!"0".equals(categoryId)) {
			categoryIds = Arrays.stream(categoryId.split(",")).map(Long::parseLong).toList();
		}
		Page<ProductProjection> productsPage = repository.searchProducts(name, categoryIds, pageable);
		List<Long> productIds = productsPage.map(ProductProjection::getId).toList();
		List<Product> entities = repository.searchProductsWithCategories(productIds);

		//retornando lista ordenada
		entities = (List<Product>) Utils.replaceGeneric(productsPage.getContent(), entities);

		List<ProductDTO> productDTOS = entities.stream().map(e -> new ProductDTO(e, e.getCategories())).toList();
        return new PageImpl<>(productDTOS, productsPage.getPageable(), productsPage.getTotalElements());
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getReferenceById(id);
			if (entity == null) {
				throw new ResourceNotFoundException("Id not found " + id);
			}
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}		
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		try {
			repository.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {

		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		/*
		for (CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getReferenceById(catDto.getId());
			entity.getCategories().add(category);			
		}*/
	}	
}
