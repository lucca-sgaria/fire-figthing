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

    private Semaphore semFire;

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
        this.semFire = new Semaphore(0);
    }

    public Fire(int threadId, AppMain main, Semaphore semWriteToMatrix, Enviroment enviroment) {
        this.threadId = threadId;
//		this.main = main;
        this.semWriteToMatrix = semWriteToMatrix;
        this.environment = enviroment;
        this.semFire = new Semaphore(0);

        try {
            this.semWriteToMatrix.acquire();
            while (true) {
                if (changeLocation()) break;
            }
            this.semWriteToMatrix.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                semFire.acquire();
                if (life == 0) {
                    sleep(3500);
                    semWriteToMatrix.acquire();
                    while (true) {
                        boolean inserted = changeLocation();
                        if (inserted) {
                            break;
                        }
                    }
                    setLife(5);
                    semWriteToMatrix.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    public void setSemFireRelease() {
        this.semFire.release();
    }

    public void setSemFireAcquire() {
        try {
            this.semFire.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void putOutFire(Firefighter firefighter) throws InterruptedException {
        logger.info("putOutFire-fire-" + this.toString());

        setLife(0);
        sleep(5000);
        environment.cleanPosition(getX(), getY(), getThreadId());
        firefighter.getSemFireFigther().release();
        semFire.release();

        logger.info("putOutFire-fire-end-" + this.toString());
    }


}
