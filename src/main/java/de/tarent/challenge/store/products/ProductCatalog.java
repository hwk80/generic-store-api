package de.tarent.challenge.store.products;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProductCatalog extends JpaRepository<Product, Long> {

    Product findBySku(String sku);

}
