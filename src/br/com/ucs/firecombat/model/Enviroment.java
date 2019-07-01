package br.com.ucs.firecombat.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
	private List<Firefighter> firefighters;
	private List<Refugee> listRefugees;
	
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
		genereteFirstObjects();
	
		for (Fire fire : fires) {
			fire.start();
		}
		
		for (Firefighter firefighter : firefighters) {
			System.out.println("aaa");
			firefighter.start();
		}

		listRefugees.forEach(Thread::start);

//		try{
//
//			for (Fire fire : fires){
//				fire.join();
//			}
//
//			for(Firefighter firefighter : firefighters){
//				firefighter.join();
//			}
//
//			for (Refugee refugee : listRefugees) {
//				refugee.join();
//			}
//		}catch (InterruptedException e){
//			e.printStackTrace();
//		}

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
		Gera todos os individuos para o inicio da simulação
	 */

	private void genereteFirstObjects(){
		this.fires = new ArrayList<>();
		this.firefighters = new ArrayList<>();
		this.listRefugees = new ArrayList<>();

		logger.info("genereteFirstObjects()");

		for(int i = 1; i <= MatrixConstants.FIREAMOUNT; i++){
			fires.add(new Fire(i+MatrixConstants.FIRE_INTERVAL_VALUES,
					null,semaphores.getSemWriteToMatrix(),this));
		}

		for(int i = 1; i <= MatrixConstants.FIREFIGHTERSAMOUNT; i++){
			firefighters.add(new Firefighter(i+MatrixConstants.FIREFIGHTERS_INTERVAL_VALUES,
					semaphores.getSemWriteToMatrix(),this));
		}

		for(int i = 1; i <= MatrixConstants.REFUGEE_AMOUNT; i++){
			listRefugees.add(new Refugee(i+MatrixConstants.REFUGEE_INTERVAL_VALUES,
					semaphores.getSemWriteToMatrix(),this));
		}

		logger.info("genereteFirstObjects() - end");

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

	void insertRefugee(Refugee refugee){
		logger.info("insertRefufee() - " + refugee.toString());
		matrix.insertRefugee(refugee);
		listeners.notifyRefugeeAddedListeners(refugee);
		logger.info("insertRefugee() - end");
	}
	
	
	public void cleanPosition(int x, int y,int threadId) {
		logger.info("cleanPosition()-x=" + x + ",y=" + y);
		matrix.cleanPosition(x, y);
		if(threadId >= 100 && threadId <= 200) {
			listeners.notifyFireRemovedListeners(x,y);
		}
		
		if(threadId >= 1000 && threadId <= 1999) {
			listeners.notifyFireFighterRemovedListeners(x, y);
		}

		if(threadId >= 201 && threadId <= 300){
			listeners.notifyRefugeeRemovedListeners(x, y);
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

	public int[] findF(Refugee refugee) {
		logger.info("findObjectaaaaaaaaa()-refugee-"+refugee.toString());
		int[] obj = matrix.findObjects(refugee.getX(), refugee.getY(), 1);

		if(obj[2] == refugee.getThreadId()) {
			obj[0] = 0;
			obj[1] = 0;
			obj[2] = 0;
		}

		logger.info("findObject()-object-end-x=" + obj[0] +",y="+obj[1]+",id="+obj[2]);
		return obj;
	}
	
	public int[] getNextStep(int x,int y) {
		logger.info("getNextStep()-x="+x+",y="+y);
		int[] obj = matrix.calculateNextStep(x, y);
		logger.info("getNextStep()-object-end-x=" + obj[0] +",y="+obj[1]);
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
