package br.com.ucs.firecombat.model;

import java.util.concurrent.Semaphore;

import br.com.ucs.firecombat.constants.FirefighterState;
import br.com.ucs.firecombat.constants.MatrixConstants;
import br.com.ucs.firecombat.constants.Params;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Firefighter extends Thread {
	private static final Logger logger = LogManager.getLogger(Firefighter.class);

	private int threadId;
	private int x;
	private int y;
	private int stateFighter = FirefighterState.SEARCHING;

	private Semaphore semFireFigther;



	private Semaphore semWriteToMatrix;
	private Enviroment environment;
	private Refugee victim;

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
		this.semFireFigther = new Semaphore(0);
	}

	@Override
	public void run() {
		try {
			logger.info("run()-" + this.toString());
			int iteraction = 1;
			while (true) {
				System.out.println("iteraction " + iteraction);
				System.out.println("run()-firefighter-" + threadId + " is waiting for a permit.");
				semWriteToMatrix.acquire();
				System.out.println("run()-" + threadId + " gets a permit.");
				// espera permissao
				// na 1 procura lugar
				// libera

				while (true) {
					if (iteraction == 1) {
						boolean firstLocation = firstLocation();
						if (firstLocation) {
							break;
						}
					} else {
						int[] nextStep = environment.getNextStep(x, y);
						if (nextStep[0] != -1) {
							environment.cleanPosition(x, y, threadId);
							this.x = nextStep[0];
							this.y = nextStep[1];
							environment.insertFireFighter(this);
							
							break;
						}
					}
				}
				semWriteToMatrix.release();
				// procura fogo
				// se achou muda estado
				if (getStateFighter() == FirefighterState.SEARCHING) {
					int[] obj = environment.findObject(this);
					System.out.println(
							"ESSE OBJETO " + obj[2] + "-" + (obj[2] >= MatrixConstants.REFUGEE_INTERVAL_VALUES_MIN
									&& obj[2] <= MatrixConstants.REFUGEE_INTERVAL_VALUES_MAX));
					if (obj[2] >= MatrixConstants.FIRE_INTERVAL_VALUES_MIN
							&& obj[2] <= MatrixConstants.FIRE_INTERVAL_VALUES_MAX) {
						// achou fogo
						Fire findFire = environment.findFire(obj[2]);
						findFire.putOutFire(this);
						// bloqueou a thread (esperando o fogo apagar)
						this.semFireFigther.acquire();
					} else if (obj[2] >= MatrixConstants.REFUGEE_INTERVAL_VALUES_MIN
							&& obj[2] <= MatrixConstants.REFUGEE_INTERVAL_VALUES_MAX) {
						System.out.println("Killin victim");
						// achou vitima
						Refugee refugee = this.environment.findRefugee(obj[2]);
						if (refugee.getStateRefugee() == Params.VICTIM) {
							System.out.println("Killin victim IS IN REALITY A VICTIM");
							refugee.getSemRefugee().acquire();
							sleep(2000);
							environment.cleanPosition(refugee.getX(), refugee.getY(), refugee.getThreadId());
							this.stateFighter = FirefighterState.WITHVICTIM;
							this.victim = refugee;
							environment.getListeners().notifyFirefighterWithVictim(this);
//							refugee.save();
//                        	refugee.getSemRefugee().release();
						}
					}
				} else {
					System.out.println("HERE BIATCHES " + iteraction);
					int[] obj = environment.findParamedics(this);
					if(obj[2] >= MatrixConstants.PARAMEDICS_INTERVAL_VALUES_MIN && obj[2] <=
							MatrixConstants.PARAMEDICS_INTERVAL_VALUES_MAX ) {

						System.out.println("FOUNDEI PARAMEDICS");
						Paramedics findParamedics = environment.findParamedics(obj[2]);
						findParamedics.setVictim(victim);
						findParamedics.setFirefighter(this);


						// TODO: 08/07/19 teria que esperar paramedics liberar para depois o firefighter liberar
						findParamedics.getSemParamedics().release();

						this.stateFighter = FirefighterState.SEARCHING;
						victim = null;

						semFireFigther.acquire();
					}
				}
				sleep(600);
				logger.info("run()-thread-" + threadId + " releases the permit.");

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

//	public void setSemFireFigtherRelease() {
//		this.semFireFigther.release();
//	}

	public void setSemFireFigtherAcquire() {
		try {
			this.semFireFigther.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setEnvironment(Enviroment environment) {
		this.environment = environment;
	}

	@Override
	public String toString() {
		return "Firefighter [threadId=" + threadId + ", x=" + x + ", y=" + y + ", state=" + getStateFighter()
				+ ", semWriteToMatrix=" + semWriteToMatrix + ", environment=" + environment + "]";
	}

	public Semaphore getSemFireFigther() {
		return semFireFigther;
	}

	public int getStateFighter() {
		return stateFighter;
	}

	public void setStateFighter(int stateFighter) {
		this.stateFighter = stateFighter;
	}

	public Refugee getVictim() {
		return victim;
	}

	public void setVictim(Refugee victim) {
		this.victim = victim;
	}

}
