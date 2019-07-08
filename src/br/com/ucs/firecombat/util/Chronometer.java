package br.com.ucs.firecombat.util;

public class Chronometer {
    private long begin;

    public void start(){
        begin = System.currentTimeMillis();
    }

    public long getTime() {
        return System.currentTimeMillis()-begin;
    }

    public String getMilliseconds() {
        return String.valueOf(System.currentTimeMillis()-begin);
    }

    public String getSeconds() {
        return String.valueOf((System.currentTimeMillis() - begin) / 1000.0);
    }

    public String getMinutes() {
        return String.valueOf((System.currentTimeMillis() - begin) / 60000.0);
    }

    public String getHours() {
        return String.valueOf((System.currentTimeMillis() - begin) / 3600000.0);
    }

}
