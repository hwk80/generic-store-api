package de.tarent.challenge.store.carts;

import org.springframework.data.jpa.repository.JpaRepository;

interface CartRepository extends JpaRepository<Cart, Long> {
}
