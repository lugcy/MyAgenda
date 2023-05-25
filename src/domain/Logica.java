package domain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class Logica {
    static final int x = 20;
    public static int y = 150;
    static final int width = 1300-2*x;
    static final int height = 100;
    static boolean sortedBy = true;
    private String date;
    private Escribir esc1 = new Escribir(163, 60, "", Color.black, 40);
    private Escribir esc2 = new Escribir(110, 115, "", Color.black, 65);
    private HashMap<String, Escribir> esc = new HashMap<>();
    private Casilla c1;
    private Casilla c2;
    private Casilla c3;
    private Collection<Casilla> casillas = new HashSet<>();

    private SortedMap<Integer, Agenda> agendas = new TreeMap();
    private List<Object> taskToRemove = new ArrayList<>();

    public Logica(){
        casillas.add(new Casilla(80, 25, 300, 100, false, Color.blue));
        esc.put("Esc1", esc1);
        esc.put("Esc2", esc2);
        esc.put("MyAgenda", new Escribir(510, 105, "MyAgenda", Color.black, 70));

        c1 = new Casilla(1000, 25, 200, 45, false, Color.green);
        casillas.add(c1);
        esc.put("Anadir", new Escribir(1018, 57, "Añadir tarea", Color.green, 30));

        c2 = new Casilla(1000, 80, 200, 45, false, Color.red);
        casillas.add(c2);
        esc.put("Borrar", new Escribir(1018, 112, "Borrar tarea", Color.red, 30));

        c3 = new Casilla(1210, 25, 60, 100, false, Color.blue);
        casillas.add(c3);
        esc.put("Sorted", new Escribir(1232, 85, "S", Color.blue, 30));

        taskToRemove.add(false);
        taskToRemove.add(0);

        loadAgenda();
    }

    public void actualize(){
        Escribir esc1 = esc.get("Esc1");
        Escribir esc2 = esc.get("Esc2");

        LocalDate date = LocalDate.now();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
        esc1.setEsc(String.valueOf(formattedDate));

        LocalTime date2 = LocalTime.now();
        String hour = date2.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        esc2.setEsc(String.valueOf(hour));

        y = 150;

        for(Map.Entry<Integer, Agenda> a : agendas.entrySet()){
            a.getValue().replace(y, a.getValue().getCasilla().getHeight());
            a.getValue().getEst().setMatter(a.getValue().getMatter());
            y+=120;
            if(a.getValue().isDone())
                a.getValue().getCasilla().setColor(Color.green);
            else
                a.getValue().getCasilla().setColor(Color.red);
            if(a.getValue().getTiempoQueQueda() == "-24:00:00") {
                taskToRemove.set(0, true);
                taskToRemove.set(1, (int)a.getKey());
                y -= 120;
            }
        }
        if((boolean) taskToRemove.get(0)){
            removeTask((int) taskToRemove.get(1), agendas.size());
            taskToRemove.set(0, false);
        }
    }

    public void isPressed(char e){
        if(e == KeyEvent.VK_SPACE)
            anadirTarea();

        if (sortedBy)
            setSortedByDateOfFin();
        else
            setSortedByImportance();
    }
    public void mouseClick(int x, int y) throws IOException {
        if(x > c1.getX() && x < c1.getX()+c1.getWidth() && y > c1.getY() && y < c1.getY()+c1.getHeight()) {
            anadirTarea();
        }
        if(x > c2.getX() && x < c2.getX()+c2.getWidth() && y > c2.getY() && y < c2.getY()+c2.getHeight())
            JOptionPane.showMessageDialog(null, "Por favor, haga un click en la tarea que quiere borrar");
        if(x > c3.getX() && x < c3.getX()+c3.getWidth() && y > c3.getY() && y < c3.getY()+ c3.getHeight()) {
            if (sortedBy)
                sortedBy = false;
            else
                sortedBy = true;
        }

        for(Map.Entry<Integer, Agenda> a : agendas.entrySet()){
            Casilla c = a.getValue().getCasilla();
            if(x>c.getX()+c.getWidth()-200 && x<c.getX()+c.getWidth() && y>c.getY() && y<c.getY()+c.getHeight()) {
                if (a.getValue().isDone())
                    a.getValue().setDone(false);
                else
                    a.getValue().setDone(true);
            } else if(x>c.getX()+c.getWidth()-240 && x<c.getX()+c.getWidth() && y>c.getY() && y<c.getY()+c.getHeight()) {
                int matter = Integer.parseInt(JOptionPane.showInputDialog("Nota sobre 3 la importancia de su tarea:"));
                a.getValue().setMatter(matter);
            } else if(x>c.getX() && x<c.getX()+c.getWidth() && y>c.getY() &&y<c.getY()+c.getHeight()){
                if(JOptionPane.showConfirmDialog(null, "Quiere borrar esta tarea ?", "Confirmar",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    removeTask(a.getKey(), agendas.size());
                    y -= 120;
                    return;
                }
            }
        }
        saveAgenda();
        if (sortedBy)
            setSortedByDateOfFin();
        else
            setSortedByImportance();
    }

    public void anadirTarea(){
        Date date = new Date();

        String msg = JOptionPane.showInputDialog("Ponga el nombre de su tarea:");
        try {
            int day = Integer.parseInt(JOptionPane.showInputDialog("Ponga el dia de su tarea:"));
            int month = Integer.parseInt(JOptionPane.showInputDialog("Ponga el mes de su tarea:"));
            int year = Integer.parseInt(JOptionPane.showInputDialog("Ponga el año de su tarea:"));
            int hour = Integer.parseInt(JOptionPane.showInputDialog("Ponga la hora de su tarea:"));
            int min = Integer.parseInt(JOptionPane.showInputDialog("Ponga el minuto de su tarea:"));

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, 0);
            Date date2 = calendar.getTime();
            int matter = Integer.parseInt(JOptionPane.showInputDialog("Nota sobre 3 la importancia de su tarea:"));

            if (date2.getTime() > date.getTime()) {
                Casilla c = new Casilla(x, y, width, height, true, Color.black);
                Escribir e = new Escribir(c.getX() + 20, c.getY() + c.getHeight() - 40, msg, Color.black, 30);
                Estrellas est = new Estrellas(matter, c.getX(), c.getY(), c.getWidth());
                agendas.put(agendas.size(), new Agenda(c, e, date2.getTime(), matter, false, est));
            } else
                JOptionPane.showMessageDialog(null, "La fecha que ha indicado ya ha pasado");
        } catch (Exception e){
            System.out.println("We've got a problem with the form");
        }
    }
    public void loadAgenda(){
        File file = new File("Resources/agenda.obj");
        if(file.length()!=0) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                try {
                    int i = 0;
                    while (true) {
                        Agenda a = (Agenda) ois.readObject();
                        agendas.put(i, a);
                        i += 1;
                    }
                } catch (EOFException e) {
                    System.out.println("File already readed");
                }
                ois.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if(sortedBy)
            setSortedByDateOfFin();
        else
            setSortedByImportance();
    }
    public void saveAgenda(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("Resources/agenda.obj")));
            for(int i = 0; i<agendas.size(); i++){
                oos.writeObject(agendas.get(i));
            }
            oos.close();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void setSortedByDateOfFin(){
        for(int j = 0; j<agendas.size(); j++) {
            for (int i = 0; i < agendas.size() - 1; i++) {
                if (agendas.get(i).getFinDate() > agendas.get(i + 1).getFinDate()) {
                    Agenda a = agendas.get(i); Agenda b = agendas.get(i + 1);
                    agendas.put(i, b); agendas.put(i + 1, a);
                }
            }
        }
    }
     public void setSortedByImportance(){
        for(int j = 0; j<agendas.size(); j++){
            for(int i = 0; i < agendas.size() -1; i++){
                if(agendas.get(i).getMatter() < agendas.get(i+1).getMatter()
                        || (agendas.get(i).getMatter()==agendas.get(i+1).getMatter() && agendas.get(i).getFinDate()>agendas.get(i+1).getFinDate())){
                    Agenda a = agendas.get(i); Agenda b = agendas.get(i + 1);
                    agendas.put(i, b); agendas.put(i + 1, a);
                }
            }
        }
     }

     public void removeTask(int i, int size){
        for(int j = i+1; j<size; j++){
            agendas.put(j-1, agendas.get(j));
            agendas.remove(j);
        }
        saveAgenda();
     }

    public Collection<Casilla> getCasillas(){return casillas;}
    public HashMap<String, Escribir> listaMsg(){return esc;}
    public SortedMap<Integer, Agenda> getTasks(){return agendas;}
}
