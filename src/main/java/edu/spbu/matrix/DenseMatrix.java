package edu.spbu.matrix;

import java.io.*;
import java.util.Scanner;
/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix
{
  public static final int ARRAY_SIZE = 3000;
  int rows = 0;
  int columns = 0;
  public double[][] dMatrix;

  @Override public int numberOfColumns() {
    return columns;
  }

  @Override
  public int numberOfRows() {
    return rows;
  }
  @Override
  public double getCell(int row, int column) {
    return dMatrix[row][column];
  }

  public SparseMatrix toSparse(){
    SparseMatrix result = new SparseMatrix(rows, columns);
    int number = 0;
    for (int i = 0; i<rows; i++)
      for (int j=0; j<columns; j++)
        if (dMatrix[i][j] != 0)
        {
          result.value.add(number, dMatrix[i][j]);
          result.row.add(number, i);
          result.column.add(number, j);
          number++;
        }
    //System.out.println();
    return result;
  }

public DenseMatrix(int x, int y){
  rows = x;
  columns = y;
  dMatrix = new double[x][y];
}

  public void printDenseMatrix(){
    for (int i = 0; i<rows; i++){
      for (int j=0; j<columns; j++)
        System.out.print(dMatrix[i][j]+ " ");
      System.out.println();
    }
  }
  /**
   * загружает матрицу из файла
   * @param fileName
   */
  public DenseMatrix(String fileName) throws IOException{
    if(fileName.trim().length()==0) // если файл не существует
      return;

    int j;
    String[] currentRowArray;
    Scanner input = new Scanner(new File(fileName));
    String currentRow="1";

    while(input.hasNextLine() && currentRow.trim().length()!=0) /* пока есть строка и она не пустая */
    {

      currentRow = input.nextLine();
      currentRowArray = currentRow.split(" ");
      if(rows == 0)
      {
        columns = currentRowArray.length;
        dMatrix = new double[ARRAY_SIZE][columns];
      }

      for(j=0; j<columns; ++j)
      {
        dMatrix[rows][j]=Double.parseDouble(currentRowArray[j]);
      }
      rows++;
    }
    input.close();
  }

  @Override public Matrix mul(Matrix o) throws IOException {
    if (o instanceof DenseMatrix) {

      int row1 = rows;
      int col1 = columns;
      int col2 = o.numberOfColumns();

      DenseMatrix result = new DenseMatrix(row1, col2);

      for (int i = 0; i < row1; i++)
        for (int j = 0; j < col2; j++)
          for (int k = 0; k < col1; k++)
            result.dMatrix[i][j] += dMatrix[i][k] * o.getCell(k, j);
      return result;
    }
    if (o instanceof SparseMatrix)
    {
      return this.toSparse().mul(o);
    }
    return null;
  }
  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   * */

  @Override public Matrix dmul(Matrix o, int h)
  {
    DenseMatrix result = new DenseMatrix(rows, o.numberOfColumns());

    class mul implements Runnable {
      int firstRow;
      int lastRow;
      public mul (int x, int y)
      {
        firstRow = x;
        lastRow = y;
      }
      public void run()
      {
        for (int i = firstRow; i <= lastRow; i++)
          for( int j = 0; j < o.numberOfColumns(); j++)
            for ( int k = 0; k < o.numberOfRows(); k++)
              result.dMatrix[i][j] += getCell(i,k) * o.getCell(k,j);
      }
    }

    int threadsCount = h;
    if (threadsCount > rows) threadsCount = rows;

    int count = rows / threadsCount; //сколько строк на каждый поток приходится
    int additional = rows % threadsCount;

    Thread[] threads = new Thread[threadsCount];
    int start = 0; int cnt;
    for (int i = 0; i < threadsCount; i++) {
      if (i == 0) cnt = count + additional;
      else cnt = count;
      threads[i] = new Thread(new mul(start, start + cnt - 1));
      start += cnt;
      threads[i].start();
    }
    //ждем завершения всех потоков
    try {
      for (int t = 0; t < threadsCount; t++) {
        threads[t].join();
      }
    } catch (InterruptedException e) {
      System.out.println("Interrupted");
    }
    return result;
  }

  @Override public boolean equals(Object o){
    if(!(o instanceof Matrix))
      return false;

    Matrix P = ((Matrix)o);
    if(P.numberOfRows() != rows || P.numberOfColumns() != columns)
      return false;

    int i, j;
    int k = 0;
    for(i = 0; i < rows; i++)
      for(j = 0; j < columns; j++)
        if(dMatrix[i][j] != P.getCell(i,j)) {
          return false;
        }
    return true;
  }
}
