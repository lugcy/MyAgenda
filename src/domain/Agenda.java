package domain;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Agenda implements Serializable{
    private Casilla casilla;
    private Escribir task;
    private long nowDate;
    private long finDate;
    private long date3;
    private int matter;
    private boolean done;
    private Estrellas est;
    private Escribir escribirDateOfFin;
    private Escribir tiempoQueQueda;
    public Agenda(Casilla casilla, Escribir task, long finDate, int matter, boolean done, Estrellas est){
        this.casilla = casilla;
        this.task = task;
        this.finDate = finDate;
        this.matter = matter;
        this.done = done;
        this.est = est;

        Date date = new Date(finDate);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date1 = formatter.format(date);
        escribirDateOfFin = new Escribir(casilla.getX()+casilla.getWidth()-170, casilla.getY()+casilla.getHeight()-60, date1, Color.black, 30);
        tiempoQueQueda = new Escribir(casilla.getX()+casilla.getWidth()-155, casilla.getY()+casilla.getHeight()-15, "", Color.black, 30);
    }

    public Casilla getCasilla() {
        return casilla;
    }
    public void setCasilla(Casilla casilla) {this.casilla = casilla;}
    public Escribir getTask() {
        return task;
    }
    public void setTask(Escribir task) {
        this.task = task;
    }
    public long getFinDate() {
        return finDate;
    }
    public void setFinDate(long finDate) {
        this.finDate = finDate;
    }
    public int getMatter() {
        return matter;
    }
    public void setMatter(int matter) {
        this.matter = matter;
    }
    public boolean isDone() {
        return done;
    }
    public void setDone(boolean done) {
        this.done = done;
    }
    public Estrellas getEst() {return est;}
    public void setEst(Estrellas est) {this.est = est;}
    public String getTiempoQueQueda(){return tiempoQueQueda.getEsc();}

    public void replace(int y, int w){
        casilla.setY(y);
        est.setY(y);
        escribirDateOfFin.setY(y+w-60);
        task.setY(y+w-40);
        tiempoQueQueda.setY(y+w-15);
    }

    public void actualizeTiempoQueQueda(){
        Calendar cal = Calendar.getInstance();
        long date = cal.getTimeInMillis();
        date3 = finDate-date;
        if(date3>=0) {
            int hour = (int) (date3 / (1000 * 3600));
            date3 -= hour * 1000 * 3600;
            int min = (int) (date3 / (1000 * 60));
            date3 -= min * 60 * 1000;
            int sec = (int) (date3 / 1000);
            String h = new String(String.valueOf(hour));
            String m = new String(String.valueOf(min));
            String s = new String(String.valueOf(sec));
            if (hour < 10)
                h = "0" + hour;
            if (min < 10)
                m = "0" + min;
            if (sec < 10)
                s = "0" + sec;
            String date4 = h + ":" + m + ":" + s;
            if (hour <= 23 && min <= 60 && sec <= 60)
                tiempoQueQueda.setEsc(date4);
            else
                tiempoQueQueda.setEsc("+24:00:00");
        }
        else {
            if(date3 < -2*24*3600*1000){
                tiempoQueQueda.setEsc("-24:00:00");
            }
            else {
                tiempoQueQueda.setEsc("00:00:00");
                Color darkgreen = new Color(0, 150, 0);
                this.casilla.setColor(darkgreen);
            }
        }
    }

    public void sortedByDateOfFin(){

    }

    public void pintar(Graphics g) throws IOException {
        task.pintar(g);
        casilla.pintar(g);
        escribirDateOfFin.pintar(g);
        tiempoQueQueda.pintar(g);
        est.pintar(g);
    }
}
