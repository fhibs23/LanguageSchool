package Services;

import Models.Basket;
import Repositories.IBasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketService {
    private IBasketRepository iBasketRepository;

    @Autowired
    public BasketService(IBasketRepository iBasketRepository){
        this.iBasketRepository=iBasketRepository;
    }
    public Basket getBasketByUserIdAndCourseId(int userId, int courseId){
        return  iBasketRepository.findByUserIdAndCourseId(userId, courseId);
    }
    public void saveBasket(Basket basket){
        iBasketRepository.save(basket);
    }
    public List<Basket> getBasketByUserId(int userId){
        return iBasketRepository.findAllByUserId(userId);
    }
    public Basket getBasketById(int id){
        return iBasketRepository.findById(id);
    }
    public void deleteBasketById(int id){
        iBasketRepository.deleteById(id);
    }
    public void deleteAllByUserId(int userId){
        iBasketRepository.deleteAllByUserId(userId);
    }


}