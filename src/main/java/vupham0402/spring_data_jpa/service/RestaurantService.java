package vupham0402.spring_data_jpa.service;

import vupham0402.spring_data_jpa.dto.RestaurantDTO;
import vupham0402.spring_data_jpa.dto.RestaurantDetailDTO;

import java.util.List;

public interface RestaurantService {
    List<RestaurantDTO> getRestaurant();
    RestaurantDetailDTO getDetailRestaurant(int id);

    //void clearAllCache();
}
