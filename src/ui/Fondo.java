package ui;

import domain.Logica;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Fondo extends JPanel implements Runnable {

    private MyAgenda myagenda;
    private Image background;

    public Fondo(MyAgenda myagenda){
        try{
            background = ImageIO.read(new File("Resources/background.png"));
        } catch (IOException e){
            System.out.println("Image not found");
        }

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                myagenda.getLogica().isPressed(e.getKeyChar());
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    myagenda.getLogica().mouseClick(e.getX(), e.getY());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        this.myagenda = myagenda;
        this.setPreferredSize(new Dimension(background.getWidth(null), 700));
    }

    public void ajustarTamano() {
        int height = 120 * myagenda.getLogica().getCasillas().size();
        this.setPreferredSize(new Dimension(background.getWidth(null), Logica.y+10));
        revalidate();
    }


    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);

        this.myagenda.getLogica().getCasillas().forEach(c->c.pintar(g));
        this.myagenda.getLogica().listaMsg().forEach((key, value) -> {value.pintar(g);});
        for(int i = 0; i<this.myagenda.getLogica().getTasks().size(); i++){
            try {
                this.myagenda.getLogica().getTasks().get(i).pintar(g);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run(){
        while(true){
            long tiempo1 = System.nanoTime();
            myagenda.getLogica().actualize();
            myagenda.getLogica().listaMsg();
            myagenda.getLogica().getTasks().forEach((key, value) -> {value.actualizeTiempoQueQueda();});
            ajustarTamano();
            double tiempoTotal = System.nanoTime() - tiempo1;
            repaint();
            try {
                double refreshRate = (1.0 / 240 * 1000);
                int tiempoSleep = (int) (refreshRate - tiempoTotal / 1000000);
                if (tiempoSleep < 0)
                    tiempoSleep = 0;
                Thread.sleep(tiempoSleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
