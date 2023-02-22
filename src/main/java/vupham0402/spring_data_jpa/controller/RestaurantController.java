package vupham0402.spring_data_jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vupham0402.spring_data_jpa.dto.RestaurantDTO;
import vupham0402.spring_data_jpa.dto.RestaurantDetailDTO;
import vupham0402.spring_data_jpa.service.RestaurantServiceImp;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    @Autowired
    RestaurantServiceImp restaurantService;

    @GetMapping
    public ResponseEntity<?> getRestaurant() {
        List<RestaurantDTO> responseEntities = restaurantService.getRestaurant();
        return new ResponseEntity<>(responseEntities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailRestaurant(@PathVariable("id") int id) {
        RestaurantDetailDTO detailDTO = restaurantService.getDetailRestaurant(id);

        return new ResponseEntity<>(detailDTO, HttpStatus.OK);
    }

//    @GetMapping("/clear-cache")
//    public ResponseEntity<?> clearCacheRestaurant() {
//        restaurantService.clearAllCache();
//        return new ResponseEntity<>("", HttpStatus.OK);
//    }
}
