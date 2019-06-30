package br.com.ucs.firecombat.model;

import java.util.concurrent.Semaphore;

import br.com.ucs.firecombat.main.AppMain;

public class Fire extends Thread {

	private int threadId;
	private int x;
	private int y;
	private AppMain main;
	private Semaphore semWriteToMatrix;

	public Fire(int threadId, int x, int y) {
		this.threadId = threadId;
		this.x = x;
		this.y = y;
	}

	public Fire(int threadId, AppMain main, Semaphore semWriteToMatrix) {
		this.threadId = threadId;
		this.main = main;
		this.semWriteToMatrix = semWriteToMatrix;
	}

	@Override
	public void run() {
		try {
			int i = 0;

			while (i < 5) {
				System.out.println("=======================================");
				System.out.println("Threadid = " +threadId+" running " + i);
				boolean inserted = false;
				System.out.println(threadId + " is waiting for a permit."); 
				semWriteToMatrix.acquire();
				System.out.println(threadId + " gets a permit."); 
				while (true) {
					inserted = changeLocation();
					
					if (inserted) {
						break;
					}
				}
				System.out.println(threadId + " releases the permit.");
				semWriteToMatrix.release();
				System.out.println("Fire " + threadId + " burning on x = " + x + ", y= " + y + ", iteration=" + i);
				

//				main.printIn();
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				main.cleanPosition(x, y);
				
				i++;
				System.out.println("=======================================");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end ThreadId= " + threadId);
	}

	public boolean changeLocation() throws InterruptedException {
		int x = main.generateRandom(5);
		int y = main.generateRandom(5);

		if (!main.existsIn(x, y)) {
			this.x = x;
			this.y = y;
			main.insertFire(this);
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

}
