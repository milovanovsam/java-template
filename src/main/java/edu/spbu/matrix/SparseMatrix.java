package edu.spbu.matrix;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

import static java.lang.Math.abs;

/**
 * Разряженная матрица
 */
public class SparseMatrix implements Matrix
{
    public int rows = 0;
    public int columns = 0;
    ArrayList<Double> value = new ArrayList<>();
    ArrayList<Integer> row = new ArrayList<>();
    ArrayList<Integer> column = new ArrayList<>();

    @Override public int numberOfColumns() {
        return columns;
    }

    @Override public int numberOfRows() {
        return rows;
    }

    @Override public double getCell(int x, int y){
        int index = 0;
        for (int i=0; i< row.size(); i++)
            if (x == row.get(i)) {
                index = i;
                for (int j = 0; j < column.size(); j++)
                    if (j == index && y == column.get(j))
                        return value.get(index);
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
        int index = 0;
        for (int i = 0; i< matrix.rows; i++)
            for (int j=0; j<matrix.columns; j++)
                if (matrix.dMatrix[i][j] != 0)
                {
                    result.value.add(index, matrix.dMatrix[i][j]);
                    result.row.add(index, i);
                    result.column.add(index, j);
                    index++;
                }
        return result;
    }

    public SparseMatrix SparseMatrixTranspose( ) {
        SparseMatrix result = new SparseMatrix(columns, rows);
        int[] count = new int[columns];
        for (int i = 0; i< value.size(); i++) {
                result.value.add(i, value.get(i));
                result.row.add(i, column.get(i));
                result.column.add(i, row.get(i));
                count[column.get(i)]++;
            }
        SparseMatrix itog = new SparseMatrix(columns, rows);
        int j=0; //счетчик перебора
        int k = 0; //счетчик количества элементов каждой строки в матрице
        int h = 0; //счетчик для нового ключа
        for (int i = 0; i < rows;i++){
            while (  k < count[i]) {
                if(result.row.get(j) == i){
                    itog.value.add( h, result.value.get(j));
                    itog.row.add(h, result.row.get(j));
                    itog.column.add(h, result.column.get(j));
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
                  row.add(j,rows);
                  value.add(j,val);
                  column.add(j,i);
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
                     while ( p1 < value.size()  &&  p2 < Q.value.size() && row.get(p1).equals(row.get(i)) && Q.row.get(p2).equals(Q.row.get(j)))  {
                         if (column.get(p1) < Q.column.get(p2)) {
                                p1++;
                            } else if (column.get(p1) > Q.column.get(p2)) {
                                p2++;
                            } else if (column.get(p1).equals(Q.column.get(p2))) {
                                sum += value.get(p1) * Q.value.get(p2);
                               p1++; p2++;
                            }
                         if(p1 == value.size() || p2 == Q.value.size()) break;
                        }
                     if (sum != 0) {
                         result.value.add(sum);
                         result.row.add(row.get(i));
                         result.column.add(Q.row.get(j));
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
  @Override public Matrix dmul(Matrix o, int h)
  {
      SparseMatrix result = new SparseMatrix(rows, o.numberOfColumns());
      SparseMatrix result1 = new SparseMatrix(rows, o.numberOfColumns());
      SparseMatrix result2 = new SparseMatrix(rows, o.numberOfColumns());
      SparseMatrix result3 = new SparseMatrix(rows, o.numberOfColumns());
      SparseMatrix result0 = new SparseMatrix(rows, o.numberOfColumns());
      SparseMatrix Q  = ((SparseMatrix) o).SparseMatrixTranspose();

      class mul implements Runnable {
          int firstElement;
          int lastElement;
          int number;
          public mul (int x, int y, int z)
          {
              firstElement = x;
              lastElement = y;
              number = z;
          }
          public void run()
          {
              for(int i = firstElement; i < lastElement;){
                  int current1 = row.get(i);
                  for(int j = 0; j < Q.value.size();) {
                      int current2 = Q.row.get(j);
                      int p1 = i;
                      int p2 = j;
                      double sum = 0;
                      while ( p1 < lastElement  &&  p2 < Q.value.size() && row.get(p1).equals(row.get(i)) && Q.row.get(p2).equals(Q.row.get(j)))  {
                          if (column.get(p1) < Q.column.get(p2)) {
                              p1++;
                          } else if (column.get(p1) > Q.column.get(p2)) {
                              p2++;
                          } else if (column.get(p1).equals(Q.column.get(p2))) {
                              sum += value.get(p1) * Q.value.get(p2);
                              p1++; p2++;
                          }
                          if(p1 == lastElement || p2 == Q.value.size()) break;
                      }
                      if (sum != 0) {
                          switch(number){
                              case 0:
                                  result0.value.add(sum);
                                  result0.row.add(row.get(i));
                                  result0.column.add(Q.row.get(j));
                                  break;
                              case 1:
                                  result1.value.add(sum);
                                  result1.row.add(row.get(i));
                                  result1.column.add(Q.row.get(j));
                                  break;
                              case 2:
                                  result2.value.add(sum);
                                  result2.row.add(row.get(i));
                                  result2.column.add(Q.row.get(j));
                                  break;
                              case 3:
                                  result3.value.add(sum);
                                  result3.row.add(row.get(i));
                                  result3.column.add(Q.row.get(j));
                                  break;
                          }
                      }
                      while (j < Q.value.size() && Q.row.get(j) == current2)
                          j++;
                  }
                  while (i < lastElement && row.get(i) == current1)
                      i++;
              }
          }
      }

      int threadsCount = h;
      if (threadsCount > rows) threadsCount = rows;
      int count = value.size() / threadsCount; //сколько строк на каждый поток приходится
      int additional = value.size() % threadsCount;

      Thread[] threads = new Thread[threadsCount];
      int start = 0; int cnt; // индекс с которого начнём и сколько элементов каждому потоку по дефолту
      for (int i = 0; i < threadsCount; i++) {
          if (i == 0) cnt = count + additional;
          else cnt = count;
          int left = start + cnt - 1;
          int right = start + cnt + 1;
          if (i== threadsCount-1) {
              threads[i] = new Thread(new mul(start, (value.size()), i));
          } else {
              while (row.get(left).equals(row.get(start + cnt)) && row.get(right).equals(row.get(start + cnt))) {
                  left--;
                  right++;
              }
              if (!row.get(right).equals(row.get(start + cnt))) {
                  threads[i] = new Thread(new mul(start, right, i));
                  start = right;
              } else {
                  threads[i] = new Thread(new mul(start, (left+1), i));
                  start = (left + 1);
              }
          }
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
      for (int k = 0; k < result0.value.size(); k++) {
          result.value.add(result0.value.get(k));
          result.row.add(result0.row.get(k));
          result.column.add(result0.column.get(k));
      }
      for (int k = 0; k < result1.value.size(); k++) {
          result.value.add(result1.value.get(k));
          result.row.add(result1.row.get(k));
          result.column.add(result1.column.get(k));
      }
      for (int k = 0; k < result2.value.size(); k++) {
          result.value.add(result2.value.get(k));
          result.row.add(result2.row.get(k));
          result.column.add(result2.column.get(k));
      }
      for (int k = 0; k < result3.value.size(); k++) {
          result.value.add(result3.value.get(k));
          result.row.add(result3.row.get(k));
          result.column.add(result3.column.get(k));
      }
      return result;
  }

  @Override public boolean equals (Object o) {
      if(!(o instanceof Matrix))
          return false;

      SparseMatrix Q = ((SparseMatrix)o);
      if(Q.numberOfRows() != rows || Q.numberOfColumns() != columns) {
          return false;
      }
      if(value.size() != Q.value.size()){
          return false;
      }
      if (Q.value.size() > 0) {
          for (int i = 0; i < value.size(); i++)
              if (abs(value.get(i) - Q.value.get(i)) > EPSILON || !row.get(i).equals(Q.row.get(i))
                      || !column.get(i).equals(Q.column.get(i))) {
                  return false;
              }

      }
      return true;
  }
}
