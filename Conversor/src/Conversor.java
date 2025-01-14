import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Conversor {

    // Inicializamos los escáneres para leer entradas de números y letras
    static Scanner numeros = new Scanner(System.in);
    static Scanner letras = new Scanner(System.in);

    static String rutaCarpetaSeleccionada;
    static File ficheroSeleccionado;
    static List<String> contenidoFichero = new ArrayList<>();


    public static void main(String[] args) {

        int opcionMenu;

        // Bucle para mostrar el menú principal hasta que el usuario elija salir
        do {
            opcionMenu = mostrarMenu();

            switch (opcionMenu) {
                case 1:
                    seleccionarCarpeta();
                    break;

                case 2:
                    lecturaFichero();
                    break;

                case 3:
                    conversionFichero();
                    break;

                case 4:
                    System.out.println("¡¡HASTA LA PRÓXIMA!!");
                    break;

                default:
                    System.out.println("Opcion incorrecta. Elija un número entre 1 y 4");

            }

        } while (opcionMenu != 4);
    }

    /**
     * Muestra el menú principal y permite al usuario elegir una opción.
     *
     * @return La opción seleccionada por el usuario.
     */
    private static int mostrarMenu() {

        int opcionMenu;

        System.out.println("""
                ******************** CONVERSOR DE FICHEROS ********************
                Menú principal:
                1. Seleccionar carpeta
                2. Lectura de fichero
                3. Conversión de fichero
                4. Salir""");

        System.out.println("Elija una opción: ");
        opcionMenu = numeros.nextInt();

        return opcionMenu;
    }

    /**
     * Permite al usuario seleccionar la carpeta donde se encuentran los ficheros.
     */
    public static void seleccionarCarpeta() {

        String rutaCarpeta;

        System.out.println("Introduce la ruta de la carpeta: ");
        rutaCarpeta = letras.next();

        //Convierto la ruta a un "objeto File" para poder utilizarla en los metodos
        File ruta = new File(rutaCarpeta);

        // Comprobamos si la carpeta introducida existe
        if (ruta.exists() && ruta.isDirectory()) {
            System.out.println("La carpeta introducida existe.");
            rutaCarpetaSeleccionada = rutaCarpeta;
            System.out.println("Carpeta seleccionada: " + rutaCarpetaSeleccionada);

            File[] ficheros = ruta.listFiles();

            // Mostramos los ficheros que contiene la carpeta
            if (ficheros != null) {
                for (File f : ficheros) {
                    System.out.println(f.getName());
                }
            } else {
                System.out.println("La carpeta introducida no contiene ficheros.");
            }
        } else {
            System.out.println("La carpeta introducida no existe.");
        }

    }

    /**
     * Permite al usuario seleccionar un fichero de la carpeta seleccionada.
     */
    public static void lecturaFichero() {

        // Comprobamos si se ha seleccionado una carpeta
        if (rutaCarpetaSeleccionada == null) {
            System.out.println("Primero debe introducir la ruta de una carpeta.");
            return;
        }

        String nombreFichero;

        System.out.println("Introduce el nombre del fichero que desea leer: ");
        nombreFichero = letras.next();

        //Convierto el fichero a un "objeto File" para poder utilizarlo en los metodos
        File fichero = new File(rutaCarpetaSeleccionada, nombreFichero);

        // Comprobamos si el fichero introducido existe
        if (fichero.exists() && fichero.isFile()) {
            contenidoFichero.clear();

            System.out.println("El fichero " + nombreFichero + " existe.");
            ficheroSeleccionado = fichero;

            try {
                BufferedReader bfr = new BufferedReader(new FileReader(fichero));

                // Lectura del fichero
                String linea;
                while ((linea = bfr.readLine()) != null) {
                    System.out.println(linea);
                    contenidoFichero.add(linea);
                }

                bfr.close();

            } catch (Exception e) {
                System.out.println("Ha ocurrido un error al leer el fichero.");
            }

            System.out.println("Fichero cargado correctamente.");
        } else {
            System.out.println("El fichero introducido no existe.");
        }

    }

    /**
     * Muestra el menú de extesiones y permite al usuario elegir una opción.
     *
     * @return La opción seleccionada por el usuario en el menú de extensiones.
     */
    private static int mostrarMenuExtensiones() {
        int extension;

        System.out.println("""
                ******************** EXTENSIONES ********************
                1. XML
                2. JSON
                3. CSV
                4. Volver al menú principal
                """);

        System.out.println("Elija un formato: ");
        extension = numeros.nextInt();

        return extension;
    }

    /**
     * Permite al usuario dar nombre a el fichero de salida y después seleccionar el formato de conversión.
     */
    public static void conversionFichero() {

        // Comprobamos si se ha seleccionado un fichero
        if (ficheroSeleccionado == null) {
            System.out.println("Debe seleccionar primero un fichero.");
            return;
        }

        System.out.print("Introduzca el nombre del fichero de salida (sin extensión): ");
        letras.nextLine(); // Para limpiar el buffer de la entrada
        String nombreFicheroSalida = letras.nextLine();

        int opcionExtension = mostrarMenuExtensiones();

        switch (opcionExtension) {
                case 1:
                    convertirAXML(nombreFicheroSalida);
                    break;

                case 2:
                    convertirAJSON(nombreFicheroSalida);
                    break;

                case 3:
                    convertirAQCSV(nombreFicheroSalida);
                    break;

                case 4:
                    System.out.println("¡¡HASTA LA PRÓXIMA!!");
                    break;

                default:
                    System.out.println("Opcion incorrecta. Elija un número entre 1 y 4");

            }
    }

    /**
     * Convierte el fichero seleccionado a formato CSV.
     *
     * @param nombreFicheroSalida El nombre del fichero de salida.
     */
    public static void convertirAQCSV(String nombreFicheroSalida) {

        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(nombreFicheroSalida + ".csv"));
             BufferedReader bfr = new BufferedReader(new FileReader(ficheroSeleccionado))) {

            String linea;
            while ((linea = bfr.readLine()) != null) {
                // Separamos la línea por espacios (o puedes usar otro delimitador)
                String[] palabras = linea.split("\\s+");  // Usamos \\s+ para separar por espacios o tabulaciones

                // Convertimos cada palabra en el arreglo, envolviéndola en comillas si contiene comas
                for (int i = 0; i < palabras.length; i++) {
                    if (palabras[i].contains(",")) {
                        palabras[i] = "\"" + palabras[i].replace("\"", "\"\"") + "\""; // Maneja comillas dentro de las palabras
                    }
                }

                // Unimos las palabras con comas
                String lineaCSV = String.join(",", palabras);

                // Escribimos la línea convertida en el archivo CSV
                bfw.write(lineaCSV);
                bfw.newLine();  // Añadimos un salto de línea
            }

            System.out.println("Conversión a CSV completada: " + nombreFicheroSalida + ".csv");
            mostrarArchivoConvertido(nombreFicheroSalida + ".csv");

        } catch (Exception e) {
            System.out.println("Error al escribir el fichero CSV.");
        }
    }

    /**
     * Convierte el fichero seleccionado a formato XML.
     *
     * @param nombreFicheroSalida El nombre del fichero de salida.
     */
    public static void convertirAXML(String nombreFicheroSalida) {
        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(nombreFicheroSalida + ".xml"));
             BufferedReader bfr = new BufferedReader(new FileReader(ficheroSeleccionado))) {

            bfw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bfw.write("<contenido>\n");

            String linea;
            while ((linea = bfr.readLine()) != null) {
                // Aquí se escapa el texto para evitar caracteres inválidos en XML
                bfw.write("  <linea>" + escapeXML(linea) + "</linea>\n");
            }

            bfw.write("</contenido>\n");
            System.out.println("Conversión a XML completada: " + nombreFicheroSalida + ".xml");
            mostrarArchivoConvertido(nombreFicheroSalida + ".xml");

        } catch (Exception e) {
            System.out.println("Error al escribir el fichero XML.");
        }
    }

    /**
     * Convierte ciertos caracteres en un formato especial para que sea válido y seguro dentro de un documento XML
     *
     * @param input El texto que se desea convertir.
     * @return El texto convertido para XML.
     */
    private static String escapeXML(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    /**
     * Convierte el fichero seleccionado a formato JSON.
     *
     * @param nombreFicheroSalida El nombre del fichero de salida.
     */
    public static void convertirAJSON(String nombreFicheroSalida) {
        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(nombreFicheroSalida + ".json"));
             BufferedReader bfr = new BufferedReader(new FileReader(ficheroSeleccionado))) {

            bfw.write("[\n");

            String linea;
            while ((linea = bfr.readLine()) != null) {
                // Cada línea se coloca en un objeto JSON
                bfw.write("  { \"linea\": \"" + escapeJSON(linea) + "\" }");
                if (bfr.ready()) {  // Si hay más líneas, agregamos una coma
                    bfw.write(",");
                }
                bfw.newLine();
            }

            bfw.write("]\n");
            System.out.println("Conversión a JSON completada: " + nombreFicheroSalida + ".json");
            mostrarArchivoConvertido(nombreFicheroSalida + ".json");


        } catch (Exception e) {
            System.out.println("Error al escribir el fichero JSON.");
        }
    }

    /**
     * Convierte ciertos caracteres en un formato especial para que sea válido y seguro dentro de un documento JSON
     *
     * @param input El texto que se desea convertir.
     * @return El texto convertido para JSON.
     */
    private static String escapeJSON(String input) {
        return input.replace("\"", "\\\"")
                .replace("\\", "\\\\")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Muestra el contenido del archivo convertido.
     *
     * @param nombreFicheroSalida El nombre del fichero a mostrar.
     */
    public static void mostrarArchivoConvertido(String nombreFicheroSalida) {
        try (BufferedReader bfr = new BufferedReader(new FileReader(nombreFicheroSalida))) {
            String linea;
            while ((linea = bfr.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + nombreFicheroSalida);
        }
    }
}
