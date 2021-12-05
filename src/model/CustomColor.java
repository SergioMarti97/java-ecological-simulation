package model;

import java.util.Random;

public class CustomColor {

    private int r;

    private int g;

    private int b;

    private int hex;

    public CustomColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        hex = 255 << 24 | r << 16 | g << 8 | b;
    }

    public CustomColor(int hex) {
        this.hex = hex;
        this.r = hex >> 16 & 255;
        this.g = hex >> 8 & 255;
        this.b = hex & 255;
    }

    public static int makeRandom() {
        Random rnd = new Random();
        int r = rnd.nextInt(255);
        int g = rnd.nextInt(255);
        int b = rnd.nextInt(255);
        return 255 << 24 | r << 16 | g << 8 | b;
    }

    public static int modify(int r, int g, int b, int bound) {
        Random rnd = new Random();
        int halfBound = bound / 2;
        r += (rnd.nextInt(2 * bound) - halfBound);
        g += (rnd.nextInt(2 * bound) - halfBound);
        b += (rnd.nextInt(2 * bound) - halfBound);
        r = Math.max(Math.min(255, r), 0);
        g = Math.max(Math.min(255, g), 0);
        b = Math.max(Math.min(255, b), 0);
        return 255 << 24 | r << 16 | g << 8 | b;
    }

    public static int modify(int c, int bound) {
        Random rnd = new Random();
        int halfBound = bound / 2;
        int r = (c >> 16 & 255) + (rnd.nextInt(2 * bound) - halfBound);
        int g = (c >> 8 & 255) + (rnd.nextInt(2 * bound) - halfBound);
        int b = (c & 255) + (rnd.nextInt(2 * bound) - halfBound);
        r = Math.max(Math.min(255, r), 0);
        g = Math.max(Math.min(255, g), 0);
        b = Math.max(Math.min(255, b), 0);
        return 255 << 24 | r << 16 | g << 8 | b;
    }

    public static int modify(CustomColor c, int bound) {
        Random rnd = new Random();
        int halfBound = bound / 2;
        int r = c.getR() + (rnd.nextInt(2 * bound) - halfBound);
        int g = c.getG() + (rnd.nextInt(2 * bound) - halfBound);
        int b = c.getB() + (rnd.nextInt(2 * bound) - halfBound);
        r = Math.max(Math.min(255, r), 0);
        g = Math.max(Math.min(255, g), 0);
        b = Math.max(Math.min(255, b), 0);
        return 255 << 24 | r << 16 | g << 8 | b;
    }

    public static int modifyPercentage(int r, int g, int b, float percentage) {
        Random rnd = new Random();
        r += rnd.nextInt((int)(2 * percentage * r)) - percentage * r;
        g += rnd.nextInt((int)(2 * percentage * g)) - percentage * g;
        b += rnd.nextInt((int)(2 * percentage * b)) - percentage * b;

        r = Math.max(Math.min(255, r), 0);
        g = Math.max(Math.min(255, g), 0);
        b = Math.max(Math.min(255, b), 0);
        return 255 << 24 | r << 16 | g << 8 | b;
    }

    public static int modifyPercentage(int c, float percentage) {
        Random rnd = new Random();
        int r = (c >> 16 & 255);
        r += rnd.nextInt((int)(2 * percentage * r)) - percentage * r;
        int g = (c >> 8 & 255);
        g += rnd.nextInt((int)(2 * percentage * g)) - percentage * g;
        int b = (c & 255);
        b += rnd.nextInt((int)(2 * percentage * b)) - percentage * b;

        r = Math.max(Math.min(255, r), 0);
        g = Math.max(Math.min(255, g), 0);
        b = Math.max(Math.min(255, b), 0);
        return 255 << 24 | r << 16 | g << 8 | b;
    }

    public static int modifyPercentage(CustomColor c, float percentage) {
        Random rnd = new Random();
        int r = c.getR();
        r += rnd.nextInt((int)(2 * percentage * r)) - percentage * r;
        int g = c.getG();
        g += rnd.nextInt((int)(2 * percentage * g)) - percentage * g;
        int b = c.getB();
        b += rnd.nextInt((int)(2 * percentage * b)) - percentage * b;

        r = Math.max(Math.min(255, r), 0);
        g = Math.max(Math.min(255, g), 0);
        b = Math.max(Math.min(255, b), 0);
        return 255 << 24 | r << 16 | g << 8 | b;
    }

    public CustomColor modify(int bound) {
        return new CustomColor(CustomColor.modify(hex, bound));
    }

    public CustomColor modify(float percentage) {
        return new CustomColor(CustomColor.modifyPercentage(hex, percentage));
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
        hex = 255 << 24 | r << 16 | g << 8 | b;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
        hex = 255 << 24 | r << 16 | g << 8 | b;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
        hex = 255 << 24 | r << 16 | g << 8 | b;
    }

    public int getHex() {
        return hex;
    }

    public void setHex(int hex) {
        this.hex = hex;
    }

}
