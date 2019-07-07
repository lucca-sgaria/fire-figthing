package br.com.ucs.firecombat.model;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.ucs.firecombat.constants.MatrixConstants;
import br.com.ucs.firecombat.constants.StepConstant;

public class Matrix {
	private static final Logger logger = LogManager.getLogger(Matrix.class);
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

	public void insertFireFighter(Firefighter firefighter) {
		matrix[firefighter.getX()][firefighter.getY()] = firefighter.getThreadId();
	}

	public void insertRefugee(Refugee refugee) {
		matrix[refugee.getX()][refugee.getY()] = refugee.getThreadId();
	}

	public void insertParamedics(Paramedics paramedics) {
		matrix[paramedics.getX()][paramedics.getY()] = paramedics.getThreadId();
	}

	public void cleanPosition(int x, int y) {
		matrix[x][y] = 0;
	}

	public boolean existsIn(int x, int y) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (i == x && j == y) {
					if (matrix[i][j] == 0) {
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

	public int[] findObjects(int x, int y, int type) {
		logger.info("findObjects() - " + type);

		int[] xa = { 0, 1, 0, -1, 0, 1, 1, -1, -1 };
		int[] ya = { 0, 0, -1, 0, 1, 1, -1, -1, 1 };

		int[] objectAddress = new int[3];

		for (int i = 0; i < xa.length; i++) {

			int newDestinyX = x + xa[i];
			int newDestinyY = y + ya[i];

			if (newDestinyX < 0 || newDestinyX >= MatrixConstants.MATRIXSIZE) {
				continue;
			}

			if (newDestinyY < 0 || newDestinyY >= MatrixConstants.MATRIXSIZE) {
				continue;
			}

			objectAddress[0] = newDestinyX;
			objectAddress[1] = newDestinyY;
			objectAddress[2] = matrix[newDestinyX][newDestinyY];
			/*
			 * type 1 procura por fogo. type 2 procura por vitimas
			 */
			if (type == 1) {
				if (matrix[newDestinyX][newDestinyY] >= MatrixConstants.FIRE_INTERVAL_VALUES_MIN &&
						matrix[newDestinyX][newDestinyY] <= MatrixConstants.FIRE_INTERVAL_VALUES_MAX) {
					logger.info("findObjects() - found fire ");
					return objectAddress;
				}
			} else if (type == 2) {
				if (matrix[newDestinyX][newDestinyY] >= MatrixConstants.REFUGEE_INTERVAL_VALUES_MIN &&
						matrix[newDestinyX][newDestinyY] <= MatrixConstants.REFUGEE_INTERVAL_VALUES_MAX) {
					logger.info("findObjects() - found vitima");
					return objectAddress;
				}
			} else if(type == 3){
				if (matrix[newDestinyX][newDestinyY] >= MatrixConstants.PARAMEDICS_INTERVAL_VALUES_MIN &&
						matrix[newDestinyX][newDestinyY] <= MatrixConstants.PARAMEDICS_INTERVAL_VALUES_MAX) {
					logger.info("findObjects() - found paramedics ");
					return objectAddress;
				}
			}else if (type == 5) {
				if (matrix[newDestinyX][newDestinyY] >= MatrixConstants.FIRE_INTERVAL_VALUES_MIN &&
						matrix[newDestinyX][newDestinyY] <= MatrixConstants.PARAMEDICS_INTERVAL_VALUES_MAX) {
					logger.info("findObjects() - found something");
					return objectAddress;
				}
			}
		}
		logger.info("findObjects() - free");
		return objectAddress;
	}

	public int[] calculateNextStep(int x, int y) {
		logger.info("calculateNextStep()-x="+x+",y="+y);
		int randomInt = ThreadLocalRandom.current().nextInt(0, 8);
		System.out.println("RANDOM INT=" + randomInt);
		int[] vet = {-1,-1};
		switch (randomInt) {
		
		case StepConstant.UP:
			if(exists(x-1,y)) {
				vet[0] = x-1;
				vet[1] = y; 
				return vet; 
			};
			break;
		case StepConstant.UPRIGHT:
			if(exists(x-1,y+1)) {
				vet[0] = x-1;
				vet[1] = y+1; 
				return vet; 
			};
			break;
		case StepConstant.RIGHT:
			if(exists(x,y+1)) {
				vet[0] = x;
				vet[1] = y+1; 
				return vet; 
			};
			break;
		case StepConstant.DOWNRIGHT:
			if(exists(x+1,y+1)) {
				vet[0] = x+1;
				vet[1] = y+1; 
				return vet; 
			};
			break;
		case StepConstant.DOWN:
			if(exists(x+1,y)) {
				vet[0] = x+1;
				vet[1] = y; 
				return vet; 
			};
			break;
		case StepConstant.DOWNLEFT:
			if(exists(x+1,y-1)) {
				vet[0] = x+1;
				vet[1] = y-1; 
				return vet; 
			};
			break;
		case StepConstant.LEFT:
			if(exists(x,y-1)) {
				vet[0] = x;
				vet[1] = y-1; 
				return vet; 
			};
			break;
		case StepConstant.UPLEFT:
			if(exists(x-1,y-1)) {
				vet[0] = x-1;
				vet[1] = y; 
				return vet; 
			};
			break;
		}
		return vet;
	}

	private boolean exists(int x, int y) {
		logger.info("exists()-x="+x+",y="+y);
		Integer integer = null;
		try {
			integer = matrix[x][y];
		} catch (Exception e) {
			logger.error("exists()-exception");
		}
		logger.info("exists()-true");
		return integer == null ? false : true;
	}
}
