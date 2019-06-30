package br.com.ucs.firecombat.model;

import java.util.concurrent.Semaphore;

public class Semaphores {
	private Semaphore semWriteToMatrix;
	
	public void init() {
		semWriteToMatrix = new Semaphore(1);
	}

	public Semaphore getSemWriteToMatrix() {
		return semWriteToMatrix;
	}

	public void setSemWriteToMatrix(Semaphore semWriteToMatrix) {
		this.semWriteToMatrix = semWriteToMatrix;
	}
}
