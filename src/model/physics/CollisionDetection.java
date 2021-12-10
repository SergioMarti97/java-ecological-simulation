package model.physics;

import model.entities.balls.Ball;
import model.shapes.AlineAxisRectangle;
import olcPGEApproach.vectors.points2d.Vec2d;
import olcPGEApproach.vectors.points2d.Vec2dd;
import olcPGEApproach.vectors.points2d.Vec2df;
import olcPGEApproach.vectors.points2d.Vec2di;

public class CollisionDetection {

    public static boolean doCirclesOverlap(Ball b1, Ball b2) {
        return (b1.getPos().getX() - b2.getPos().getX()) * (b1.getPos().getX() - b2.getPos().getX()) +
                (b1.getPos().getY() - b2.getPos().getY()) * (b1.getPos().getY() - b2.getPos().getY()) <=
                (b1.getR() + b2.getR()) * (b1.getR() + b2.getR());
    }

    // Is point in circle methods

    public static boolean isPointInCircle(int x, int y, Ball b) {
        return (x - b.getPos().getX()) * (x - b.getPos().getX()) +
                (y - b.getPos().getY()) * (y - b.getPos().getY()) <=
                (b.getR() * b.getR());
    }

    public static boolean isPointInCircle(float x, float y, Ball b) {
        return (x - b.getPos().getX()) * (x - b.getPos().getX()) +
                (y - b.getPos().getY()) * (y - b.getPos().getY()) <=
                (b.getR() * b.getR());
    }

    public static boolean isPointInCircle(double x, double y, Ball b) {
        return (x - b.getPos().getX()) * (x - b.getPos().getX()) +
                (y - b.getPos().getY()) * (y - b.getPos().getY()) <=
                (b.getR() * b.getR());
    }

    public static boolean isPointInCircle(Vec2di p, Ball b) {
        return isPointInCircle(p.getX(), p.getY(), b);
    }

    public static boolean isPointInCircle(Vec2df p, Ball b) {
        return isPointInCircle(p.getX(), p.getY(), b);
    }

    public static boolean isPointInCircle(Vec2dd p, Ball b) {
        return isPointInCircle(p.getX(), p.getY(), b);
    }

    public static boolean isPointInCircle(Vec2d p, Ball b) {
        if (p instanceof Vec2di) {
            return isPointInCircle((Vec2di) p, b);
        } else if (p instanceof Vec2df) {
            return isPointInCircle((Vec2df) p, b);
        } else if (p instanceof Vec2dd) {
            return isPointInCircle((Vec2dd) p, b);
        } else {
            return false;
        }
    }

    // is point in rectangle

    public static boolean isPointInRect(double x, double y, int posX, int posY, int w, int h) {
        return x >= posX && x <= (posX + w) &&
                y >= posY && y <= (posY + h);
    }

    public static boolean isPointInRect(double x, double y, float posX, float posY, float w, float h) {
        return x >= posX && x <= (posX + w) &&
                y >= posY && y <= (posY + h);
    }

    public static boolean isPointInRect(double x, double y, double posX, double posY, double w, double h) {
        return x >= posX && x <= (posX + w) &&
                y >= posY && y <= (posY + h);
    }

    public static boolean isPointInRect(double x, double y, AlineAxisRectangle rect) {
        return isPointInRect(x, y, rect.getPosX(), rect.getPosY(), rect.getWidth(), rect.getHeight());
    }

    // is point in triangle
    private static int ccw(int ax, int ay, int bx, int by, int cx, int cy) {
        return (bx - ax) * (cy - ay) - (cx - ax) * (by - ay);
    }

    private static float ccw(float ax, float ay, float bx, float by, float cx, float cy) {
        return (bx - ax) * (cy - ay) - (cx - ax) * (by - ay);
    }

    private static double ccw(double ax, double ay, double bx, double by, double cx, double cy) {
        return (bx - ax) * (cy - ay) - (cx - ax) * (by - ay);
    }

    public static boolean isPointInTriangle(double x, double y, int x1, int y1, int x2, int y2, int x3, int y3) {
        if (ccw(x1, y1, x2, y2, x3, y3) > 0) {
            // then change one vertex by other vertex
            int temp = x1;
            x1 = x2;
            x2 = temp;

            temp = y1;
            y1 = y2;
            y2 = temp;
        }
        return (x1 - x2) * (y - y1) - (y1 - y2) * (x - x1) > 0 &&
                (x2 - x3) * (y - y2) - (y2 - y3) * (x - x2) > 0 &&
                (x3 - x1) * (y - y3) - (y3 - y1) * (x - x3) > 0;
    }

    public static boolean isPointInTriangle(
            double x, double y, float x1, float y1, float x2, float y2, float x3, float y3) {
        if (ccw(x1, y1, x2, y2, x3, y3) > 0) {
            // then change one vertex by other vertex
            float temp = x1;
            x1 = x2;
            x2 = temp;

            temp = y1;
            y1 = y2;
            y2 = temp;
        }
        return (x1 - x2) * (y - y1) - (y1 - y2) * (x - x1) > 0 &&
                (x2 - x3) * (y - y2) - (y2 - y3) * (x - x2) > 0 &&
                (x3 - x1) * (y - y3) - (y3 - y1) * (x - x3) > 0;
    }

    public static boolean isPointInTriangle(
            double x, double y, double x1, double y1, double x2, double y2, double x3, double y3) {
        if (ccw(x1, y1, x2, y2, x3, y3) > 0) {
            // then change one vertex by other vertex
            double temp = x1;
            x1 = x2;
            x2 = temp;

            temp = y1;
            y1 = y2;
            y2 = temp;
        }
        return (x1 - x2) * (y - y1) - (y1 - y2) * (x - x1) > 0 &&
                (x2 - x3) * (y - y2) - (y2 - y3) * (x - x2) > 0 &&
                (x3 - x1) * (y - y3) - (y3 - y1) * (x - x3) > 0;
    }

    public static <T> boolean isPointInTriangle(double x, double y, T x1, T y1, T x2, T y2, T x3, T y3) {
        if (x1 instanceof Integer && y1 instanceof Integer &&
                x2 instanceof Integer && y2 instanceof Integer &&
                x3 instanceof Integer && y3 instanceof Integer) {
            return isPointInTriangle(x, y,
                    ((Integer) x1).intValue(), ((Integer) y1).intValue(),
                    ((Integer) x2).intValue(), ((Integer) y2).intValue(),
                    ((Integer) x3).intValue(), ((Integer) y3).intValue());
        } else if (x1 instanceof Float && y1 instanceof Float &&
                x2 instanceof Float && y2 instanceof Float &&
                x3 instanceof Float && y3 instanceof Float) {
            return isPointInTriangle(x, y,
                    ((Float)x1).floatValue(), ((Float)y1).floatValue(),
                    ((Float)x2).floatValue(), ((Float)y2).floatValue(),
                    ((Float)x3).floatValue(), ((Float) y3).floatValue());
        } else if (x1 instanceof Double && y1 instanceof Double &&
                x2 instanceof Double && y2 instanceof Double &&
                x3 instanceof Double && y3 instanceof Double) {
            return isPointInTriangle(x, y,
                    ((Double)x1).floatValue(), ((Double)y1).floatValue(),
                    ((Double)x2).floatValue(), ((Double)y2).floatValue(),
                    ((Double)x3).floatValue(), ((Double) y3).floatValue());
        } else {
            return false;
        }
    }

}
