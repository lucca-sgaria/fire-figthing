package br.com.ucs.firecombat.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import br.com.ucs.firecombat.constants.MatrixConstants;
import sun.awt.Mutex;

public class Enviroment {
	private static final Logger logger =
			LogManager.getLogger(Enviroment.class);

	public static void main(String[] args) {
		Enviroment env = new Enviroment();
		env.init();
	}
	
	private List<Fire> fires;
	private List<Firefighter> firefighters;
	
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
		generateFirstFighters();
	
		for (Fire fire : fires) {
			fire.start();
		}
		
		for (Firefighter firefighter : firefighters) {
			System.out.println("aaa");
			firefighter.start();
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
			int id = i+100;
			Fire e = new Fire(id, null, semaphores.getSemWriteToMatrix(),this);
			fires.add(e);
			logger.info("init()-created fire-" +e.toString());
		}
		logger.info("generateFirstFires()-end");
	}
	
	private void generateFirstFighters() {
		this.firefighters = new ArrayList<Firefighter>();
		int firefightersAmount = MatrixConstants.FIREFIGHTERSAMOUNT;
		logger.info("generateFirstFighters()-firefighter-" +firefightersAmount);
		
		for (int i = 1; i <= firefightersAmount; i++) {
			int id = i+1000;
			Firefighter e = new Firefighter(id, semaphores.getSemWriteToMatrix(),this);
			firefighters.add(e);
			logger.info("init()-created firefighter-" +e.toString());
		}
		logger.info("generateFirstFighters()-end");
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
	
	public void insertFireFighter(Firefighter firefighter) {
		logger.info("insertFireFighter()-"+firefighter.toString());
		matrix.insertFireFighter(firefighter);
		listeners.notifyFireFighterAddedListeners(firefighter);
		logger.info("insertFireFighter()-end");
	}
	
	
	public void cleanPosition(int x, int y,int threadId) {
		logger.info("cleanPosition()-x=" + x + ",y=" + y);
		matrix.cleanPosition(x, y);
		if(threadId >= 100 && threadId <= 201) {
			listeners.notifyFireRemovedListeners(x,y);
		}
		
		if(threadId >= 1000 && threadId <= 1999) {
			listeners.notifyFireFighterRemovedListeners(x, y);
		}
		
		logger.info("cleanPosition()-end");
	}
	
	public boolean existsIn(int x,int y) {
		logger.info("existsIn()-x=" + x + ",y=" + y);
		boolean existsIn = matrix.existsIn(x,y);
		logger.info("existsIn()-x=" + x + ",y=" + y+"-value="+existsIn);
		return existsIn;
	}
	
	public int[] findObject(Firefighter firefighter) {
		logger.info("findObjectaaaaaaaaa()-firefighter-"+firefighter.toString());
		int[] obj = matrix.findObjects(firefighter.getX(), firefighter.getY(), 5);
		
		if(obj[2] == firefighter.getThreadId()) {
			obj[0] = 0;
			obj[1] = 0;
			obj[2] = 0;
		}
		
		logger.info("findObject()-object-end-x=" + obj[0] +",y="+obj[1]+",id="+obj[2]);
		return obj;
	}
	
	public Fire findFire(int threadId) {
		logger.info("findFire()-fireId-"+threadId);
		Optional<Fire> findFirst = fires.stream().filter(f -> f.getThreadId() == threadId).findFirst();
		Fire fire = null;
		try {
			fire = findFirst.get();
			logger.info("findFire()-fire-"+fire.toString());
		} catch (Exception e) {
			logger.error("e" + e.getMessage());
		}
		return fire;
	}
	
	public void start() {
		init();
	}
	public List<Fire> getFires() {
		return fires;
	}
	public void setFires(List<Fire> fires) {
		this.fires = fires;
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
