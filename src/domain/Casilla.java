package domain;

import java.awt.*;
import java.io.Serializable;

public class Casilla implements Serializable {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean withLine;
    private Color color;
    static final int arc = 20;
    static final int line = 200;

    public Casilla(int x, int y, int width, int height, boolean withLine, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.withLine = withLine;
        this.color = color;
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
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public boolean isWithLine() {
        return withLine;
    }
    public void setWithLine(boolean withLine) {
        this.withLine = withLine;
    }
    public Color getColor() {return color;}
    public void setColor(Color color) {this.color = color;}

    public void pintar(Graphics g){
        g.setColor(color);
        g.drawRoundRect(x, y, width, height, arc, arc);
        if(isWithLine())
            g.drawLine(width-line+x, y, width-line+x, y+height);
    }
}
