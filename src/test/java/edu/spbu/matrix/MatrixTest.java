package edu.spbu.matrix;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MatrixTest
{
  /**
   * ожидается 4 таких теста
   */
  /*@Test
  public void mulDD() {
    Matrix m1 = new DenseMatrix("m1.txt");
    Matrix m2 = new DenseMatrix("m2.txt");
    Matrix expected = new DenseMatrix("result.txt");
    assertEquals(expected, m1.mul(m2));
  }*/
  @Test
  public void mulDD()  throws IOException
  {
    Matrix m1 = new DenseMatrix("src/test/matrix/dense1.txt");
    Matrix m2 = new DenseMatrix("src/test/matrix/dense2.txt");
    Matrix expected = new DenseMatrix("src/test/matrix/result.txt");
    assertEquals(expected, m1.mul(m2));
  }
  @Test
  public void printHELLO() {
    System.out.println("HELLO");
  }
}
