package com.LoginPage.login.CarFetch;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/db-cars")
@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true")
public class CarDbController {

    private final CarRepo repo;

    public CarDbController(CarRepo repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Car> getAllCars() {
        return repo.findAll();
    }

    @GetMapping("/search")
    public List<Car> search(@RequestParam("q") String query) {

        String trimmed = query.trim();
        String brand = trimmed;
        String model = null;

        String[] parts = trimmed.split(" ", 2);
        if (parts.length >= 1) {
            brand = parts[0];
        }
        if (parts.length == 2) {
            model = parts[1];
        }

        String finalBrand = brand.toLowerCase();
        String finalModel = model == null ? null : model.toLowerCase();

        return repo.findAll().stream()
                .filter(c -> c.getBrand() != null &&
                        c.getBrand().toLowerCase().contains(finalBrand))
                .filter(c -> {
                    if (finalModel == null) return true;
                    return c.getModel() != null &&
                            c.getModel().toLowerCase().contains(finalModel);
                })
                .toList();
    }
}
