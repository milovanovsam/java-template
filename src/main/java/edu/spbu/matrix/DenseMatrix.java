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
/*
  public SparseMatrix toSparse(){
    SparseMatrix result = new SparseMatrix(rows, columns);
    int number = 0;
    for (int i = 0; i<rows; i++)
      for (int j=0; j<columns; j++)                             перевод dense в sparce после написания класса sparce
        if (dMatrix[i][j] != 0)
        {
          result.value.put(number, dMatrix[i][j]);
          result.row.put(number, i);
          result.column.put(number, j);
          number++;
        }
    number++;
    return result;
  }
  */
public DenseMatrix(int x, int y){
  rows = x;
  columns = y;
  dMatrix = new double[rows][columns];
}

  public void printDenseMatrix(){
    for (int i = 0; i<rows; i++){
      for (int j=0; j<columns; j++)
        System.out.print(dMatrix[i][j]+ " ");
      System.out.println(" ");
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
      ++rows;
      currentRow = input.nextLine();
      currentRowArray = currentRow.split(" ");
      if(rows == 1)
      {
        columns = currentRowArray.length;
        dMatrix = new double[ARRAY_SIZE][columns];
      }

      for(j=0; j<columns; ++j)
      {
        dMatrix[rows-1][j]=Double.parseDouble(currentRowArray[j]);
      }
    }
    input.close();
  }


/**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   */
  @Override public Matrix mul(Matrix o) throws IOException {
    if (o instanceof DenseMatrix) {

      int row1 = rows;
      int col1 = columns;
      int row2 = o.numberOfRows();
      int col2 = o.numberOfColumns();

      DenseMatrix result = new DenseMatrix(row1, col2);

      for (int i = 0; i < row1; i++)
        for (int j = 0; j < col2; j++)
          for (int k = 0; k < col1; k++)
            result.dMatrix[i][j] += dMatrix[i][k] * o.getCell(k, j);
      return result;
    }
    /*if (o instanceof SparseMatrix)
    {
      return this.toSparse().mul(o);
    }*/
    return null;
  }
  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return

  @Override public Matrix dmul(Matrix o)
  {
    return null;
  }


   * спавнивает с обоими вариантами
   * @param o
   * @return
   */
  @Override public boolean equals(Object o){
    if(!(o instanceof Matrix))
      return false;

    Matrix Q = ((Matrix)o);
    if(Q.numberOfRows() != rows || Q.numberOfColumns() != columns)
      return false;

    int i, j;
    for(i = 0; i < rows; ++i)
      for(j=0; j< columns; ++j)
        if(dMatrix[i][j] != Q.getCell(i,j))
          return false;
    return true;
  }
}
