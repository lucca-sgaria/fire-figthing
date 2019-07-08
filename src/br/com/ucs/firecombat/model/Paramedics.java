package br.com.ucs.firecombat.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.ucs.firecombat.constants.Params;

import java.util.concurrent.Semaphore;

public class Paramedics extends Thread {
	private static final Logger logger = LogManager.getLogger(Firefighter.class);

	private int threadId;
	private int x;
	private int y;

	private Semaphore semWriteToMatrix;
	private Enviroment environment;

	private Refugee victim;
	private Firefighter firefighter;
	private Semaphore semParamedics;
	private Semaphore mutexParamedics;

	public Paramedics(int threadId, Semaphore semWriteToMatrix, Enviroment environment) {
		this.threadId = threadId;
		this.semWriteToMatrix = semWriteToMatrix;
		this.environment = environment;
		this.semParamedics = new Semaphore(0);
		this.mutexParamedics = new Semaphore(1);


		try {
			this.semWriteToMatrix.acquire();
			while (true) {
				if (firstLocation())
					break;
			}
			this.semWriteToMatrix.release();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



    @Override
	public void run() {
		try {
			while (true) {
				System.out.println("waiting");

				semParamedics.acquire();
				//libera o firefighter q estava esperando o antendimento
				firefighter.getSemFireFigther().release();

				System.out.println("paramedics running");
				environment.getListeners().notifyParamedicsWithVictim(getX(),getY());
				sleep(5000);
				victim.setStateRefugee(Params.ALIVE);

				//thread para a escrita na matriz
				semWriteToMatrix.acquire();
				victim.firstLocation();
				semWriteToMatrix.release();

				//libera thread do refugee
				victim.getSemRefugee().release();
				victim = null;
				environment.getListeners().notifyParamedicsAddedListeners(this);

				//permite entrar de novo
				mutexParamedics.release();
//				semParamedics.release();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean firstLocation() {
		int x = environment.generateRandom();
		int y = environment.generateRandom();

		if (!environment.existsIn(x, y)) {
			this.x = x;
			this.y = y;

			environment.insertParamedics(this);
			return true;
		} else {
			System.out.println("Something already in x=" + x + ";y=" + y);
			return false;
		}

	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
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

	public Semaphore getSemWriteToMatrix() {
		return semWriteToMatrix;
	}

	public void setSemWriteToMatrix(Semaphore semWriteToMatrix) {
		this.semWriteToMatrix = semWriteToMatrix;
	}

	public Enviroment getEnvironment() {
		return environment;
	}

	public Firefighter getFirefighter() {
		return firefighter;
	}

	public void setFirefighter(Firefighter firefighter) {
		this.firefighter = firefighter;
	}

	public void setEnvironment(Enviroment environment) {
		this.environment = environment;
	}

	@Override
	public String toString() {
		return "Paramedics{" + "threadId=" + threadId + ", x=" + x + ", y=" + y + ", semWriteToMatrix="
				+ semWriteToMatrix + ", environment=" + environment + '}';
	}

	public Refugee getVictim() {
		return victim;
	}

    public Semaphore getMutexParamedics() {
        return mutexParamedics;
    }

	public void setVictim(Refugee victim) {
		this.victim = victim;
	}

	public Semaphore getSemParamedics() {
		return semParamedics;
	}

	public void setSemParamedics(Semaphore semParamedics) {
		this.semParamedics = semParamedics;
	}

}
