package itmo.programming.points_service.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class PointCalculator {
    public static boolean check(BigDecimal x, BigDecimal y, BigDecimal r) {
        if (r.equals(BigDecimal.ZERO)) {
            return false;
        }

        MathContext mc = new MathContext(20, RoundingMode.HALF_UP);
        BigDecimal scaleFactor = r.divide(BigDecimal.valueOf(7), mc).abs();
        BigDecimal localX = x.divide(scaleFactor, mc);
        BigDecimal localY = y.divide(scaleFactor, mc);

        return isInBatmanFigure(localX, localY, mc);
    }

    private static boolean isInBatmanFigure(BigDecimal localX, BigDecimal localY, MathContext mc) {
        BigDecimal absX = localX.abs(mc);

        // ВАЖНО: Проверяем границы по X (как на фронтенде)
        if (absX.compareTo(BigDecimal.valueOf(7)) > 0) {
            return false;
        }

        BigDecimal upperBound = calculateUpperBound(absX, mc);
        if (localY.compareTo(upperBound) > 0) {
            return false;
        }

        BigDecimal lowerBound = calculateLowerBound(localX, absX, mc);
        if (localY.compareTo(lowerBound) < 0) {
            return false;
        }

        return true;
    }

    private static BigDecimal calculateUpperBound(BigDecimal absX, MathContext mc) {
        if (absX.compareTo(BigDecimal.valueOf(3)) >= 0 && absX.compareTo(BigDecimal.valueOf(7)) <= 0) {
            BigDecimal term = BigDecimal.ONE.subtract(
                    absX.divide(BigDecimal.valueOf(7), mc).pow(2, mc), mc
            );
            return sqrt(term, mc).multiply(BigDecimal.valueOf(3), mc);
        }
        else if (absX.compareTo(BigDecimal.valueOf(1.5)) >= 0 && absX.compareTo(BigDecimal.valueOf(3)) < 0) {
            BigDecimal dx = absX.subtract(BigDecimal.valueOf(2.25));
            BigDecimal term = BigDecimal.ONE.subtract(
                    dx.pow(2, mc).divide(BigDecimal.valueOf(0.5625), mc), mc
            );
            return BigDecimal.valueOf(2.0).subtract(sqrt(term.max(BigDecimal.ZERO), mc), mc);
        }
        else if (absX.compareTo(BigDecimal.valueOf(0.6)) >= 0 && absX.compareTo(BigDecimal.valueOf(1.5)) < 0) {
            if (absX.compareTo(BigDecimal.valueOf(0.9)) <= 0) {
                BigDecimal dx = absX.subtract(BigDecimal.valueOf(0.75));
                BigDecimal term = BigDecimal.ONE.subtract(
                        dx.pow(2, mc).divide(BigDecimal.valueOf(0.0225), mc), mc
                );
                return BigDecimal.valueOf(3).add(
                        BigDecimal.valueOf(0.4).multiply(sqrt(term.max(BigDecimal.ZERO), mc), mc), mc
                );
            } else {
                return BigDecimal.valueOf(2.0).add(
                        BigDecimal.valueOf(1.5).subtract(absX, mc).multiply(BigDecimal.valueOf(0.3), mc
                        ), mc);
            }
        }
        else {
            return BigDecimal.valueOf(2.8);
        }
    }

    private static BigDecimal calculateLowerBound(BigDecimal localX, BigDecimal absX, MathContext mc) {
        if (absX.compareTo(BigDecimal.valueOf(4)) >= 0 && absX.compareTo(BigDecimal.valueOf(7)) <= 0) {
            BigDecimal term = BigDecimal.ONE.subtract(
                    localX.divide(BigDecimal.valueOf(7), mc).pow(2, mc), mc
            );
            return sqrt(term, mc).multiply(BigDecimal.valueOf(-3), mc);
        }
        else {
            BigDecimal term1 = absX.divide(BigDecimal.valueOf(2), mc);

            BigDecimal constant = BigDecimal.valueOf(3)
                    .multiply(sqrt(BigDecimal.valueOf(33), mc), mc)
                    .subtract(BigDecimal.valueOf(7))
                    .divide(BigDecimal.valueOf(112), mc);

            BigDecimal term2 = constant.multiply(localX.pow(2, mc), mc);
            BigDecimal term3 = BigDecimal.valueOf(-3);

            BigDecimal innerTerm = absX.subtract(BigDecimal.valueOf(2)).abs(mc)
                    .subtract(BigDecimal.ONE).abs(mc).pow(2, mc);
            BigDecimal term4 = sqrt(BigDecimal.ONE.subtract(innerTerm, mc).max(BigDecimal.ZERO), mc);

            return term1.subtract(term2).add(term3).add(term4);
        }
    }

    private static BigDecimal sqrt(BigDecimal value, MathContext mc) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()), mc);
        for (int i = 0; i < 10; i++) {
            x = x.add(value.divide(x, mc)).divide(BigDecimal.valueOf(2), mc);
        }
        return x;
    }
}