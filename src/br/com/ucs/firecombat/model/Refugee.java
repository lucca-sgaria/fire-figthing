package br.com.ucs.firecombat.model;

import br.com.ucs.firecombat.constants.Params;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Semaphore;

public class Refugee extends Thread {

    private static final Logger logger =
            LogManager.getLogger(Fire.class);

    private int threadId;
    private int x;
    private int y;
    private int life = Params.REFUGEE_LIFE;

    private Semaphore semWriteToMatrix;
    private Enviroment environment;


    public Refugee(int threadId, int x, int y) {
        this.threadId = threadId;
        this.x = x;
        this.y = y;
    }

    public Refugee(int threadId, Semaphore semWriteToMatrix, Enviroment enviroment) {
        this.threadId = threadId;
        this.environment = enviroment;
        this.semWriteToMatrix = semWriteToMatrix;
    }


    @Override
    public void run(){

        for(;;){
            try {
                semWriteToMatrix.acquire();

                while (true){
                    if(firstLocation()) break;
                }

                sleep(5000);
                semWriteToMatrix.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }

    public boolean firstLocation(){
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

    @Override
    public String toString() {
        return "Refugee{" +
                "threadId=" + threadId +
                ", x=" + x +
                ", y=" + y +
                ", life=" + life +
                ", semWriteToMatrix=" + semWriteToMatrix +
                ", environment=" + environment +
                '}';
    }

}