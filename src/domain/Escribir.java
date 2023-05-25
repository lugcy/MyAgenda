package domain;

import java.awt.*;
import java.io.Serializable;

public class Escribir implements Serializable{
    private int x;
    private int y;
    private String esc;
    private Color color;
    private int size;

    public Escribir(int x, int y, String esc, Color color, int size) {
        this.x = x;
        this.y = y;
        this.esc = esc;
        this.color = color;
        this.size = size;
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
    public String getEsc() {
        return esc;
    }
    public void setEsc(String esc) {
        this.esc = esc;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {this.color = color;}
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void pintar(Graphics g){
        g.setColor(color);
        g.setFont(new Font("Times New Roman", Font.BOLD, size));
        g.drawString(String.valueOf(esc), x, y);
    }
}
