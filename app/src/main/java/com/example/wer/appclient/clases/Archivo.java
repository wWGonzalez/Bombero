package com.example.wer.appclient.clases;
import java.io.*;
import java.util.Scanner;
//import javax.swing.JOptionPane;

/**
 *
 * @author Luis
 */
public class Archivo {
    public static void fileEscribirAppend(String direccion, String hola){
        File nuevo = new File(direccion);
        try {

            if (nuevo.exists()) {
                FileWriter escribir = new FileWriter(nuevo,true);
                BufferedWriter er = new BufferedWriter(escribir);
                er.append(hola);
                er.newLine();
                //System.out.println("Se accedio a el archivo satisfactoriamente");
                er.close();
            }
            else if(!nuevo.exists()){
                nuevo.createNewFile();
                FileWriter escribir = new FileWriter(nuevo);
                BufferedWriter er = new BufferedWriter(escribir);
                er.write(hola);
                er.newLine();
                //System.out.println("Se creo y accedio al archivo satisfactoriamente");
                er.close();
            }
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Error al crear el archivo"+e.getMessage());
        }
    }

    public static void fileEscribir(String direccion, String hola){
        File nuevo = new File(direccion);
        try {

            if (nuevo.exists()) {
                FileWriter escribir = new FileWriter(nuevo);
                BufferedWriter er = new BufferedWriter(escribir);
                er.append(hola);
                er.newLine();
                //System.out.println("Se accedio a el archivo satisfactoriamente");
                er.close();
            }
            else if(!nuevo.exists()){
                nuevo.createNewFile();
                FileWriter escribir = new FileWriter(nuevo);
                BufferedWriter er = new BufferedWriter(escribir);
                er.write(hola);
                er.newLine();
                //System.out.println("Se creo y accedio al archivo satisfactoriamente");
                er.close();
            }
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Error al crear el archivo"+e.getMessage());
        }
    }

    public static String fileLeer(String direccion){
        String texto = null;
        try{
            File nuevo = new File(direccion);
            FileReader leer = new FileReader(nuevo);
            BufferedReader lr = new BufferedReader(leer);
            texto = lr.readLine();
        }
        catch(Exception e){
            //JOptionPane.showMessageDialog(null, "Error leyendo el fichero:\n"+e.getMessage());
        }
        return texto;
    }

    public static String fileLeerAll(String direccion){
        String texto = null;
        try{
            File nuevo = new File(direccion);
            FileReader leer = new FileReader(nuevo);
            BufferedReader lr = new BufferedReader(leer);
            Scanner s = null;
            s = new Scanner(nuevo);
            while(s.hasNextLine()){
                texto += (s.nextLine()+"\n");
            }
        }
        catch(Exception e){
            //JOptionPane.showMessageDialog(null, "Error leyendo el fichero:\n"+e.getMessage());
        }
        return texto;
    }

    public static void deleteFile(String direccion){
        File fichero = new File(direccion);
        try {
            fichero.delete();
        } catch (Exception e) {
            System.out.println("No se puedo eliminar el fihero: "+e.getMessage());
        }
    }
}
