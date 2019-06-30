package br.com.ucs.firecombat.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.ucs.firecombat.constants.MatrixConstants;

public class Enviroment {
	private static final Logger logger = 
			LogManager.getLogger(Enviroment.class);
	
	public static void main(String[] args) {
		Enviroment env = new Enviroment();
		env.init();
	}
	
	private List<Fire> fires;
	private Matrix matrix;
	private Semaphores semaphores;
	private Listeneres listeners;
	
	public Enviroment() {
		configureListeners();
	}
	
	private void init() {
		logger.info("init()");
		
		configureMatrix();
		configureSemaphores();
		generateFirstFires();
		
		for (Fire fire : fires) {
			fire.start();
		}
	}

	private void configureMatrix() {
		logger.info("configureMatrix()-");
		this.matrix = new Matrix();
		logger.info("configureMatrix()-end");
	}

	private void configureSemaphores() {
		logger.info("configureSemaphores()-");
		this.semaphores = new Semaphores();
		semaphores.init();
		logger.info("configureSemaphores()-end");
	}
	
	private void configureListeners() {
		logger.info("configureListeners()-");
		this.listeners = new Listeneres();
		logger.info("configureListeners()-end");
	}
	
	/*
	 * Gera os fogos iniciais
	 */
	private void generateFirstFires() {
		this.fires = new ArrayList<Fire>();
		int fireamount = MatrixConstants.FIREAMOUNT;
		logger.info("generateFirstFires()-fireamount-" +fireamount);
		
		for (int i = 1; i <= fireamount; i++) {
			Fire e = new Fire(i, null, semaphores.getSemWriteToMatrix(),this);
			fires.add(e);
			logger.info("init()-created fire-" +e.toString());
		}
		logger.info("generateFirstFires()-end");
	}
	
	public int generateRandom() {
		logger.info("generateRandom()-");
		int matrixsize = MatrixConstants.MATRIXSIZE;
		int nextInt = new Random().nextInt(matrixsize);
		logger.info("generateRandom()-generated-"+nextInt);
		return nextInt;
	}
	
	public void insertFire(Fire fire) {
		logger.info("insertFire()-"+fire.toString());
		matrix.insertFire(fire);
		listeners.notifyFireAddedListeners(fire);
		logger.info("insertFire()-end");
	}
	
	
	public void cleanPosition(int x, int y) {
		logger.info("cleanPosition()-x=" + x + ",y=" + y);
		matrix.cleanPosition(x, y);
		listeners.notifyFireRemovedListeners(x,y);
		logger.info("cleanPosition()-end");
	}
	
	public boolean existsIn(int x,int y) {
		logger.info("existsIn()-x=" + x + ",y=" + y);
		boolean existsIn = matrix.existsIn(x,y);
		logger.info("existsIn()-x=" + x + ",y=" + y+"-value="+existsIn);
		return existsIn;
	}
	
	public List<Fire> getFires() {
		return fires;
	}
	public void setFires(List<Fire> fires) {
		this.fires = fires;
	}

	public void start() {
		init();
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}

	public Semaphores getSemaphores() {
		return semaphores;
	}

	public void setSemaphores(Semaphores semaphores) {
		this.semaphores = semaphores;
	}

	public Listeneres getListeners() {
		return listeners;
	}

	public void setListeners(Listeneres listeners) {
		this.listeners = listeners;
	}
	
	
	
}
