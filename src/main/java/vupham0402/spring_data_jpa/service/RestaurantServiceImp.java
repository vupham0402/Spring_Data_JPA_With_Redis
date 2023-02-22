package vupham0402.spring_data_jpa.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vupham0402.spring_data_jpa.dto.RestaurantDTO;
import vupham0402.spring_data_jpa.dto.RestaurantDetailDTO;
import vupham0402.spring_data_jpa.entity.FoodEntity;
import vupham0402.spring_data_jpa.entity.RestaurantEntity;
import vupham0402.spring_data_jpa.entity.RestaurantReviewEntity;
import vupham0402.spring_data_jpa.model.FoodModel;
import vupham0402.spring_data_jpa.repository.RestaurantRepository;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImp implements RestaurantService{
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private ServletContext context;

    @Override
    public List<RestaurantDTO> getRestaurant() {
        List<RestaurantDTO> dtos = new ArrayList<>();
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();

        for (RestaurantEntity data: restaurantEntities) {
            RestaurantDTO restaurantDTO = new RestaurantDTO();
            restaurantDTO.setTitle(data.getName());
            restaurantDTO.setImage("http://localhost:8080" + context.getContextPath() + "/" + data.getImage());

            float avgRate = 0;
            float sumRate = 0;
            for (RestaurantReviewEntity dataReview: data.getRestaurantReviews()) {
                sumRate += dataReview.getRate();
            }
            if (data.getRestaurantReviews().size() > 0) {
                avgRate = sumRate/data.getRestaurantReviews().size();
            }
            restaurantDTO.setAvgRate(avgRate);
            dtos.add(restaurantDTO);
        }
        return dtos;
    }

    @Override
    //@Cachable("detail_restaurant")
    public RestaurantDetailDTO getDetailRestaurant(int id) {
        String key = "res" + id;
        Gson gson = new Gson();
        RestaurantDetailDTO restaurantDetailDTO = new RestaurantDetailDTO();

        if(redisTemplate.hasKey(id)) {
            String data = (String) redisTemplate.opsForValue().get(key);
            restaurantDetailDTO = gson.fromJson(data, RestaurantDetailDTO.class);
        }
        else {
            Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(id);

            if(restaurantEntity.isPresent()) {
                restaurantDetailDTO.setTitle(restaurantEntity.get().getName());
                restaurantDetailDTO.setImage(restaurantEntity.get().getImage());
                float avgRate = 0;
                float sumRate = 0;
                for (RestaurantReviewEntity dataReview: restaurantEntity.get().getRestaurantReviews()) {
                    sumRate += dataReview.getRate();
                }
                if (restaurantEntity.get().getRestaurantReviews().size() > 0) {
                    avgRate = sumRate/restaurantEntity.get().getRestaurantReviews().size();
                }
                restaurantDetailDTO.setAvgRate(avgRate);
                List<FoodModel> foodModels = new ArrayList<>();
                for (FoodEntity foodEntity: restaurantEntity.get().getFoods()) {
                    FoodModel foodModel = new FoodModel();
                    foodModel.setId(foodEntity.getId());
                    foodModel.setName(foodEntity.getName());
                    foodModel.setImage(foodEntity.getImage());
                    foodModel.setPrice(foodEntity.getPrice());

                    foodModels.add(foodModel);
                }
                restaurantDetailDTO.setFoods(foodModels);

                String json = gson.toJson(restaurantDetailDTO);
                redisTemplate.opsForValue().set(key, json);
            }
        }
        return restaurantDetailDTO;
    }

//    @Override
//    @CacheEvict(value = "detai_restaurant", allEntries = true)
//    public void clearAllCache(){};
}
