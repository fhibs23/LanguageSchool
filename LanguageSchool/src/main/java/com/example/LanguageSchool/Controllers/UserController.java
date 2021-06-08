package com.example.LanguageSchool.Controllers;
import com.example.LanguageSchool.Models.Basket;
import com.example.LanguageSchool.Models.Course;
import com.example.LanguageSchool.Models.User;
import com.example.LanguageSchool.Services.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/")
@Getter
@Setter
public class UserController {
    private final TypeService typeService;
    private final CourseService CourseService;
    private final UserService userService;
    private final EmailService emailService;
    private final BasketService basketService;

    private String addBasketStatus = "";

    public UserController(TypeService typeService, CourseService CourseService,  UserService userService, EmailService emailService, BasketService basketService) {
        this.typeService = typeService;
        this.CourseService = CourseService;
        this.userService = userService;
        this.emailService = emailService;
        this.basketService = basketService;
    }
    @GetMapping
    public String index(@RequestParam(name = "typeId", required = false) Integer typeId,
                        Model model, Authentication authentication){
        String userRole = getUserRole(authentication);
        if (typeId == null) typeId = 0;
        model.addAttribute("userRole", userRole);
        model.addAttribute("courses", CourseService.getAllCourses());
        model.addAttribute("types", typeService.getAllTypes());
        model.addAttribute("typeId", typeId);
        return "templates/html/index";
    }
    @GetMapping("/courses")
    public String courses(@RequestParam(name = "typeId", required = false) Integer typeId,
                           Model model, Authentication authentication){
        String userRole = getUserRole(authentication);
        if (typeId == null) typeId = 0;
        model.addAttribute("userRole", userRole);
        model.addAttribute("courses", CourseService.getAllCoursesByTypeId(typeId));
        model.addAttribute("types", typeService.getAllTypes());
        model.addAttribute("typeId", typeId);
        return "UserController/courses";
    }
    @GetMapping("/course")
    public  String course(@RequestParam(name = "courseId",required = false) Integer courseId,
                           @RequestParam(name = "typeId", required = false) Integer typeId,
                           Model model, Authentication authentication){
        String userRole = getUserRole(authentication);
        if (courseId == null) courseId = 0;
        if (typeId == null) typeId = 0;
        model.addAttribute("userRole", userRole);
        model.addAttribute("courses", CourseService.getAllCoursesByTypeId(typeId));
        model.addAttribute("types", typeService.getAllTypes());
        model.addAttribute("typeId", typeId);
        Course course = CourseService.getCourseById(courseId);
        model.addAttribute("course", course);
        model.addAttribute("Status", addBasketStatus);
        reloadAddBasketStatus();
        return "UserController/course";
    }

    private String getUserRole(Authentication authentication) {
        if (authentication == null)
            return "GUEST";
        else
            return ((User)userService.loadUserByUsername(authentication.getName())).getRole();
    }
    private int getUserId(Authentication authentication) {
        if (authentication == null)
            return 0;
        else
            return ((User)userService.loadUserByUsername(authentication.getName())).getId();
    }
    @GetMapping("/sign")
    public String sign() {
        return "UserController/registration";
    }
    @PostMapping("/sign")
    public String signCreate(HttpServletRequest request,
                             @RequestParam(name = "email") String email,
                             @RequestParam(name = "username") String username,
                             @RequestParam(name = "password") String password,
                             Model model) {
        if (userService.loadUserByUsername(username) != null) {
            model.addAttribute("Status", "user_exists");
            return "UserController/registration";
        }
        else {
            userService.create(email,username,password);
            authWithHttpServletRequest(request, username, password);
            return "redirect:/";
        }
    }
    public void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) { }
    }
    private void reloadAddBasketStatus() {
        addBasketStatus = "";
    }
    private void setAddBasketStatus(String status) {
        addBasketStatus = status;
    }
    private int getTotalPrice(List<Basket> baskets){
        int res=0;
        for(Basket basket:baskets){
            res+=CourseService.getCourseById(basket.getCourseId()).getPrice() * basket.getCourseCount();
        }
        return res;
    }
    private String createMessageForUser(List<Basket> userBaskets){
        List<Course> userCourses = new ArrayList<>();
        for (Basket basket:userBaskets){
            userCourses.add(CourseService.getCourseById(basket.getCourseId()));
        }
        String res = "Здравствуйте, ваш заказ:<br>";
        for (int i = 0; i < userCourses.size(); i++) {
            res += (i + 1) + ") " + userCourses.get(i).getName() + " (Количество: " + userBaskets.get(i).getCourseCount() + ", Стоимость: " + (userCourses.get(i).getPrice()*userBaskets.get(i).getCourseCount()) + " р.)<br>";
        }
        res += "Конечная стоимость: " + getTotalPrice(userBaskets) + " р.<br>";
        return res;
    }
    @PostMapping("/course")
    public String addToBasket(Authentication authentication,
                              @RequestParam(name="courseId", required = false) Integer courseId,
                              @RequestParam(name = "courseCount") int courseCount){

        String userRole = getUserRole(authentication);
        String redirectString = "redirect:/course?courseId=" + courseId;
        if (userRole == "GUEST") {
            setAddBasketStatus("user_guest");
            return redirectString;
        }
        else {
            int userId = getUserId(authentication);
            Basket basket = basketService.getBasketByUserIdAndCoursesId(userId, courseId);
            if (basket == null) {
                Basket newBasket = new Basket();
                newBasket.setUserId(userId);
                newBasket.setCourseId(courseId);
                newBasket.setCourseCount(courseCount);
                basketService.saveBasket(newBasket);
                setAddBasketStatus("Ok");
                return redirectString;
            }
            else{
                if (basket.getCourseCount() + courseCount > 100){
                    setAddBasketStatus("count_overflow");
                    return redirectString;
                }
                else {
                    basket.setCourseCount(basket.getCourseCount()+courseCount);
                    basketService.saveBasket(basket);
                    setAddBasketStatus("Ok");
                    return redirectString;
                }
            }
        }
    }
    @GetMapping("/basket")
    public String basket(Model model, Authentication authentication){
        String userRole = getUserRole(authentication);
        int userId = getUserId(authentication);
        model.addAttribute("totalPrice", getTotalPrice(basketService.getBasketByUserId(userId)));
        model.addAttribute("userRole", userRole);
        model.addAttribute("types", typeService.getAllTypes());
        List<Basket> baskets = basketService.getBasketByUserId(userId);
        Collections.sort(baskets, Comparator.comparing(Basket::getId));
        model.addAttribute("basket", baskets);
        model.addAttribute("CourseService", CourseService);

        return "UserController/basket";
    }
    @PostMapping(value = "/basketOperation", params = "delete")
    public String deleteBasket(@RequestParam(name = "basketToDeleteId") int basketToDeleteId) {
        basketService.deleteBasketById(basketToDeleteId);
        return "redirect:/basket";
    }
    @SneakyThrows
    @PostMapping(value = "/sendBasket")
    public String sendBasket(Authentication authentication,
                             @RequestParam(name = "address") String address,
                             @RequestParam(name = "telephone") String telephone) {
        User user = (User)userService.loadUserByUsername(authentication.getName());
        String userMessage = createMessageForUser(basketService.getBasketByUserId(user.getId()));
        emailService.sendmail(userMessage, user.getEmail());
        basketService.deleteAllByUserId(user.getId());
        return "redirect:/basket";
    }
}
