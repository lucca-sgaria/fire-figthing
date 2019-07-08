package br.com.ucs.firecombat.model;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.ucs.firecombat.constants.Params;

public class Refugee extends Thread {
	private static final Logger logger = LogManager.getLogger(Refugee.class);

	private int threadId;
	private int x;
	private int y;
	private int life = Params.REFUGEE_LIFE;
	private int stateRefugee = Params.ALIVE;
	private Semaphore semWriteToMatrix;
	private Enviroment environment;

	private Semaphore semRefugee;

	public Refugee(int threadId, int x, int y) {
		this.threadId = threadId;
		this.x = x;
		this.y = y;
		this.semRefugee = new Semaphore(1);
	}

	public Refugee(int threadId, Semaphore semWriteToMatrix, Enviroment enviroment) {
		this.threadId = threadId;
		this.environment = enviroment;
		this.semWriteToMatrix = semWriteToMatrix;
		this.semRefugee = new Semaphore(1);

//		try {
//			this.semWriteToMatrix.acquire();
//			while (true) {
//				if (firstLocation())
//					break;
//			}
//			this.semWriteToMatrix.release();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

//    @Override
//    public void run(){
//        for(;;){
//            try {
//                // TODO: 30/06/19 metodo para andar aleatoriamente
//                semWriteToMatrix.acquire();
//
//                for(int i = 1; i <= MatrixConstants.REFUGEE_AMOUNT; i++){
//
//                    //semWriteToMatrix.acquire();
//
//                    environment.cleanPosition(getX(),getY(),MatrixConstants.REFUGEE_INTERVAL_VALUES + i);
//                    while (true){
//                        int[] nextStep = environment.getNextStep(x, y);
//                        if(nextStep[0] != -1) {
//                            environment.cleanPosition(x, y, threadId);
//                            this.x = nextStep[0];
//                            this.y = nextStep[1];
//                            environment.insertRefugee(this);
//                            break;
//                        }
//                    }
//
//                    // procura fogo
//                    // se achou muda estado
//                    int[] obj = environment.findF(this);
//                    if(obj[2] >= 1 && obj[2] <=100){
//                        System.out.println("está pegando fogo");
//                    }
//
//
//
//                }
//                semWriteToMatrix.release();
//                sleep(3000);
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

	@Override
	public void run() {
		int iteracion = 1;
		try {
			while (true) {
				semRefugee.acquire();
				System.out.println("run()-refugee-" + threadId + " is waiting for a permit.");
				semWriteToMatrix.acquire();
				System.out.println("run()-" + threadId + " gets a permit.");

				while (true) {
					if (iteracion == 1) {
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
							environment.insertRefugee(this);
							break;
						}
					}
				}

				if (this.stateRefugee == Params.ALIVE) {
					int[] findF = environment.findF(this);
					if (findF[2] > 0) {
						this.life--;
						this.stateRefugee = Params.VICTIM;
						environment.turnIntoVictim(this);
					}
				} else if (this.stateRefugee == Params.VICTIM) {
					life--;
				}

				if (life == 0) {
					environment.cleanPosition(x, y, threadId);
					semWriteToMatrix.release();
					break;
				}
				semWriteToMatrix.release();
				semRefugee.release();
				sleep(600);
				iteracion++;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("dead");
	}

	public boolean firstLocation() {
		int x = environment.generateRandom();
		int y = environment.generateRandom();

		if (!environment.existsIn(x, y)) {
			this.x = x;
			this.y = y;

			environment.insertRefugee(this);
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

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getStateRefugee() {
		return stateRefugee;
	}

	public void setStateRefugee(int stateRefugee) {
		this.stateRefugee = stateRefugee;
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

	public Semaphore getSemRefugee() {
		return semRefugee;
	}

	public void setSemRefugee(Semaphore semRefugee) {
		this.semRefugee = semRefugee;
	}

	@Override
	public String toString() {
		return "Refugee [threadId=" + threadId + ", x=" + x + ", y=" + y + ", life=" + life + ", stateRefugee="
				+ stateRefugee + ", semWriteToMatrix=" + semWriteToMatrix + ", environment=" + environment + "]";
	}

	public void save() throws InterruptedException {
		logger.info("save-fire-" + this.toString());
		sleep(5000);
		stateRefugee = Params.ALIVE;
		turnIntoVictim();
		sleep(2000);
		logger.info("save-fire-end" + this.toString());
	}

	protected void turnIntoVictim() {
		this.environment.turnIntoVictim(this);
	}

}
