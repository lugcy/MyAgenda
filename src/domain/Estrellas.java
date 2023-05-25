package domain;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.security.PrivateKey;
import java.util.Collection;
import java.util.HashSet;

public class Estrellas implements Serializable{
    private int matter;
    private int x;
    private int y;
    private int width;
    private final String path1 = "Resources/Estrella1.png";
    private final String path2 = "Resources/Estrella2.png";
    private int pos[] = {2, 35, 68};
    private Image estrella1;
    private Image estrella2;
    Collection<Image> estrellas = new HashSet<>();

    public Estrellas(int matter, int x, int y, int width) {
        this.matter = matter;
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public int getMatter() {return matter;}
    public void setMatter(int matter) {this.matter = matter;}
    public int getX() {return x;}
    public void setX(int x) {this.x = x;}
    public int getY() {return y;}
    public void setY(int y) {this.y = y;}
    public int getWidth() {return width;}
    public void setWidth(int width) {this.width = width;}

    public void pintar(Graphics g) throws IOException {
        for(int i=0; i<3; i++){
            if(i<matter)
                g.drawImage(ImageIO.read(new File(path1)), getX()+getWidth()-235, getY()+pos[i], null);
            else
                g.drawImage(ImageIO.read(new File(path2)), getX()+getWidth()-235, getY()+pos[i], null);
        }
    }
}
