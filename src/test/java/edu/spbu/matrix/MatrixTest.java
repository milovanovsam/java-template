package edu.spbu.matrix;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class MatrixTest {
  @Test
  public void Print()  throws IOException{
    Matrix m2 = new SparseMatrix("src/test/matrix/sparse1.txt");
    SparseMatrix Q = ((SparseMatrix)m2);
    Q.printSparseMatrix();
  }
  @Test
  public void Trans() throws IOException
  {
    Matrix m1 = new SparseMatrix("src/test/matrix/example.txt");
    Matrix expected = new SparseMatrix("src/test/matrix/exampleT.txt");
    m1 = ((SparseMatrix)m1).SparseMatrixTranspose();
    //((SparseMatrix) expected).printSparseMatrix();
    assertEquals(m1,expected);
  }
  @Test
  public void mulDD() throws IOException {
    Matrix m1 = new DenseMatrix("src/test/matrix/dense1.txt");
    Matrix m2 = new DenseMatrix("src/test/matrix/dense2.txt");
    Matrix expected = new DenseMatrix("src/test/matrix/resultdd.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulDS()  throws IOException
  {
    Matrix m1 = new DenseMatrix("src/test/matrix/dense1.txt");
    Matrix m2 = new SparseMatrix("src/test/matrix/sparse1.txt");
    Matrix expected = new SparseMatrix("src/test/matrix/resultds.txt");
    assertEquals(expected, m1.mul(m2));
  }
  @Test
  public void mulSD()  throws IOException
  {
    Matrix m1 = new SparseMatrix("src/test/matrix/sparse1.txt");
    Matrix m2 = new DenseMatrix("src/test/matrix/dense1.txt");
    Matrix expected = new SparseMatrix("src/test/matrix/resultsd.txt");
    assertEquals(expected, m1.mul(m2));
  }
  @Test
  public void mulSS()  throws IOException
  {
    Matrix m1 = new SparseMatrix("src/test/matrix/sparse1.txt");
    Matrix m2 = new SparseMatrix("src/test/matrix/sparse2.txt");
    Matrix expected = new SparseMatrix("src/test/matrix/resultss.txt");
    assertEquals(expected, m1.mul(m2));
  }
  @Test
  public void equals() throws IOException
  {
    Matrix m1 = new DenseMatrix("src/test/matrix/dense1.txt");
    Matrix m2 = new DenseMatrix("src/test/matrix/dense2.txt");
    boolean k = m1.equals(m1);
    assertEquals(k, true);
  }
}
