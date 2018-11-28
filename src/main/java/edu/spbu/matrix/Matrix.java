package edu.spbu.matrix;

import java.io.IOException;

/**
 *
 */
public interface Matrix
{
  int numberOfRows();
  double getCell(int row, int column);
  int numberOfColumns();
  boolean equals(Object o);
  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   * @param o
   * @return
   */
  Matrix mul(Matrix o) throws IOException;

  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   */
  //Matrix dmul(Matrix o);
}
