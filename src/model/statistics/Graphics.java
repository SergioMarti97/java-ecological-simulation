package model.statistics;

import javafx.util.Pair;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Graphics {

    public static void drawGraphic(
            Renderer r,
            int offX,
            int offY,
            int w,
            int h,
            double[] values,
            double max,
            double min
    ) {
        r.drawFillRectangle(offX, offY, w, h, HexColors.WHITE);
        r.drawText(String.format("%.2f", max), offX, offY, HexColors.BLACK);
        r.drawText(String.format("%.2f", min), offX, h + offY, HexColors.BLACK);

        double interval = max - min;
        int step = (w / values.length);
        double error = (w / (float)values.length) - step;
        int index = 0;
        int beforeX = offX;
        int beforeY = (int)(h * (1 - ((values[index] - min) / interval)) + offY);

        r.setPixel(beforeX, beforeY, HexColors.RED);

        double errorInc = error;
        do {
            int afterX = beforeX + step;
            if (errorInc >= 1) {
                afterX += (int)errorInc;
                errorInc -= (int)errorInc;
            }
            int afterY = (int)(h * (1 - ((values[index] - min) / interval)) + offY);

            r.drawLine(beforeX, beforeY, afterX, afterY, HexColors.RED);
            r.drawCircle(afterX, afterY, 3, HexColors.RED);

            beforeX = afterX;
            beforeY = afterY;
            index++;
            errorInc += error;
        } while (index < values.length);
    }

    private static double getMax(List<Double> values) {
        Optional<Double> optMax = values.stream().max(Double::compare);
        return optMax.isPresent() ? optMax.get() : 0;
    }

    private static double getMin(List<Double> values) {
        Optional<Double> optMin = values.stream().min(Double::compare);
        return optMin.isPresent() ? optMin.get() : 0;
    }

    private static double transform(double val, double max, double min) {
        if (max - min != 0) {
            return (val - min) / (max - min);
        } else {
            return 1;
        }
    }

    public static void drawGraph(
            Renderer r,
            Collection<Pair<Double, Double>> values,
            double slope,
            double origin,
            int offX, int offY, int w, int h, boolean drawBackground, int colBackground, int colValues, int colRegression) {
        ArrayList<Double> xValues = new ArrayList<>();
        ArrayList<Double> yValues = new ArrayList<>();
        for (Pair<Double, Double> p : values) {
            xValues.add(p.getKey());
            yValues.add(p.getValue());
        }
        double maxX = getMax(xValues);
        double minX = getMin(xValues);
        double maxY = getMax(yValues);
        double minY = getMin(yValues);

        final int paddingT = 10; // padding top
        final int paddingB = 10; // padding bottom
        final int paddingL = 10; // padding left
        final int paddingR = 10; // padding right

        int realW = w - paddingL - paddingR;
        int realH = h - paddingT - paddingB;

        // draw background
        if (drawBackground) {
            r.drawFillRectangle(offX, offY, w, h, colBackground);
            // draw axis:
            final int axisExtra = 3;
            r.drawLine(
                    offX + paddingL - axisExtra,
                    offY + paddingT + realH,
                    offX + paddingL + realW,
                    offY + paddingT + realH,
                    HexColors.BLACK);
            r.drawLine(
                    offX + paddingL,
                    offY + paddingT,
                    offX + paddingL,
                    offY + paddingT + realH + axisExtra,
                    HexColors.BLACK);
        }
        // draw points:
        if (values.size() > 2) {
            for (int i = 0; i < values.size() - 1; i++) {
                double x1 = realW * transform(xValues.get(i), maxX, minX) + paddingL + offX;
                double y1 = realH * (1 - transform(yValues.get(i), maxY, minY)) + paddingT + offY;

                double estimation1 = realH * (1 - transform(slope * xValues.get(i) + origin, maxY, minY)) + paddingT + offY;

                double x2 = realW * transform(xValues.get(i + 1), maxX, minX) + paddingL + offX;
                double y2 = realH * (1 - transform(yValues.get(i + 1), maxY, minY)) + paddingT + offY;

                double estimation2 = realH * (1 - transform(slope * xValues.get(i + 1) + origin, maxY, minY)) + paddingT + offY;

                r.drawLine((int) x1, (int) y1, (int) x2, (int) y2, colValues);
                r.drawLine((int) x1, (int) estimation1, (int) x2, (int) estimation2, colRegression);
            }
        }
    }

}
