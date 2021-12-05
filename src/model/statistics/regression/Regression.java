package model.statistics.regression;

import java.util.Collection;
import java.util.List;

public class Regression {

    public static int calN(Collection<Double> v1, Collection<Double> v2) {
        int size = v1.size();
        if (v1.size() != v2.size() && v1.size() < v2.size()) {
            size = v2.size();
        }
        return size;
    }

    // Función para calcular la media de los vectores. //
    public static double avg(double[] vector, int n){
        // Variables //
        int a; // variable para el bucle.
        double suma = 0; // la suma de todos los valores del vector.
        double mean = 0; // la media. Esta en ingles porque no se puede repetir la misma palabra.

        // Bucle para sumar
        for(a = 0; a < n; a++){
            suma = suma + vector[a];
        }
        mean = suma/n;

        return mean;
    }

    public static double avg(Collection<Double> v) {
        Double sum = 0.0;
        int count = 0;
        for (Double num : v) {
            sum += num;
            count++;
        }
        return sum / count;
    }

    // Función para calcular la varianza. //
    public static double var(double[] vector, int n){
        // Variables //
        int a; // Variable para el bucle.
        double mean; // Variable para la media.
        double suma_cuadrado = 0; // Variable de la suma de los cuadrados

        // Media //
        mean = avg(vector, n);

        for(a = 0; a < n; a++){
            suma_cuadrado += (vector[a] - mean)*(vector[a] - mean); // Cálculo de la suma de cuadrados. Así es más rápido.
        }

        return suma_cuadrado/(n - 1);
    }

    public static double var(Collection<Double> v) {
        double sum = 0.0;
        int count = 0;
        double mean = avg(v);
        for (Double num : v) {
            sum += (num - mean) * (num - mean);
            count++;
        }
        return sum / (count - 1);
    }

    // Función para calcular la desviación típica. //
    public static double desvest(double[] vector, int n){
        return Math.sqrt(var(vector, n));
    }

    public static double desvest(Collection<Double> v) {
        return Math.sqrt(var(v));
    }

    // Función para calcular la covarianza de los dos vectores. //
    public static double covariance(double[] vector1, double[] vector2, int n){
        // Formula de la covarianza:          //
        // Sxy = (1/n-1)*[∑(Xi*Yi) - n*X̅*Y̅] //

        // Variables //
        int a; // variable para el bucle.
        double mediaX = avg(vector1, n); // media del vector1.
        double mediaY = avg(vector2, n); // media del vector2.
        double sumatorioXY = 0; // sumatorio de Xi * Yi.
        double mediaXY = 0; // mediaX * MediaY, calculo necesario para la covarianza.
        double cov = 0; // covarianza, Sxy.

        // Bucle para calcular el sumatorio de vector1i * vector2i //
        for(a = 0; a < n; a++){
            sumatorioXY = sumatorioXY + (vector1[a] * vector2[a]);
        }

        mediaXY = mediaX * mediaY;

        cov = (sumatorioXY - n*mediaXY)/(n - 1);

        return cov;
    }

    public static double covariance(List<Double> v1, List<Double> v2, double avgX, double avgY, int size) {
        double avgXY = avgX * avgY;

        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += v1.get(i) * v2.get(i);
        }

        return (sum - size * avgXY) / (size - 1);
    }

    public static double covariance(List<Double> v1, List<Double> v2) {
        return covariance(v1, v2, avg(v1), avg(v2), calN(v1, v2));
    }

    // Función para calcular el coeficiente de correlación de Pearson.
    public static double calR(double[] vector1, double[] vector2, int n){
        // Variables //
        double valor_r; // Correlación muestral de pearson.
        double mult_desvXY; // Multiplicación de las desviaciónes típicas de X y Y

        mult_desvXY = desvest(vector1, n) * desvest(vector2, n);
        valor_r = covariance(vector1, vector2, n)/mult_desvXY;

        return valor_r;
    }

    public static double calR(double desvestX, double desvestY, double cov) {
        return cov / (desvestX * desvestY);
    }

    public static double calR(List<Double> v1, List<Double> v2) {
        return calR(desvest(v1), desvest(v2), covariance(v1, v2));
    }

    // Función para calcular el t-experimental de la dependencia de los datos. Se necesita para realizar contrastes.
    public static double tExpRegression(double r, int n){
        // Variables //
        double texp; // valor del t-experimental para el contraste.

        texp = Math.abs((r * Math.sqrt(n - 2)) / Math.sqrt(1 - r * r));

        return texp;
    }

    // Función para calcular la pendiente.
    public static double slope(double[] vector1, double[] vector2, int n){
        // Variables //
        double m; // valor de la pendiente.
        double r = calR(vector1, vector2, n); // el valor de "r".
        double Sx = desvest(vector1, n); // La desviación típica de X.
        double Sy = desvest(vector2, n); // La desviación típica de Y.

        m = r * (Sy / Sx);

        return m;
    }

    public static double slope(double r, double Sx, double Sy) {
        return r * (Sy / Sx);
    }

    public static double slope(List<Double> v1, List<Double> v2) {
        return slope(calR(v1, v2), desvest(v1), desvest(v2));
    }

    // Función para calcular la cordenada en el origen.
    public static double origin(double[] vector1, double[] vector2, int n){
        // Variables //
        double origen; // valor de la cordenada en el origen "n".
        double m = slope(vector1, vector2, n); // valor de la pendiente.
        double mediaX = avg(vector1, n); // media de X.
        double mediaY = avg(vector2, n); // media de Y.

        origen = mediaY - m * mediaX;

        return origen;
    }

    public static double origin(double avgX, double avgY, double slope) {
        return avgY - slope * avgX;
    }

    public static double origin(List<Double> v1, List<Double> v2) {
        return origin(avg(v1), avg(v2), slope(v1, v2));
    }

    // Función para calcular el error típico de estimación de la recta Y sobre X. "S^2x..y".
    public static double S2XY(double[] vector1, double[] vector2, int n){
        // Variables //
        double resultado; // el valor de este estadístico.
        double m = slope(vector1, vector2, n); // valor de la pendiente.
        double Sx = desvest(vector1, n); // desviación típica de X.
        double Sy = desvest(vector2, n); // desviación típica de X.

        resultado = ((n - 1)*(Sy * Sy - (m * m) * (Sx * Sx)))/(n - 2);

        return resultado;
    }

    public static double S2XY(double m, double Sx, double Sy, int n) {
        return ((n - 1)*(Sy * Sy - (m * m) * (Sx * Sx)))/(n - 2);
    }

    public static double S2XY(List<Double> v1, List<Double> v2) {
        double m = slope(v1, v2);
        double Sx = desvest(v1);
        double Sy = desvest(v2);
        int n = calN(v1, v2);
        return S2XY(m, Sx, Sy, n);
    }

    // Función para calcular el t-experimental de la significacia del modelo. Se necesita para realizar contrastes.
    public static double tExpModel(double[] vector1, double[] vector2, int n){
        // Variables //
        double texp; // valor del t-experimental para el contraste.
        double m = slope(vector1, vector2, n); // valor de la pendiente.
        double Sx = desvest(vector1, n); // valor de desviación típica de X.
        double error_xy = Math.sqrt(S2XY(vector1, vector2, n)); // valor del S..xy.

        texp = Math.abs((m * Sx * Math.sqrt(n - 1)) / error_xy);

        return texp;
    }

    public static double tExpModel(double m, double Sx, double errorXY, int n) {
        return Math.abs((m * Sx * Math.sqrt(n - 1)) / errorXY);
    }

    public static double tExpModel(List<Double> v1, List<Double> v2) {
        double m = slope(v1, v2);
        double Sx = desvest(v1);
        double errorXY = Math.sqrt(S2XY(v1, v2));
        int n = calN(v1, v2);
        return tExpModel(m, Sx, errorXY, n);
    }

    // Función para calcular el error para un intervalo de predicción.
    public static double predictionIntervalError (double[] vector1, double[] vector2, int n, double x0){
        // Formula del error:                 //
        // error = t(n-2; alfa/2) * Sy..x * √(1 + 1/n + (x0 - X̅)^2/(n - 1)*S2x) //

        // Variables //
        double error; // el error del intervalo. Se requiere de un valor de probabilidad de la tabla t-student.
        double mediaX = avg(vector1, n); // media de X.
        double Sx2 = var(vector1, n); // varianza de X.
        double S_xy = Math.sqrt(S2XY(vector1, vector2, n)); // el valor estadístico S..xy.
        double raiz = 0; // el calculo de toda la parte de la raíz de la formula.

        raiz = Math.sqrt(1 + ((x0 - mediaX)*(x0 - mediaX))/((n - 1) * Sx2 * n));

        // valor del t-teorico de la tabla t-student para n-2 grados de libertad y alfa de 0,025 //
        // 2.3646 //

        error = 2.3646 * S_xy * raiz;
        return error;
    }

    public static double predictionIntervalError(double avgX, double sx2, double sXY, double x0, int n) {
        double sqrt = Math.sqrt(1 + ((x0 - avgX)*(x0 - avgX))/((n - 1) * sx2 * n));
        final double val = 2.3646;
        return val * sXY * sqrt;
    }

    public static double predictionIntervalError(List<Double> v1, List<Double> v2, double x0) {
        double sXY = Math.sqrt(S2XY(v1, v2));
        return predictionIntervalError(avg(v1), var(v1), sXY, x0, calN(v1, v2));
    }

}
