package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.model.*;
import ru.job4j.cars.service.*;

import java.io.IOException;

@Controller
@RequestMapping("/all_posts")
@ThreadSafe
@AllArgsConstructor
public class PostController {
    private final PostService postService;
    private final EngineService engineService;
    private final BodyService bodyService;
    private final TransmissionService transmissionService;
    private final CarService carService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("all_posts", postService.findAll());
        return "all_posts/list";
    }

    @GetMapping("/posts_with_photo")
    public String getAllWithPhoto(Model model) {
        model.addAttribute("all_posts", postService.getAllWithPhoto());
        return "all_posts/list";
    }

    @GetMapping("/posts_last_day")
    public String getAllForTheLastDay(Model model) {
        model.addAttribute("all_posts", postService.getAllForTheLastDay());
        return "all_posts/list";
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "all_posts/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Post post,
                         @RequestParam MultipartFile fileDto, Model model,
                         @RequestParam("carName") String carName,
                         @RequestParam("engineName") String engineName,
                         @RequestParam("bodyName") String bodyName,
                         @RequestParam("transmissionName") String transmissionName,
                         @SessionAttribute User user) throws IOException {
        var engine = new Engine();
        engine.setName(engineName);
        engineService.save(engine);
        var body = new Body();
        body.setName(bodyName);
        bodyService.save(body);
        var transmission = new Transmission();
        transmission.setName(transmissionName);
        transmissionService.save(transmission);
        var car = new Car();
        car.setTransmission(transmission);
        car.setBody(body);
        car.setEngine(engine);
        car.setName(carName);
        var savedCar = carService.save(car);
        post.setCar(car);
        post.setUser(user);
        var savedPost = postService.create(post,
                new FileDto(fileDto.getOriginalFilename(), fileDto.getBytes()));
        if (savedCar.isEmpty() || savedPost.isEmpty()) {
            model.addAttribute("message",
                    String.format("%s%s", "Не удалось создать объявление.",
                            "Перейдите на страницу создания объявления и попробуйте снова."));
            return "errors/404";
        }
        return "redirect:/all_posts";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var postOptional = postService.findById(id);
        if (postOptional.isEmpty()) {
            model.addAttribute("message",
                    "Объявление с указанным идентификатором не найдено");
            return "errors/404";
        }
        model.addAttribute("post", postOptional.get());
        return "all_posts/one";
    }

    @PostMapping("/setSold/{id}")
    public String setTaskStatusIsSold(@PathVariable int id, Model model) {
        var updateRsl = postService.setIsSold(id);
        if (!updateRsl) {
            model.addAttribute("message",
                    "Не удалось установить статус объявлению");
            return "errors/404";
        }
        return "redirect:/all_posts";
    }

    @PostMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = postService.delete(id);
        if (!isDeleted) {
            model.addAttribute("message",
                    "Объявление с указанным идентификатором не найдено");
            return "errors/404";
        }
        return "redirect:/all_posts";
    }
}