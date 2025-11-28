package itmo.programming.points_service.controllers;

import itmo.programming.points_service.dtos.requests.PointsDeleteRequestDTO;
import itmo.programming.points_service.dtos.requests.PointsListRequestDTO;
import itmo.programming.points_service.dtos.responses.NewInvalidPointsResponseDTO;
import itmo.programming.points_service.dtos.responses.PointsListResponseDTO;
import itmo.programming.points_service.services.PointService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/point")
public class PointController {

    @Autowired
    private PointService pointService;

    @GetMapping
    public ResponseEntity<PointsListResponseDTO> getPoints(
            @RequestParam(value = "r", required = false) BigDecimal radius,
            @RequestHeader("X-User-Id") Integer userId
    ){
        if (radius != null) {
            return ResponseEntity.ok(
                    pointService.getPoints(userId, radius)
            );
        } else {
            return ResponseEntity.ok(
                    pointService.getPoints(userId)
            );
        }
    }

    @PostMapping
    public ResponseEntity<NewInvalidPointsResponseDTO> addPoints(
            PointsListRequestDTO pointsListRequestDTO,
            @RequestHeader("X-User-Id") Integer userId
    ){
        return ResponseEntity.ok(
                pointService.addPoints(userId, pointsListRequestDTO.getPoints())
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePoints(
            PointsDeleteRequestDTO pointsDeleteRequestDTO,
            @RequestHeader("X-User-Id") Integer userId
    ) {
        pointService.deletePoints(userId, pointsDeleteRequestDTO.getPoints());
        return ResponseEntity.ok().build();
    }
}
