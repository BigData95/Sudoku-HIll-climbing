/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tarea3Sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*
El sudoku se divide en 9 secciones
De izquierda a derecha. De arriba hacia abajo
        La primera va de (0,0) a (2,2)
        La segunda va de (0,3) a (2,5)
        La tercera va de (0,6) a (2,8)
        
        La Cuarta va de  (3,0) a (5,2)
        La quinta va de  (3,3) a (5,5)
        La sexta va de   (3,6) a (5,8)
        
        La septima va de (6,0) a (8,2)
        La octava va de  (6,3) a (8,3)
        La novena va de  (6,6) a (6,8)

En base a estas coordenadas se crearan y asi se muestran en el codigo.



Comente y descomente los tableros que quiere probar Divididos por dificultad
        
 */
public class Solver {

    private String vecindario;

    private Random rd;

    public Solver(int semilla, String vecindario) {
        rd = new Random(semilla);
        this.vecindario = vecindario;
    }

    private final int condicionParo = 100000; //Este numero parace ideal. Da buenos resultados en poco tiempo(6 sec por las 30 semillas)
    //100000
    //Medio Mejor solucion encontrada con costo = 0
//    private int[][] board = { //Contiene las condiciones iniciales del Sudoku
//        {0, 0, 0, 0, 0, 0, 0, 0, 0},
//        {0, 7, 9, 0, 5, 0, 1, 8, 0},
//        {8, 0, 0, 0, 0, 0, 0, 0, 7},
//        {0, 0, 7, 3, 0, 6, 8, 0, 0},
//        {4, 5, 0, 7, 0, 8, 0, 9, 6},
//        {0, 0, 3, 5, 0, 2, 7, 0, 0},
//        {7, 0, 0, 0, 0, 0, 0, 0, 5},
//        {0, 1, 6, 0, 3, 0, 4, 2, 0},
//        {0, 0, 0, 0, 0, 0, 0, 0, 0}
//    };

    //Dificil  Mejor solucion encontrada con costo = 2   s12a.txt 
//    private int[][] board = { //Contiene las condiciones iniciales del Sudoku
//        {0, 0, 0, 0, 0, 3, 0, 1, 7},
//        {0, 1, 5, 0, 0, 9, 0, 0, 8},
//        {0, 6, 0, 0, 0, 0, 0, 0, 0},
//        {1, 0, 0, 0, 0, 7, 0, 0, 0},
//        {0, 0, 9, 0, 0, 0, 2, 0, 0},
//        {0, 0, 0, 5, 0, 0, 0, 0, 4},
//        {0, 0, 0, 0, 0, 0, 0, 2, 0},
//        {5, 0, 0, 6, 0, 0, 3, 4, 0},
//        {3, 4, 0, 2, 0, 0, 0, 0, 0}
//    };
/// Facil.  Mejor solucion encontrada con costo = 0 s01a.txt 
    private int[][] board = { //Contiene las condiciones iniciales del Sudoku
        {0, 4, 0, 0, 0, 0, 1, 7, 9},
        {0, 0, 2, 0, 0, 8, 0, 5, 4},
        {0, 0, 6, 0, 0, 5, 0, 0, 8},
        {0, 8, 0, 0, 7, 0, 9, 1, 0},
        {0, 5, 0, 0, 9, 0, 0, 3, 0},
        {0, 1, 9, 0, 6, 0, 0, 4, 0},
        {3, 0, 0, 4, 0, 0, 7, 0, 0},
        {5, 7, 0, 1, 0, 0, 2, 0, 0},
        {9, 2, 8, 0, 0, 0, 0, 6, 0}
    };

    private int[][] fijos = new int[9][9]; //Guarda los numeros fijos del sudoku
    private int[][] mejorSolucion = new int[9][9];
    private static final int BOARD_SIZE = 9;
    private static final int SUBSECTION_SIZE = 3;

    private int[][] solucionInicial(int[][] solucion) {
        int[][] aux = solucion;
        //Hago un arreglo con todas las subsecciones y lo revuelvo con el al algorimo Durstenfeld (Durstenfeld shuffle)
        int[] subSeccion = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        subSeccion = shuffleArray(subSeccion);

        //Escoge subSecciones. Al final tiene que cubrir todas.
        //Aun asi para evitar problemas se hizo la seleccion de subsecciones lo mas aleatorio posible
        for (int i = 0; i < subSeccion.length; i++) {
            switch (subSeccion[i]) {
                case 1:
                    subSeccionInicial(aux, 0, 0);
                    break;
                case 2:
                    subSeccionInicial(aux, 0, 3);
                    break;
                case 3:
                    subSeccionInicial(aux, 0, 6);
                    break;
                case 4:
                    subSeccionInicial(aux, 3, 0);
                    break;
                case 5:
                    subSeccionInicial(aux, 3, 3);
                    break;
                case 6:
                    subSeccionInicial(aux, 3, 6);
                    break;
                case 7:
                    subSeccionInicial(aux, 6, 0);
                    break;
                case 8:
                    subSeccionInicial(aux, 6, 3);
                    break;
                case 9:
                    subSeccionInicial(aux, 6, 6);
                    break;
            }
        }
        return aux;
    }

    private void subSeccionInicial(int[][] board, int row, int column) {
        //Corta la matriz en subsecciones y luego las llena cno soluciones posibles.
        int[][] aux;
        aux = Arrays.copyOf(fijos, 9);

        int[] soluciones = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        soluciones = shuffleArray(soluciones); //Se modifica el orden

        int subSeccionRowIni = (row / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subSeccionRowFin = subSeccionRowIni + SUBSECTION_SIZE;

        int subseccionColumnIni = (column / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subseccionColumnFin = subseccionColumnIni + SUBSECTION_SIZE;

        int contador = 0; //El contador es para moverse a traves del nuevo arreglo si los numeros fijos

        soluciones = eliminarFijos(soluciones, row, column); //Elimina los numeros fijos en la seccion para solo permitir numeros que no se repitn
        for (int r = subSeccionRowIni; r < subSeccionRowFin; r++) {
            for (int c = subseccionColumnIni; c < subseccionColumnFin; c++) {
                if (aux[r][c] == 0) { //Estos no son fijos se pueden modificar
                    board[r][c] = soluciones[contador];
                    contador++;
                }
            }
        }
    }

    ////////////////////////Parte Principal del algotirmo////////////////////////////////
    public void solucion() {
        fijos();// Se crea la matriz de numeros fijos para ayuda en el proceso/
        int mejorCosto = 0;
        int costoActual = 0;   //Costo Actual es el ultimo costo admitid
        int costoNuevo = 0;    //Costo nuevo es el ultimo costo generado
        int[][] mejorSol = null;
        int[][] solActual = null; //Es la ultima solucion adimitida
        int[][] solNueva = null;  //Es la ultima solucion generada  
        int[][] solAux = null;
        boolean bandera = true; //Nos dira si ya se encontro la mejor solucion con costo = 0

        //printBoard(fijos);
        int criterioParo = 0;
        mejorSol = solucionInicial(board.clone());
        //printBoard(mejorSol);
        mejorCosto = costoTotal(mejorSol);
        costoActual = mejorCosto;
        //System.out.println("Costo Inicial " + mejorCosto);
        solActual = copiarMatriz(mejorSol);

        //Se repite hasta que encuentra el mejor o cumple la condicion de paro
        while ((criterioParo != condicionParo && bandera == true)) {
            solAux = copiarMatriz(solActual);

            solNueva = vecindario(solAux, vecindario); //Se genera una nueva solucion
            costoNuevo = costoTotal(solNueva); //Se calcula el costo de la nueva solucion

            if (costoNuevo <= mejorCosto) { //Se ve si el nuevo costo es mejor
                // System.out.println("Es mejor!!!!!!!!!!!!!!!!!");
                mejorCosto = costoNuevo;
                costoActual = costoNuevo;
                mejorSol = copiarMatriz(solNueva);
                solActual = copiarMatriz(solNueva);
            } else {  //No es mejor se regresa a la ultima solucion aceptada
                solNueva = copiarMatriz(solActual);
                costoNuevo = costoActual;
                criterioParo++;
            }
            if (mejorCosto == 0) {//En caso de que encuentre el mejor
                bandera = false;
                printBoard(mejorSol);
            }

        }
        //System.out.println("LA MEJOR SOLUCION ES::");
        //printBoard(mejorSol);
        System.out.println("Mejor costo: " + mejorCosto + "\n\n");

    }

    //Dentro de vecindario() pasa lo siguiente
    // 1- Se escoge una subseccion al azar.
    // 2- Se crea una subseccion por subSeccionVecindarios();
    // 3- Se hace la permutacion de a subseccion segun el tipo de vecindad (A o B) descrita por shuffleMatrix();
    // 4- Regresa la matriz con la subseccion ya modificada
    private int[][] vecindario(int[][] solucion, String vecindario) {
        int[][] aux = solucion;
        int sub = rd.nextIntAcotado(1, 9);
        // System.out.println("Se modifica " + sub);
        switch (sub) {
            case 1:
                subSeccionVecindarios(aux, 0, 0, vecindario);
                break;
            case 2:
                subSeccionVecindarios(aux, 0, 3, vecindario);
                break;
            case 3:
                subSeccionVecindarios(aux, 0, 6, vecindario);
                break;
            case 4:
                subSeccionVecindarios(aux, 3, 0, vecindario);
                break;
            case 5:
                subSeccionVecindarios(aux, 3, 3, vecindario);
                break;
            case 6:
                subSeccionVecindarios(aux, 3, 6, vecindario);
                break;
            case 7:
                subSeccionVecindarios(aux, 6, 0, vecindario);
                break;
            case 8:
                subSeccionVecindarios(aux, 6, 3, vecindario);
                break;
            case 9:
                subSeccionVecindarios(aux, 6, 6, vecindario);
                break;
        }
        return aux;
    }

    private void subSeccionVecindarios(int[][] sol, int row, int column, String vecindario) {
        //Crea una subseecion de la tabla sodoku original y de la tabla de numeros fijos para referencia
        int[][] board = sol;
        int[][] partidasFijas = new int[3][3];
        int subSeccionRowIni = (row / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subSeccionRowFin = subSeccionRowIni + SUBSECTION_SIZE;

        int subseccionColumnIni = (column / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subseccionColumnFin = subseccionColumnIni + SUBSECTION_SIZE;

        int[][] nueva = new int[3][3];
        int rowNueva = 0;
        int columnNueva = 0;
        /////////////////////Aca se crea la subseccion//////////////////
        for (int r = subSeccionRowIni; r < subSeccionRowFin; r++) {
            columnNueva = 0;
            for (int c = subseccionColumnIni; c < subseccionColumnFin; c++) {
                nueva[rowNueva][columnNueva] = board[r][c];
                partidasFijas[rowNueva][columnNueva] = fijos[r][c];
                columnNueva++;
            }
            rowNueva++;
        }
        // printBoard(partidasFijas);
        ///////////La subseccion se mezcla segun el vecindario (A o B)//////////////
        nueva = shuffleMatrix(nueva, partidasFijas, vecindario);
        // printBoard(nueva);     

        //////////Una vez que regresa la subseccion revuelta, se copia en el Sudoku(El tablero grande)//////////
        rowNueva = 0;
        columnNueva = 0;
        for (int r = subSeccionRowIni; r < subSeccionRowFin; r++) {
            columnNueva = 0;
            for (int c = subseccionColumnIni; c < subseccionColumnFin; c++) {
                board[r][c] = nueva[rowNueva][columnNueva];
                columnNueva++;
            }
            rowNueva++;
        }
    }

    private int[][] shuffleMatrix(int[][] resultado, int[][] fijas, String vecindad) {
        /*
        En el afán de obtener subsecciones bien mezcladas el algoritmo es un poco difícil de seguir. 
        Pero se obtuvieron buenos resultados. 
        El algoritmo que antes usaba para revolver las subsecciones no generaba ninguna solución con costo 0. 
        Por lo que mejoro bastante.
        
        
        La idea es 
        1- Aplanar las matrices a arreglos 
        2- Dejar solo soluciones permitidas(Que no sean fijas)
        3- Revolver el arreglo asegurando cambios
        4- Regresar el arreglo a matrix para posteriormente regresarlo a subSeccionVecindarios() y que se una con el sudoku
         */

        //Se aplanan las matrices   //No tengo idea porque no use desde el principio listas lol
        List<Integer> arrayAplanada = new ArrayList<Integer>();
        List<Integer> fijasAplanada = new ArrayList<Integer>();
        for (int i = 0; i < resultado.length; i++) {
            for (int j = 0; j < resultado[i].length; j++) {
                arrayAplanada.add(resultado[i][j]);
                fijasAplanada.add(fijas[i][j]);
            }
        }
        //Se quita las fijas. Quedando solo valores factibles
        for (int i = 0; i < fijasAplanada.size(); i++) {
            for (int j = 0; j < fijasAplanada.size(); j++) {
                if (arrayAplanada.contains(fijasAplanada.get(j))) {
                    arrayAplanada.remove(fijasAplanada.get(j));
                }
            }
        }
        //Se pasan de nuevo a un int[] (primitivo) para hacer shuffleArray
        int[] arrays = new int[arrayAplanada.size()];
        int[] fijos = new int[fijasAplanada.size()];
        for (int i = 0; i < arrays.length; i++) {
            arrays[i] = arrayAplanada.get(i);
            fijos[i] = fijasAplanada.get(i);
        }
        if (vecindad == "A") {
            shuffleArray(arrays); //Simplemene se les revuelve con el algorimo Durstenfeld (Durstenfeld shuffle)
        } else {//Es la vecindad B
            int index, index1;
            int aux = 0;
            boolean bandera = true;
            while(bandera){
            index = rd.nextIntAcotado(0, arrays.length - 1);
            index1 = rd.nextIntAcotado(0, arrays.length - 1);
            if (index != index1) {
                aux = arrays[index];
                arrays[index] = arrays[index1];
                arrays[index1] = aux;
                bandera = false;
            }
            }

        }

        int contador = 0;
        contador = 0;
        //Se modifica la subColumna ya como va a quedar 
        for (int x = 0; x < arrays.length; x++) {
            for (int i = 0; i < resultado.length; i++) {
                for (int j = 0; j < resultado.length; j++) {
                    if (fijas[i][j] == 0 && contador < arrays.length) {
                        resultado[i][j] = arrays[contador];
                        contador++;
                    } else {
                        if (fijas[i][j] != 0) {
                            resultado[i][j] = fijas[i][j];
                        }
                    }
                }
            }
        }
        return resultado;
    }

    private int costoTotal(int[][] matrix) {
        int costoRow = 0;
        int costoTotal = 0;
        int costoColumn = 0;
        boolean banderaRow = true;
        boolean banderaColumn = true;
        int contador = 0;
        int[] row = new int[BOARD_SIZE];
        int[] column = new int[BOARD_SIZE];

        while (contador != BOARD_SIZE) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                row[i] = matrix[contador][i];    //Aplano la matriz por fila
                column[i] = matrix[i][contador]; //Aplano la matriz por columnas
            }
            //Segun el articulo el costo es la cantidad de veces que no aparece un numero.
            //Lo equivalente es por cada vez que se repite un numero 
            //Por ejemplo dos veces el 7 tiene costo de 1
            //Y 3 veces el 7 tiene costo de 2, 4 tendria el costo de 3 y asi
            //Por alguna razon se me hizo mas facil programarlo asi que buscando los faltantes
            for (int i = 0; i < row.length; i++) { //Busco los costos
                banderaRow = true;
                banderaColumn = true;
                for (int j = i + 1; j < row.length; j++) {
                    if (row[i] == row[j] && banderaRow == true) {
                        costoRow++;
                        banderaRow = false;
                    }
                    if (column[i] == column[j] && banderaColumn == true) {
                        costoColumn++;
                        banderaColumn = false;
                    }
                }
            }
            contador++;
        }
        costoTotal += costoRow + costoColumn;

        return costoTotal;
    }

    /*
    Todos estos metodos no son parte esencial de la busqueda en si. 
    Pero son elementos de apoyo. 
    
     */
    private void fijos() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0) {
                    fijos[i][j] = board[i][j];
                } else {
                    fijos[i][j] = 0;
                }
            }
        }
    }

    private void printBoard(int[][] board) {

        for (int row = 0; row < board.length; row++) {
            if (row % 3 == 0) {
                System.out.println("-------------------------");
            }
            for (int column = 0; column < board[row].length; column++) {
                if (column % 3 == 0) {
                    System.out.print(" | ");
                }
                System.out.print(board[row][column] + " ");
            }
            System.out.println();
        }
    }

    private int[] eliminarFijos(int[] soluciones, int row, int column) {
        int[][] aux;
        aux = Arrays.copyOf(fijos, 9);

        int subSeccionRowIni = (row / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subSeccionRowFin = subSeccionRowIni + SUBSECTION_SIZE;

        int subseccionColumnIni = (column / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subseccionColumnFin = subseccionColumnIni + SUBSECTION_SIZE;

        for (int r = subSeccionRowIni; r < subSeccionRowFin; r++) {
            for (int c = subseccionColumnIni; c < subseccionColumnFin; c++) {
                if (aux[r][c] != 0) {
                    soluciones = comparar(soluciones, aux[r][c]);
                }

            }
        }
        return soluciones;

    }

    private int[] comparar(int[] array, int comparar) {
        int[] nuevo = array;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == comparar) {
                nuevo = eliminarElemento(array, comparar);
            }
        }
        return nuevo;

    }

    private int[] shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = rd.nextIntAcotado(0, i);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        return array;
    }

    private static int[] eliminarElemento(int[] modificado, int borar) {
        List<Integer> nuevo = new ArrayList<Integer>();
        for (int i = 0; i < modificado.length; i++) {
            if (modificado[i] != borar) {
                nuevo.add(modificado[i]);
            }
        }
        int[] nuevoArray = new int[nuevo.size()];
        nuevoArray = nuevo.stream().mapToInt(i -> i).toArray();
        return nuevoArray;
    }

    private int[][] copiarMatriz(int[][] input) {
        if (input == null) {
            return null;
        }
        int[][] result = new int[input.length][];
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
    }
}
