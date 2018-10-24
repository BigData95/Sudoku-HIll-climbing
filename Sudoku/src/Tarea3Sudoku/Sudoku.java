package Tarea3Sudoku;

/*
 ===================================================================================================
 Name        : Tarea 3 Heuristicas 
 Author      : Edgar Ivan Martinez Vargas
 Version     : 1.0
 Copyright   : 
 Description : Es es la clase principal que ejecuta y resuelve el problema 2 de la tarea3 de heuristicas
 ==================================================================================================
 */
public class Sudoku {
    //Semillas utilizadas = 30
    public int[] semillas = new int[]{10, 55, 30, 150, 101, 91, 19, 44, 69, 47,
        187372311, 204110176, 12999567, 6155814, 2261281, 909, 212289, 14676, 9441, 11762,
        12345, 65432, 10101, 9009, 666, 1995, 570143, 370319, 10618, 12059};

    public static void main(String[] args) {
        Sudoku app = new Sudoku();
        app.SudokuSolver();
    }

    public void SudokuSolver() {
        //Todos  los elementos de "solver" son privados,menos la solucion()
        for (int i = 0; i < semillas.length; i++) {
            // Se le pasa "A" si quiere usar el vecindario 1 descrito en la tarea 3
            //Se le pasa "B" si se quiere usar el vecindario 2 descrito en la tarea 3
            Solver resolver = new Solver(semillas[i],"B"); 
            resolver.solucion();
        }

    }

}
