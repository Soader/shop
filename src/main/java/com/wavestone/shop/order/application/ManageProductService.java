package com.wavestone.shop.order.application;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wavestone.shop.domain.Product;
import com.wavestone.shop.domain.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManageProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	@Transactional
	public Long storeProduct(ProductDto product) {
		Product entity = Optional.ofNullable(product.id())
				.map(id -> productRepository.findById(id)
						.orElseThrow(() -> new EntityNotFoundException("Product not found")))
				.orElseGet(Product::new);

		productMapper.toEntity(entity, product);
		productRepository.save(entity);

		log.info("Saved product {}", entity.getId());
		return entity.getId();
	}

}
