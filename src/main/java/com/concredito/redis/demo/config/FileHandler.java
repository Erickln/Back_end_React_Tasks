package com.concredito.redis.demo.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileHandler {
    // Método estático para añadir un String a un archivo .txt
    public static void addStringToFile(String content) {
        // Nombre del archivo y ruta
        String fileName = "archivo.txt";

        try {
            // Crear un FileWriter con el nombre del archivo
            FileWriter fileWriter = new FileWriter(fileName, true);

            // Crear un PrintWriter para escribir en el archivo
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Escribir el contenido en el archivo
            printWriter.println(content);

            // Cerrar el PrintWriter
            printWriter.close();

            System.out.println("Se añadió el contenido al archivo correctamente.");

        } catch (IOException e) {
            System.out.println("Error al añadir el contenido al archivo: " + e.getMessage());
        }
    }

    public static String readFile() {
        String fileName = "archivo.txt";
        String content = "";
        try {
            // Crear un FileReader con el nombre del archivo
            FileReader fileReader = new FileReader(fileName);

            // Crear un BufferedReader para leer el archivo
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Leer el contenido del archivo
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content += line + "\n";
            }

            // Cerrar el BufferedReader
            bufferedReader.close();

            System.out.println("Se leyó el contenido del archivo correctamente.");

        } catch (IOException e) {
            System.out.println("Error al leer el contenido del archivo: " + e.getMessage());
        }
        return content;

    }

    public static void main(String[] args) {
        // Ejemplo de uso del método addStringToFile
        System.out.println(readFile());
    }
}
