package vupham0402.spring_data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vupham0402.spring_data_jpa.entity.RestaurantEntity;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Integer> {
}
