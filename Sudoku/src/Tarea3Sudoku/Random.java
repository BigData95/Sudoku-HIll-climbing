/*
Para no tener que importaar paquetes simplemente compie la funcion Random
de las tareas pasadas
 */
package Tarea3Sudoku;

/**
 *
 * @author Ivan
 */

public class Random {

    public double[] randomNum; //Aqui se guardan "U[0,1]" numeros pseudo aleatorios 
    public int[] intRandomNum; //Aqui se guardan los numeros enterospseudo aleatorios

    private final static int a = 25173;
    private final static int c = 13849;
    private final static int m = 32768;  //2147483647
    

    private int zi;

    public Random(int semilla, int cuantos) {
        this.randomNum = new double[cuantos];
        this.intRandomNum = new int[cuantos];
        this.zi = semilla;
    }

    public Random(int semilla) {
        this.zi = semilla;
    }

    public double next() {
        zi = (a * zi + c) % m;
        return (double) zi / m;
    }

    public double nextAcotado(double desde, double hasta) {
        double eleatorio = 0;
        eleatorio = next() * (hasta - desde + 1) + desde;
        if (eleatorio > hasta) {
            eleatorio = hasta;
        }

        return eleatorio;
    }

    public int nextIntAcotado(int desde, int hasta) {
        int eleatorio = 0;

        eleatorio = (int) Math.round((next() * (hasta - desde + 1) + desde));
        if (eleatorio > hasta) {
            eleatorio = hasta;
        }
        return eleatorio;
    }

    public int nextInt() {
        zi = (a * zi + c) % m;

        return zi;

    }

    public void generar() {
        for (int i = 0; i < intRandomNum.length; i++) {
            zi = (a * zi + c) % m;
            intRandomNum[i] = zi;
        }
        for (int i = 0; i < randomNum.length; i++) {
            randomNum[i] = ((double) intRandomNum[i]) / ((double) m);
        }

    }

    public void print_random_numbers() {

        for (int i = 0; i < randomNum.length; i++) {
            System.out.println(randomNum[i]);
        }
    }

    public void print_random_numbers_acotados(int desde, int hasta) {
        for (int i = 0; i < randomNum.length; i++) {
            System.out.println(randomNum[i] * (hasta - desde + 1) + desde);
        }

    }

    public void print_int_random_numbers_acotados(int desde, int hasta) {

        int eleatorio = 0;

        for (int i = 0; i < randomNum.length; i++) {
            eleatorio = (int) Math.round((randomNum[i] * (hasta - desde + 1) + desde));
            if (eleatorio == 11) {
                System.out.println(10);
            } else {
                System.out.println(eleatorio);
            }
        }
    }

    public void print_int_random_numbers() {

        for (int i = 0; i < intRandomNum.length; i++) {
            System.out.println(intRandomNum[i]);
        }
    }

}
