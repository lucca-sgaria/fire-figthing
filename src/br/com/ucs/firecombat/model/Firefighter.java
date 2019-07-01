package br.com.ucs.firecombat.model;

import java.util.concurrent.Semaphore;

import br.com.ucs.firecombat.constants.FirefighterState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Firefighter extends Thread {
	private static final Logger logger = LogManager.getLogger(Firefighter.class);

	private int threadId;
	private int x;
	private int y;
	private int state = FirefighterState.SEARCHING;

	private Semaphore semWriteToMatrix;
	private Enviroment environment;

	public Firefighter(int threadId, int x, int y, Semaphore semWriteToMatrix) {
		this.threadId = threadId;
		this.x = x;
		this.y = y;
		this.semWriteToMatrix = semWriteToMatrix;
	}

	public Firefighter(int threadId, Semaphore semWriteToMatrix, Enviroment environment) {
		this.threadId = threadId;
		this.semWriteToMatrix = semWriteToMatrix;
		this.environment = environment;
	}

	@Override
	public void run() {
		try {
			logger.info("run()-" + this.toString());
			int iteraction = 1;
			while (true) {
				System.out.println("iteraction " + iteraction);
				if (state == FirefighterState.SEARCHING) {
					System.out.println("run()-firefighter-" + threadId + " is waiting for a permit.");
					semWriteToMatrix.acquire();
					System.out.println("run()-" + threadId + " gets a permit.");
					// espera permissao
					// na 1 procura lugar
					// libera

					while (true) {
						if(iteraction == 1) {
							boolean firstLocation = firstLocation();
							if (firstLocation) {
								break;
							}
						} else {
							int[] nextStep = environment.getNextStep(x, y);
							if(nextStep[0] != -1) {
								environment.cleanPosition(x, y, threadId);
								this.x = nextStep[0];
								this.y = nextStep[1];
								environment.insertFireFighter(this);
								break;
							}
						}
					}
					// procura fogo
					// se achou muda estado
					int[] obj = environment.findObject(this);
					System.out.println("obj " + obj[2]);
					if (obj[2] == 0) {
						// n achou nada
						// procura um lugar
					} else if (obj[2] >= 1 && obj[2] <= 100) {
						// achou vitima
					} else if (obj[2] >= 101 && obj[2] <= 200) {
						// achou fogo
						Fire findFire = environment.findFire(obj[2]);
						findFire.putOutFire();
					}
					sleep(5000);
					logger.info("run()-thread-" + threadId + " releases the permit.");
					semWriteToMatrix.release();
				}

				// se estado == pronto pra apagar
				// comeca apagar fogo (a cada iteracao)
				// ainda n implementado
				iteraction++;
			}
		} catch (Exception e) {
		}

	}

	public boolean firstLocation() throws InterruptedException {
		int x = environment.generateRandom();
		int y = environment.generateRandom();

		if (!environment.existsIn(x, y)) {
			this.x = x;
			this.y = y;
//			getEnvironment().insertFire(this);
			environment.insertFireFighter(this);
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

	public void setEnvironment(Enviroment environment) {
		this.environment = environment;
	}

	@Override
	public String toString() {
		return "Firefighter [threadId=" + threadId + ", x=" + x + ", y=" + y + ", state=" + state
				+ ", semWriteToMatrix=" + semWriteToMatrix + ", environment=" + environment + "]";
	}

}
