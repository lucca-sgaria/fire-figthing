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

	public int[] findObjects(int x, int y, int type){
		logger.info("findObjects() - " + type);

		int[] xa = {0,1,0,-1,0,1,1,-1,-1};
		int[] ya = {0,0,-1,0,1,1,-1,-1,1};

		int[] objectAddress = new int[3];

		for(int i = 0; i < xa.length; i++){

			int newDestinyX = x + xa[i];
			int newDestinyY = y + ya[i];

			if(newDestinyX < 0 || newDestinyX >= MatrixConstants.MATRIXSIZE){
				continue;
			}

			if(newDestinyY < 0 || newDestinyY >= MatrixConstants.MATRIXSIZE){
				continue;
			}

			objectAddress[0] = newDestinyX;
			objectAddress[1] = newDestinyY;
			objectAddress[2] = matrix[newDestinyX][newDestinyY];
			/*
				type 1 procura por fogo.
				type 2 procura por vitimas
			 */
			if(type == 1){
				if(matrix[newDestinyX][newDestinyY] >= 1
						&& matrix[newDestinyX][newDestinyY] <= 100){
					logger.info("findObjects() - found fire ");
					return objectAddress;
				}
			}else if(type == 2){
				if(matrix[newDestinyX][newDestinyY] >= 101
						&& matrix[newDestinyX][newDestinyY] <= 200){
					logger.info("findObjects() - found vitima");
					return objectAddress;
				}
			}
		}
		logger.info("findObjects() - free");

		return null;
	}
}
