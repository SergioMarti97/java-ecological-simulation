package model.neuronal;

import java.util.Random;

/**
 * Possible functions of the neurons:
 * SIG: sigmoid
 * LIN: linear
 * SQR: square
 * SIN: sinus
 * ABS: absolute
 * REL: reluctant (0 if < 0)
 * GAU: gaussian
 * LAT: latch (memory)
 */
public class NeuronFunctions {

    public enum NF {
        EQU,
        ABS,
        REL,
        SIG,
        SQR,
        SIN
    }

    public static NeuronFunction obtainFunctions(NF nf) {
        switch (nf) {
            default: case EQU: return NeuronFunctions::equ;
            case ABS: return NeuronFunctions::abs;
            case REL: return NeuronFunctions::rel;
            case SIG: return NeuronFunctions::sig;
            case SQR: return NeuronFunctions::sqr;
            case SIN: return NeuronFunctions::sin;
        }
    }

    public static NF obtainRandomNF(Random rnd) {
        return NF.values()[rnd.nextInt(NF.values().length)];
    }

    public static NeuronFunction obtainRandomFunction(Random rnd) {
        return obtainFunctions(obtainRandomNF(rnd));
    }

    public static NeuronFunction obtainFunction(String name) {
        return obtainFunctions(NF.valueOf(name));
    }

    public static double equ(double v) {
        return v;
    }

    public static double abs(double v) {
        if (v >= 0) {
            return v;
        } else {
            return -v;
        }
    }

    public static double rel(double v) {
        if (v < 0) {
            return 0;
        } else {
            return v;
        }
    }

    public static double sig(double v) {
        return 1 / (1 + 1 / Math.pow(Math.E, v));
    }

    public static double sqr(double v) {
        return Math.sqrt(abs(v));
    }

    public static double sin(double v) {
        return Math.sin(v);
    }

}
