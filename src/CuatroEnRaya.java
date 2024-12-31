import java.util.Scanner;

public class CuatroEnRaya {

    // Inicializamos los escáneres para leer entradas de números y letras
    static Scanner numeros = new Scanner(System.in);
    static Scanner letras = new Scanner(System.in);
    static int modalidadSeleccionada = 0;

    static String colorJugador1;
    static String colorJugador2;

    static int victoriasJugador1 = 0;
    static int victoriasJugador2 = 0;
    static int empates = 0;
    static int numeroDeRondas = 1; // Valor por defecto

    static int ordenSalida = 1; // 1: Aleatorio, 2: Ganador, 3: Perdedor, 4: Siempre Jugador 1


    public static void main(String[] args) {

        int opcionMenu;

        // Bucle para mostrar el menú principal hasta que el usuario elija salir
        do {
            opcionMenu = mostrarMenu();

            switch (opcionMenu) {
                case 1:
                    elegirModalidad();
                    break;

                case 2:
                    iniciarJuego();
                    break;

                case 3:
                    mostrarConfiguracion();
                    break;

                case 4:
                    mostrarCreditos();
                    break;

                case 5:
                    mostrarInstrucciones();
                    break;

                case 6:
                    System.out.println("HAS ELEGIDO: Salir");
                    break;

                default:
                    System.out.println("Opcion incorrecta. Eliga un número entre 1 y 6");

            }

        } while (opcionMenu != 6);
    }

    /**
     * Muestra el menú principal y permite al usuario elegir una opción.
     *
     * @return La opción seleccionada por el usuario.
     */
    private static int mostrarMenu() {

        int opcionMenu;

        System.out.println("""
                ******************** CUATRO EN RAYA ********************
                Menú principal:
                1. Elegir modalidad
                2. Iniciar juego
                3. Configuración
                4. Créditos
                5. Instrucciones
                6. Salir""");

        System.out.println("Elija una opción: ");
        opcionMenu = numeros.nextInt();

        return opcionMenu;
    }

    /**
     * Permite al usuario elegir la modalidad de juego (contra la IA o contra otro jugador humano).
     */
    public static void elegirModalidad() {

        int modalidad;

        System.out.println("""
                ******************** ELEGIR MODALIDAD ********************
                1. Contra la IA
                2. Contra Humano
                """);

        System.out.println("Elija una modalidad: ");
        modalidad = numeros.nextInt();

        switch (modalidad) {
            case 1:
                System.out.println("Contra la IA");
                modalidadSeleccionada = 1;
                break;
            case 2:
                System.out.println("Contra un Humano");
                modalidadSeleccionada = 2;
                break;
            default:
                System.out.println("Opcion incorrecta.");
        }

    }

    /**
     * Inicia el juego y maneja las rondas, turnos y condiciones de victoria.
     * Juega hasta que el número de rondas configurado es alcanzado.
     */
    public static void iniciarJuego() {

        int rondasJugadas = 0;
        int victoriasJugador1Serie = 0;
        int victoriasJugador2Serie = 0;

        // Bucle para jugar las rondas hasta llegar al número de rondas configurado
        do {
            System.out.println("Iniciando ronda " + (rondasJugadas + 1) + " de " + numeroDeRondas);

            //Creación del tablero

            char[][] tablero = new char[6][7];

            // Inicialización del tablero vacío
            for (int fila = 0; fila < tablero.length; fila++) {
                for (int columna = 0; columna < tablero[fila].length; columna++) {
                    tablero[fila][columna] = '.';
                }
            }

            char[] fichas = {'X', 'O'};

            // Determinar quién empieza según la configuración de ordenSalida
            int turno = determinarOrdenSalida();

            while (true) {

                mostrarTablero(tablero);
                System.out.println("Jugador " + (turno + 1) + " (" + fichas[turno] + "), elige una columna (0-6): ");
                int columna = numeros.nextInt(); // Lee la columna donde el jugador desea colocar la ficha

                // Validación del movimiento (columna válida y espacio disponible)
                if (columna < 0 || columna >= 7 || !colocarFicha(tablero, columna, fichas[turno])) {
                    System.out.println("Movimiento inválido, intenta de nuevo.");
                }

                // Verificar si hay ganador
                if (hayGanador(tablero, fichas[turno])) {
                    mostrarTablero(tablero);
                    System.out.println("¡Jugador " + (turno + 1) + " gana!");
                    if (turno == 0) {
                        victoriasJugador1++; // Incrementa la victoria del jugador 1
                        victoriasJugador1Serie++;
                    } else {
                        victoriasJugador2++; // Incrementa la victoria del jugador 2
                        victoriasJugador2Serie++;
                    }
                    break; // Termina la ronda si hay un ganador
                }

                // Verificar si el tablero está lleno
                if (tableroLleno(tablero)) {
                    mostrarTablero(tablero);
                    System.out.println("¡Empate! No hay más movimientos posibles.");
                    empates++;
                    break; // Termina la ronda en empate
                }

                // Cambiar turno entre los jugadores
                turno = 1 - turno; // Alternar entre 0 y 1

                rondasJugadas++;

            }

            // Muestra los resultados después de cada ronda
            System.out.println("Resultados hasta ahora:");
            System.out.println("Jugador 1: " + victoriasJugador1 + " victorias");
            System.out.println("Jugador 2: " + victoriasJugador2 + " victorias");
            System.out.println("Empates: " + empates);

        } while (rondasJugadas < numeroDeRondas);

        // Muestra el resultado final de la serie
        if (victoriasJugador1Serie > victoriasJugador2Serie) {
            System.out.println("¡Jugador 1 es el vencedor de esta serie con " + victoriasJugador1Serie + " victorias!");
        } else if (victoriasJugador2Serie > victoriasJugador1Serie) {
            System.out.println("¡Jugador 2 es el vencedor de esta serie con " + victoriasJugador2Serie + " victorias!");
        } else {
            System.out.println("La serie termina en empate con ambos jugadores ganando " + victoriasJugador1Serie + " partidas.");
        }
    }

    /**
     * Muestra el tablero de juego en consola.
     *
     * @param tablero El tablero de juego, representado como una matriz bidimensional de caracteres.
     */
    public static void mostrarTablero(char[][] tablero) {
        for (int fila = 0; fila < tablero.length; fila++) {
            for (int columna = 0; columna < tablero[fila].length; columna++) {
                System.out.print(tablero[fila][columna] + " ");
            }
            System.out.println(); // Nueva línea por cada fila
        }
    }

    /**
     * Coloca una ficha en la columna especificada del tablero.
     * El lugar de la ficha se determina buscando la primera fila vacía en la columna.
     *
     * @param tablero El tablero de juego.
     * @param columna La columna en la que se coloca la ficha.
     * @param ficha El carácter que representa la ficha (X u O).
     * @return true si el movimiento fue válido (la ficha se colocó correctamente), false si la columna está llena.
     */
    public static boolean colocarFicha(char[][] tablero, int columna, char ficha) {
        for (int i = tablero.length - 1; i >= 0; i--) { // Empezamos desde la última fila
            if (tablero[i][columna] == '.') {
                tablero[i][columna] = ficha; // Coloca la ficha en la primera fila vacía
                return true; // Movimiento válido
            }
        }
        return false; // Columna llena
    }

    /**
     * Verifica si un jugador ha ganado en el tablero.
     * Un jugador gana si consigue alinear cuatro fichas de su color en cualquier dirección.
     *
     * @param tablero El tablero de juego.
     * @param ficha El carácter que representa la ficha del jugador (X u O).
     * @return true si el jugador ha ganado, false en caso contrario.
     */
    public static boolean hayGanador(char[][] tablero, char ficha) {
        int filas = tablero.length;
        int columnas = tablero[0].length;

        // Verificar horizontal
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas - 3; j++) {
                if (tablero[i][j] == ficha && tablero[i][j + 1] == ficha &&
                        tablero[i][j + 2] == ficha && tablero[i][j + 3] == ficha) {
                    return true; // Se ha encontrado un ganador horizontal
                }
            }
        }

        // Verificar vertical
        for (int i = 0; i < filas - 3; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] == ficha && tablero[i + 1][j] == ficha &&
                        tablero[i + 2][j] == ficha && tablero[i + 3][j] == ficha) {
                    return true; // Se ha encontrado un ganador vertical
                }
            }
        }

        // Verificar diagonal ↘
        for (int i = 0; i < filas - 3; i++) {
            for (int j = 0; j < columnas - 3; j++) {
                if (tablero[i][j] == ficha && tablero[i + 1][j + 1] == ficha &&
                        tablero[i + 2][j + 2] == ficha && tablero[i + 3][j + 3] == ficha) {
                    return true; // Se ha encontrado un ganador diagonal ↘
                }
            }
        }

        // Verificar diagonal ↗
        for (int i = 3; i < filas; i++) {
            for (int j = 0; j < columnas - 3; j++) {
                if (tablero[i][j] == ficha && tablero[i - 1][j + 1] == ficha &&
                        tablero[i - 2][j + 2] == ficha && tablero[i - 3][j + 3] == ficha) {
                    return true; // Se ha encontrado un ganador diagonal ↗
                }
            }
        }

        return false; // No hay ganador
    }

    /**
     * Verifica si el tablero está lleno.
     * Un tablero está lleno si no hay espacio en la primera fila de ninguna columna.
     *
     * @param tablero El tablero de juego.
     * @return true si el tablero está lleno, false en caso contrario.
     */
    public static boolean tableroLleno(char[][] tablero) {
        for (int j = 0; j < tablero[0].length; j++) {
            if (tablero[0][j] == '.') {
                return false; // Hay al menos una columna con espacio
            }
        }
        return true; // Todas las columnas están llenas
    }

    /**
     * Muestra el menú de configuración donde el usuario puede elegir entre configurar opciones como los colores de los jugadores,
     * el número de rondas y el orden de salida.
     */
    public static void mostrarConfiguracion() {

        int configuracion;

        // Bucle para mostrar el menú de configuración
        do {
            configuracion = mostrarMenuConfiguracion();

            switch (configuracion) {
                case 1:
                    System.out.println("Color de los jugadores");
                    configurarColorJugadores();
                    break;
                case 2:
                    System.out.println("Rondas jugadas");
                    configurarRondas();
                    break;
                case 3:
                    System.out.println("Orden de salida");
                    configurarOrdenSalida();
                    break;
                case 4:
                    System.out.println("Volver al menú principal");
                    break;
                default:
                    System.out.println("Opcion incorrecta. Eliga un número entre 1 y 3");
            }

        } while (configuracion != 4);
    }

    /**
     * Muestra el menú de configuración y permite al usuario elegir una opción.
     *
     * @return La opción seleccionada por el usuario en el menú de configuración.
     */
    private static int mostrarMenuConfiguracion() {
        int configuracion;

        System.out.println("""
                ******************** CONFIGURACIÓN ********************
                1. Configurar el color de los jugadores
                2. Configurar el numero de rondas
                3. Configurar el orden de salida
                4. Volver al menú principal
                """);

        System.out.println("Elija una configuración: ");
        configuracion = numeros.nextInt();

        return configuracion;
    }

    /**
     * Permite al usuario configurar los colores de los jugadores.
     * El jugador 1 y el jugador 2 no pueden tener el mismo color.
     */
    public static void configurarColorJugadores() {
        System.out.println("Color jugador 1: ");
        String nuevoColorJugador1 = letras.next();

        System.out.println("Color jugador 2: ");
        String nuevoColorJugador2 = letras.next();

        // Asegura que los colores sean diferentes
        while (nuevoColorJugador1.equals(nuevoColorJugador2)) {
            System.out.println("Los colores no pueden ser iguales");
            System.out.println("Color jugador 1: ");
            nuevoColorJugador1 = letras.next();
            System.out.println("Color jugador 2: ");
            nuevoColorJugador2 = letras.next();

        }

        colorJugador1 = nuevoColorJugador1;
        colorJugador2 = nuevoColorJugador2;
        System.out.println("Los colores se han configurado correctamente");
        System.out.println("Color jugador 1: " + colorJugador1);
        System.out.println("Color jugador 2: " + colorJugador2);

    }

    /**
     * Permite al usuario configurar el número de rondas a jugar.
     * Si el número de rondas es menor a 1, se muestra un mensaje de error.
     */
    public static void configurarRondas() {
        System.out.println("Ingrese el número de rondas que se jugarán: ");
        int rondas = numeros.nextInt();

        // Si el número de rondas es inválido, muestra un mensaje de error
        if (rondas < 1) {
            System.out.println("El número de rondas debe ser mayor a 0.");
        } else {
            numeroDeRondas = rondas; // Actualiza el número de rondas
            System.out.println("El número de rondas se ha configurado correctamente a: " + numeroDeRondas);
        }
    }

    /**
     * Permite al usuario configurar el orden de salida de los jugadores.
     * El jugador puede elegir entre un orden aleatorio, que salga el ganador, que salga el perdedor o que siempre empiece el jugador 1.
     */
    public static void configurarOrdenSalida() {

        int ordenSalida;

        // Muestra las opciones para configurar el orden de salida
        System.out.println("""
                ******************** ORDEN DE SALIDA ********************
                1. Aleatorio
                2. Sale ganador
                3. Sale perdedor
                4. Sale siempre jugador 1
                """);

        System.out.println("Elija un orden de salida: ");
        ordenSalida = numeros.nextInt();

        // Si la opción es inválida, muestra un mensaje de error
        if (ordenSalida < 1 || ordenSalida > 4) {
            System.out.println("Opción incorrecta. Elija un número entre 1 y 4.");
        } else {
            System.out.println("El orden de salida se ha configurado correctamente.");
        }

    }

    /**
     * Determina el orden de salida de los jugadores según la configuración seleccionada.
     *
     * @return El índice del jugador que comienza (0 para jugador 1, 1 para jugador 2).
     */
    private static int determinarOrdenSalida() {
        switch (ordenSalida) {
            case 1:
                return (int) (Math.random() * 2); // Aleatorio
            case 2:
                if (victoriasJugador1 > victoriasJugador2) return 0; // Gana el jugador 1
                if (victoriasJugador2 > victoriasJugador1) return 1; // Gana el jugador 2
                return (int) (Math.random() * 2); // Empate, aleatorio
            case 3:
                if (victoriasJugador1 < victoriasJugador2) return 0; // Gana el jugador 2
                if (victoriasJugador2 < victoriasJugador1) return 1; // Gana el jugador 1
                return (int) (Math.random() * 2); // Empate, aleatorio
            case 4:
                return 0; // Siempre Jugador 1
            default:
                return 0;
        }
    }

    /**
     * Muestra los créditos de la aplicación.
     */
        public static void mostrarCreditos () {
            System.out.println("""
                    ******************** CRÉDITOS ********************
                    Aplicación realizada por: Alba Barroso Fernández
                    Curso: DAM 2ºA
                    Año: 2024-2025
                    """);
        }

    public static void mostrarInstrucciones () {
        System.out.println("""
                    ******************** INTRUCCIONES ********************
                    El objetivo de Conecta 4 es alinear cuatro fichas sobre un tablero formado por seis filas y
                    siete columnas. Cada jugador dispone de 21 fichas de un color (por lo general, rojas o
                    amarillas). Por turnos, los jugadores deben introducir una ficha en la columna que prefieran
                    (siempre que no esté completa) y ésta caerá a la posición más baja. Gana la partida el primero
                    que consiga alinear cuatro fichas consecutivas de un mismo color en horizontal, vertical o diagonal.
                    Si todas las columnas están llenas pero nadie ha hecho una fila válida, hay empate.
                    """);
    }

    }
