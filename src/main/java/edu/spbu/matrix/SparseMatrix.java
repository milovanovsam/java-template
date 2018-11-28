package edu.spbu.matrix;
import java.io.IOException;
/**
 * Разряженная матрица
 */
public class SparseMatrix implements Matrix
{
  /**
   * загружает матрицу из файла
   * @param fileName
   */
  public SparseMatrix(String fileName) {

  }
  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param
   * @return
   */
  @Override public double getCell(int x, int y){
    return 0;
  }
  @Override public int numberOfColumns() {
    return 0;
  }

  @Override
  public int numberOfRows() {
    return 0;
  }
    @Override public Matrix mul(Matrix o) throws IOException{
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
  }

  /**
   * спавнивает с обоими вариантами
   * @param o
   * @return
   */
  @Override public boolean equals(Object o) {
    return false;
  }
}
