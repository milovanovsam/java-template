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
    assertEquals(m1,expected);
  }
  @Test
  public void mulDD() throws IOException {
    Matrix m1 = new DenseMatrix("src/test/matrix/dense1.txt");
    Matrix m2 = new DenseMatrix("src/test/matrix/dense2.txt");
    Matrix expected = new DenseMatrix("src/test/matrix/resultdd.txt");
    SparseMatrix k = ((DenseMatrix)m1).toSparse();
    Matrix T = k.mul(m2);
    //m1 = ((DenseMatrix)m1.mul(m2)).toSparse();
    SparseMatrix nose = ((DenseMatrix)expected).toSparse();
    assertEquals(nose, T);
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
    m2 = m1.mul(m2);
    assertEquals(expected, m2);
  }
  @Test
  public void BigMulSS()  throws IOException
  {
    Matrix m1 = new SparseMatrix("m1.txt");
    Matrix m2 = new SparseMatrix("m2.txt");
    m2 = m1.mul(m2);
    SparseMatrix Q = ((SparseMatrix)m2);
    //Q.printSparseMatrix();
    //assertEquals(expected, m2);
  }
  @Test
  public void equals() throws IOException
  {
    Matrix m1 = new DenseMatrix("src/test/matrix/dense1.txt");
    assertEquals(m1, m1);
  }
  @Test
  public void dmulDD() throws IOException {
    Matrix m1 = new DenseMatrix("m1.txt");
    Matrix m2 = new DenseMatrix("m2.txt");
    long t1 = System.currentTimeMillis();
    Matrix R = m1.mul(m2);
    long t2 = System.currentTimeMillis();
    long t3 = System.currentTimeMillis();
    Matrix R2 = m1.dmul(m2,2);
    long t4 = System.currentTimeMillis();
    long t5 = System.currentTimeMillis();
    Matrix R3 = m1.dmul(m2,4);
    long t6 = System.currentTimeMillis();
    SparseMatrix Q = ((DenseMatrix)m1).toSparse();
    SparseMatrix D = ((DenseMatrix)m2).toSparse();
    Matrix R4 = new SparseMatrix(2000,2000);
    long t7 = System.currentTimeMillis();
    R4 = Q.mul(D);
    long t8 = System.currentTimeMillis();
    long t9 = System.currentTimeMillis();
    R4 = Q.dmul(D,4);
    long t10 = System.currentTimeMillis();
    R3 = ((DenseMatrix)R3).toSparse();
    System.out.println("Время Sparse параллельного перемножения матриц на 4 потоках размерами 2000 X 2000: "
            +(t10-t9));
    System.out.println("Время Sparse перемножения матриц с размерами 2000 X 2000: "
            +(t8-t7));
    System.out.println("Время параллельного перемножения матриц на 4 потоках размерами 2000 X 2000: "
            +(t6-t5));
    System.out.println("Время параллельного перемножения матриц на 2 потоках размерами 2000 X 2000: "
            +(t4-t3));
    System.out.println("Время обычного перемножения матриц размерами 2000 X 2000: "
            +(t2-t1));
    assertEquals(R3,R4);
  }
  @Test
  public void dmulSS () throws IOException {
    Matrix m1 = new SparseMatrix("m1.txt");
    Matrix m2 = new SparseMatrix("m2.txt");
    long t1 = System.currentTimeMillis();
    Matrix R = m1.mul(m2);
    long t2 = System.currentTimeMillis();
    long t3 = System.currentTimeMillis();
    Matrix R2 = m1.dmul(m2,4);
    long t4 = System.currentTimeMillis();
    System.out.println("Время Sparse параллельного перемножения матриц на 4 потоках размерами 2000 X 2000: "
            +(t4-t3));
    System.out.println("Время Sparse перемножения матриц с размерами 2000 X 2000: "
            +(t2-t1));
    assertEquals(R,R2);
  }
}
