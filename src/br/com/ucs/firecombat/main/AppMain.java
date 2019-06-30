package br.com.ucs.firecombat.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import br.com.ucs.firecombat.listener.FireAddedListener;
import br.com.ucs.firecombat.listener.FireRemovedListener;
import br.com.ucs.firecombat.model.Fire;

public class AppMain {

	public static int[][] MATRIX = new int[5][5];
	
	private List<FireAddedListener> addedListeners = new ArrayList<>();
	private List<FireRemovedListener> removedListeners = new ArrayList<>();
	
	private Semaphore semWriteToMatrix = new Semaphore(1);

//	public static void main(String[] args) throws InterruptedException {
//		startMain();
//	}

	public static void startMain() throws InterruptedException {
//		System.out.println("beggining");
//		Fire fireOne = new Fire(1,this);
//		fireOne.start();
//		fireOne.join();
	}

	public void start() throws InterruptedException {
		System.out.println("beggining");
		Fire fireOne = new Fire(1,this, semWriteToMatrix);
		Fire fireTwo = new Fire(2,this, semWriteToMatrix);
		Fire fireThree = new Fire(3,this, semWriteToMatrix);
		fireOne.start();
		fireTwo.start();
		fireThree.start();
	}

	public int generateRandom(int limit) {
		Random generator = new Random();
		int nextInt = generator.nextInt(limit);
		System.out.println("generateRandom()-generated " + nextInt);
		return nextInt;
	}

	public void insertFire(Fire fire) {
		System.out.println("insertFire()-goingtoinsert ,x=" + fire.getX() + ",y=" + fire.getY() + ",id=" + fire.getThreadId());
		MATRIX[fire.getX()][fire.getY()] = fire.getThreadId();
		notifyFireAddedListeners(fire);
	}
	
	public boolean existsIn(int x,int y) {
		for (int i = 0; i < MATRIX.length; i++) {
			for (int j = 0; j < MATRIX.length; j++) {
				if(i == x && j == y) {
					if(MATRIX[i][j] == 0) {
						return false;
					} else {
						System.out.println("ENCONTREI EXISTENTE");
						return true;
					}
				}
			}
		}
		return true;
	}

	public void printIn() {
		System.out.print("Matriz = ");
		for (int i = 0; i < MATRIX.length; i++) {
			System.out.println();
			for (int j = 0; j < MATRIX.length; j++) {
				System.out.print(MATRIX[i][j] + " ");
			}
		}
		System.out.println();
	}

	public void cleanPosition(int x, int y) {
		System.out.println("cleanPosition()-goingtoclean ,x=" + x + ",y=" + y);
		MATRIX[x][y] = 0;
		this.notifyFireRemovedListeners(x,y);
	}

	protected void notifyFireAddedListeners(Fire fire) {
		// Notify each of the listeners in the list of registered listeners
		this.addedListeners.forEach(listener -> listener.onFireAdded(fire));
	}
	
	protected void notifyFireRemovedListeners(int x,int y) {
		// Notify each of the listeners in the list of registered listeners
		this.removedListeners.forEach(listener -> listener.onFireRemoved(x,y));
	}
	
    public void registerFireAddedListener (FireAddedListener listener) {
        // Add the listener to the list of registered listeners
        this.addedListeners.add(listener);
    }
    
    public void registerFireRemovedListener (FireRemovedListener listener) {
        // Add the listener to the list of registered listeners
        this.removedListeners.add(listener);
    }


}
