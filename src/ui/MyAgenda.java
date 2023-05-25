package ui;

import domain.Logica;
import javax.swing.*;
import java.awt.*;

public class MyAgenda extends JFrame {

    private Fondo fondo;
    private Logica logica;

    public static void main(String[] args){
        new MyAgenda();
    }

    public MyAgenda(){
        super("MyAgenda");
        this.logica = new Logica();
        init();
        new Thread(fondo).start();
    }

    public void init(){
        fondo = new Fondo(this);
        JScrollPane scrollPane = new JScrollPane(fondo);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane, BorderLayout.CENTER);
        //this.add(fondo);
        this.pack();
        fondo.requestFocus();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    public Logica getLogica(){
        return logica;
    }
}
