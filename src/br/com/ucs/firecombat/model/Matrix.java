package br.com.ucs.firecombat.model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.ucs.firecombat.constants.MatrixConstants;


public class Matrix {
	private static final Logger logger = 
			LogManager.getLogger(Matrix.class);
	private Integer[][] matrix;
	
	public Matrix() {
		int matrixsize = MatrixConstants.MATRIXSIZE;
		this.matrix = new Integer[matrixsize][matrixsize];
		init();
	}

	private void init() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j] = 0; 
			}
		}
	}

	public void insertFire(Fire fire) {
		matrix[fire.getX()][fire.getY()] = fire.getThreadId();
	}
	
	public void cleanPosition(int x, int y) {
		matrix[x][y] = 0;
	}

	public boolean existsIn(int x, int y) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if(i == x && j == y) {
					if(matrix[i][j] == 0) {
						return false;
					} else {
						logger.warn("existsIn()-Alread exists-x=" + x + ",y=" + y);
						return true;
					}
				}
			}
		}
		return true;
	}
}
