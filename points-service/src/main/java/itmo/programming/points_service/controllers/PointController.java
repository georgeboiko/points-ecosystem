package itmo.programming.points_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/point")
public class PointController {

    @GetMapping
    public ResponseEntity<Integer> test() {
        return ResponseEntity.ok(123);
    }
}
