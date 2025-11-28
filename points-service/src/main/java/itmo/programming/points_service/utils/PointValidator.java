package itmo.programming.points_service.utils;

import itmo.programming.points_service.dtos.requests.PointRequestDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PointValidator {

    private static final BigDecimal MIN_X = BigDecimal.valueOf(-5);
    private static final BigDecimal MAX_X = BigDecimal.valueOf(3);
    private static final BigDecimal MIN_Y = BigDecimal.valueOf(-3);
    private static final BigDecimal MAX_Y = BigDecimal.valueOf(5);
    private static final List<BigDecimal> ALLOWED_R = List.of(
            BigDecimal.valueOf(-5),
            BigDecimal.valueOf(-4),
            BigDecimal.valueOf(-3),
            BigDecimal.valueOf(-2),
            BigDecimal.valueOf(-1),
            BigDecimal.valueOf(0),
            BigDecimal.valueOf(1),
            BigDecimal.valueOf(2),
            BigDecimal.valueOf(3)
    );

    public static List<String> validate(PointRequestDTO point) {
        List<String> errors = new ArrayList<>();

        checkParameter("X", point.getX(), MIN_X, MAX_X, errors);
        checkParameter("Y", point.getY(), MIN_Y, MAX_Y, errors);
        checkParameter("R", point.getR(), ALLOWED_R, errors);

        return errors;
    }

    private static void checkParameter(String parameterName,
                                BigDecimal value, BigDecimal leftBound, BigDecimal rightBound,
                                List<String> errors) {
        if (value.compareTo(leftBound) < 0 || value.compareTo(rightBound) > 0) {
            errors.add(
                    String.format("%s value between %f and %f", parameterName, leftBound, rightBound)
            );
        }
    }

    private static void checkParameter(String parameterName,
                                BigDecimal value, List<BigDecimal> allowedValues,
                                List<String> errors) {
        if (!allowedValues.contains(value)) {
            errors.add(
                    String.format("%s value must be in %s", parameterName, allowedValues)
            );
        }
    }
}
