package itmo.programming.points_service.services;

import itmo.programming.points_service.dtos.requests.PointRequestDTO;
import itmo.programming.points_service.dtos.responses.NewInvalidPointsResponseDTO;
import itmo.programming.points_service.dtos.responses.PointsListResponseDTO;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    public PointsListResponseDTO getPoints(Integer ownerId) {
        return null;
    }

    public PointsListResponseDTO getPoints(Integer ownerId, BigDecimal radius) {
        return null;
    }

    public NewInvalidPointsResponseDTO addPoints(Integer ownerId, List<PointRequestDTO> points) {
        return null;
    }

    public void deletePoints(Integer ownerId, List<Long> pointsId) {

    }

}
