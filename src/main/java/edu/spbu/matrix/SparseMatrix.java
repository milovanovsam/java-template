package edu.spbu.matrix;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * Разряженная матрица
 */
public class SparseMatrix implements Matrix
{
    public int rows = 0;
    public int columns = 0;
    public HashMap<Integer,Double> value = new HashMap<>();
    public HashMap<Integer,Integer> row = new HashMap<>();
    public HashMap<Integer,Integer> column = new HashMap<>();

    @Override public int numberOfColumns() {
        return columns;
    }

    @Override public int numberOfRows() {
        return rows;
    }

    @Override public double getCell(int x, int y){
        int key = 0;
        for (int i=0; i< row.size(); i++)
            if (x == row.get(i)) {
                key = i;
                for (int j = 0; j < column.size(); j++)
                    if (j == key && y == column.get(j))
                        return value.get(key);
            }
        return 0;
    }

    public void printSparseMatrix() {
        double[][] array = new double[rows][columns];
        for (int i = 0; i < value.size(); i++)
            array[row.get(i)][column.get(i)] = value.get(i);

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; j++)
                System.out.print(array[i][j] + " ");
            System.out.println();
        }
    }

    public SparseMatrix(int x, int y){
        rows = x;
        columns = y;
    }

    public SparseMatrix toSparse(DenseMatrix matrix){
        SparseMatrix result = new SparseMatrix(matrix.rows, matrix.columns);
        int key = 0;
        for (int i = 0; i< matrix.rows; i++)
            for (int j=0; j<matrix.columns; j++)
                if (matrix.dMatrix[i][j] != 0)
                {
                    result.value.put(key, matrix.dMatrix[i][j]);
                    result.row.put(key, i);
                    result.column.put(key, j);
                    key++;
                }
        return result;
    }

    public SparseMatrix SparseMatrixTranspose( ) {
        SparseMatrix result = new SparseMatrix(columns, rows);
        int[] count = new int[columns];
        for (int i = 0; i< value.size(); i++) {
                result.value.put(i, value.get(i));
                result.row.put(i, column.get(i));
                result.column.put(i, row.get(i));
                count[column.get(i)]++;
            }
        SparseMatrix itog = new SparseMatrix(columns, rows);
        int j=0; //счетчик перебора
        int k = 0; //счетчик количества элементов каждой строки в матрице
        int h = 0; //счетчик для нового ключа
        for (int i = 0; i < rows;i++){
            while (  k < count[i]) {
                if(result.row.get(j) == i){
                    itog.value.put(h, result.value.get(j));
                    itog.row.put(h, result.row.get(j));
                    itog.column.put(h, result.column.get(j));
                    k++;
                    h++;
                }
                j++;
            }
            k=0;
            j=0;
        }
    return itog;
        }

    /**
   * загружает матрицу из файла
   * @param fileName
   */
  public SparseMatrix(String fileName) throws FileNotFoundException {
      if(fileName.trim().length()==0) // если файл не существует
          return;

      String[] currentRowArray;
      Scanner input = new Scanner(new File(fileName));
      String currentRow="1";
      double val;
      int j = 0;

      while(input.hasNextLine() && currentRow.trim().length()!=0)
      {
          currentRow = input.nextLine();
          currentRowArray = currentRow.split(" ");
          for (int i = 0; i < currentRowArray.length; i++){
              val = Double.parseDouble(currentRowArray[i]);
              if (val != 0.0) {
                  row.put(j,rows);
                  value.put(j,val);
                  column.put(j,i);
                  j++;
              }
          }
          rows++;
          if(rows == 1)
          {
              columns = currentRowArray.length;
          }
      }
      input.close();
  }
  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param
   * @return
   */

  @Override public Matrix mul(Matrix o) throws IOException{
      if (o instanceof DenseMatrix)
          o = ((DenseMatrix)o).toSparse();
      if (o instanceof SparseMatrix) {
          if (columns != ((SparseMatrix) o).rows) {
              System.out.println("Error: different sizes");
              return null;
          }
          SparseMatrix result = new SparseMatrix(rows,((SparseMatrix) o).columns);
          SparseMatrix Q  = ((SparseMatrix) o).SparseMatrixTranspose();
          for(int i = 0; i < value.size();){
              int current1 = row.get(i);
              for(int j = 0; j < Q.value.size();) {
                  int current2 = Q.row.get(j);
                  int p1 = i;
                     int p2 = j;
                     double sum = 0;
                     while ( (p1 < value.size() ) && ( row.get(p1)  ==  row.get(i) ) && ( Q.row.get(p2) == Q.row.get(j) )  && ( p2 < Q.value.size() ) ) {
                         if (column.get(p1) < Q.column.get(p2)) {
                                p1++;
                            } else if (column.get(p1) > Q.column.get(p2)) {
                                p2++;
                            } else if (column.get(p1) == Q.column.get(p2)) {
                                sum += value.get(p1) * Q.value.get(p2);
                                p1++; p2++;
                            }
                        }
                     if (sum != 0) {
                         int key = result.value.size();
                         result.value.put(key, sum);
                         result.row.put(key, row.get(i));
                         result.column.put(key, Q.row.get(j));
                            }
                  while (j < Q.value.size() && Q.row.get(j) == current2)
                      j++;
              }
              while (i < value.size() && row.get(i) == current1)
                  i++;
          }
       return result;
      }
      return null;
  }


    /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   */
  /*@Override public Matrix dmul(Matrix o)
  {
    return null;
  }*/

  @Override public boolean equals (Object o) {
      if(!(o instanceof Matrix))
          return false;

      SparseMatrix Q = ((SparseMatrix)o);
      if(Q.numberOfRows() != rows || Q.numberOfColumns() != columns)
          return false;
      for (int i = 0; i<value.size(); i++)
          if (value.get(i)-Q.value.get(i) != 0 || row.get(i)-Q.row.get(i) != 0
                  || column.get(i)-Q.column.get(i) != 0)
              return false;
      return true;
  }
}
