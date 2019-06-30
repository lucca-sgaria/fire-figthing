package br.com.ucs.firecombat.model;

import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.ucs.firecombat.main.AppMain;

public class Fire extends Thread {
	
	private static final Logger logger = 
			LogManager.getLogger(Fire.class);

	private int life = 5;
	private int threadId;
	private int x;
	private int y;
	
//	private AppMain main;
	
	private Semaphore semWriteToMatrix;
	
	private Enviroment environment;

	public Fire(int threadId, int x, int y) {
		this.threadId = threadId;
		this.x = x;
		this.y = y;
	}

	public Fire(int threadId, AppMain main, Semaphore semWriteToMatrix) {
		this.threadId = threadId;
//		this.main = main;
		this.semWriteToMatrix = semWriteToMatrix;
	}
	
	public Fire(int threadId, AppMain main, Semaphore semWriteToMatrix,Enviroment enviroment) {
		this.threadId = threadId;
//		this.main = main;
		this.semWriteToMatrix = semWriteToMatrix;
		this.environment = enviroment;
	}

	@Override
	public void run() {
		try {
		logger.info("run()-"+this.toString());
		int iterations = 1;
		
		while (true) {
			logger.info("run()-iteration-"+iterations+"-"+this.toString());
			if(life == 0 || iterations == 1) {
				System.out.println("run()-"+threadId + " is waiting for a permit."); 
				semWriteToMatrix.acquire();
				System.out.println("run()-"+threadId + " gets a permit."); 
				
				if(iterations > 1) {
					environment.cleanPosition(x, y);
					life = 5;
					logger.info("run()-life restored-"+this.toString());
				}
				while (true) {
					boolean inserted = changeLocation();
					if (inserted) {
						break;
					}
				}
				sleep(5000);
				logger.info("run()-thread-" +threadId + " releases the permit.");
				semWriteToMatrix.release();
			}
			life--;
			iterations++;
			sleep(5000);
		}
		
		
//			int i = 0;
//
//			while (i < 5) {
//				System.out.println("=======================================");
//				System.out.println("Threadid = " +threadId+" running " + i);
//				boolean inserted = false;
//				System.out.println(threadId + " is waiting for a permit."); 
//				semWriteToMatrix.acquire();
//				System.out.println(threadId + " gets a permit."); 
//				while (true) {
//					inserted = changeLocation();
//					
//					if (inserted) {
//						break;
//					}
//				}
//				
//				System.out.println(threadId + " releases the permit.");
//				semWriteToMatrix.release();
//				System.out.println("Fire " + threadId + " burning on x = " + x + ", y= " + y + ", iteration=" + i);
//				
//
//				main.printIn();
//				try {
//					sleep(5000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				main.cleanPosition(x, y);
//				
//				i++;
//				System.out.println("=======================================");
//			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("end ThreadId= " + threadId);
	}

	public boolean changeLocation() throws InterruptedException {
		int x = environment.generateRandom();
		int y = environment.generateRandom();

		if (!environment.existsIn(x, y)) {
			this.x = x;
			this.y = y;
			environment.insertFire(this);
			return true;
		} else {
			System.out.println("Fire already in x=" + x + ";y=" + y);
			return false;
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	
	@Override
	public String toString() {
		return "Fire [life=" + life + ", threadId=" + threadId + ", x=" + x + ", y=" + y + ", semWriteToMatrix="
				+ semWriteToMatrix + ", environment=" + environment + "]";
	}

	public Enviroment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Enviroment environment) {
		this.environment = environment;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	

}
