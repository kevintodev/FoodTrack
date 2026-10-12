import java.util.Scanner;

public class FoodTrack {

    static final String USER = "ana";
    static final String KEY = "java123";

    /* Colores */
    static final String	RESET = "\u001B[0m";
    static final String	RED	 = "\u001B[31m";
    static final String	GREEN = "\u001B[32m";
    static final String	YELLOW = "\u001B[33m";
    static final String	BLUE = "\u001B[34m";
    static final String	MAGENTA = "\u001B[35m";
    static final String	CYAN = "\u001B[36m";
    static final String	WHITE = "\u001B[37m";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!login(sc)) {
            sc.close();
            return;
        }
        // arrays de productos y precios
        String[] products = {
                "Pizza con piña",
                "Smash Burger",
                "Patatas fritas",
                "Tacos mexicanos",
                "Wrap de pollo"
        };
        double[] prices = {5.25, 4.50, 2.50, 9.50, 7.95};
        int[] units = {0, 0, 0, 0, 0};

        // do-while: menú de pedido con switch
        int option;
        do {
            drawMenu(products, prices, units);
            // Elegir opción
            System.out.print(CYAN + "Elige opción: " + RESET);
            option = sc.nextInt();
            sc.nextLine();
            switch (option) {
                case 0:
                    System.out.println(MAGENTA + "Finalizando compra..." + RESET);
                    break;
                case 1, 2, 3, 4, 5:
                    // Rellenar el carrito
                    units[option - 1]++;
                    System.out.printf("%s[+] %s Añadido a la cesta %s%n",
                            GREEN, products[option - 1], RESET);
                    break;
                default:
                    System.out.println(RED + "[ERROR] Opción no válida" + RESET);
                    break;
            }


        } while (option !=0);

        // Comprobar si tiene la cesta vacía
        if ( !hasItems(units) ) {
            System.out.println(YELLOW + "Tienes la cesta vacía. Gracias por su visita, " + USER + RESET);
        } else {
            // Decidir si pagar en cash o con tarjeta
            boolean inCash = inCash(sc);

            // Resumen de la cesta de productos
            showCart(products, prices, units);
            // for: calcular factura con descuentos
            generateInvoice(prices, units, inCash);
        }


        // Cerrar Scanner
        sc.close();

    }

    private static boolean inCash(Scanner sc) {
        /*
        * Preguntar si pagar en efectivo
        * Devuelve:
        * - true si ha pulsado 's',
        * - false, si no
        * */

        System.out.print(YELLOW + "¿Pagar en efectivo? (s/n): " + RESET);
        char inCash = sc.nextLine().toLowerCase().charAt(0);
        return inCash == 's';

    }

    private static boolean hasItems(int[] units) {
        /*
        * Comprobar si ha finalizado con productos
        * en el carrito.
        * Devuelve
        * - true si totalUnits > 0
        * - false, si no
        * */

        int totalUnits = 0;
        for (int unit : units) {
            totalUnits += unit;
        }
        return totalUnits > 0;
    }


    static boolean login (Scanner sc) {
        /*
        * Sistema de login
        * Pide usuario y clave. Máximo 3 intentos
        * Devuelve:
        * - true: login correcto
        * - false: 3 intentos fallidos
        * */

        // Número máximo de intentos
        final int MAX_ATTEMPTS = 3;
        // Número de intentos actuales del usuario
        int userAttempts = 0;

        while ( userAttempts < MAX_ATTEMPTS ) {
            System.out.println(BLUE + "===== INICIAR SESIÓN =====" + RESET);
            System.out.printf("%s(Intentos restantes: %d de %d)%s%n", CYAN,
                    (MAX_ATTEMPTS - userAttempts), MAX_ATTEMPTS, RESET);
            // Pedir usuario
            System.out.print(BLUE + "Ingresa nombre de usuario: " + RESET);
            String userRequest = sc.nextLine();
            // Pedir contraseña
            System.out.print(BLUE + "Ingresa clave: " + RESET);
            String keyRequest = sc.nextLine();

            userAttempts++;


            if ( userRequest.equals(USER) && keyRequest.equals(KEY)) {
                // Éxito de inicio de sesión. Devolver true
                System.out.printf("%s[OK] Acceso concedido (intento %d de %d)%s%n",
                        GREEN, userAttempts, MAX_ATTEMPTS, RESET);
                return true;
            }
            System.out.println(RED + "[ERROR] Nombre de usuario o contraseña incorrecta" + RESET);

        }
        System.out.println(RED + "[ERROR] Has agotado los intentos de inicio de sesión" + RESET);
        return false;
    }

    static void drawMenu(String[] products, double[] prices,  int[] units) {
        /*
        * Dibujar el menú del día
        * */

        System.out.println(MAGENTA + "===== MENÚ DEL DÍA =====" + RESET);
        for (int i = 0; i < products.length  ; i++) {
            System.out.printf("%s%d. %s ..... %.2f €%s",
                    CYAN, i + 1, products[i], prices[i], RESET);
            System.out.printf("%s (en tu cesta: %d unidades) %s%n",
                    MAGENTA, units[i],RESET);
        }
        System.out.println(CYAN + "0. Finalizar pedido" + RESET);

    }

    static void showCart(String[] products, double[] prices, int[] units) {
        /*
        * Mostrar el carro de compra
        * Muestra nombre de producto, precio y unidades solicitadas
        * */

        System.out.println(YELLOW + "===== CESTA DE PRODUCTOS =====" + RESET);
        for (int i = 0; i < products.length; i++) {
            if ( units[i] > 0 ){
                System.out.printf("%s[+] %s ..... %.2f € ..... Unidades: %d%s%n",
                        CYAN, products[i], prices[i], units[i], RESET);
            }
        }
    }
    private static void generateInvoice( double[] prices,
                                        int[] units,
                                        boolean inCash) {
        /*
        * Generar factura
        * Calcula el subtotal
        * Aplica los descuentos, si los hay
        * Imprime la factura detallada (modo de pago, subtotal, descuento, total)
        * */

        // Calcular subtotal
        double subTotal = calculateSubTotal(prices, units);
        // Descuentos: Si el subtotal supera 20 € 10% de descuento, si no, sin descuento (cero)
        double discount = ( subTotal > 20 ) ? 0.1 * subTotal : 0 ;
        double total = subTotal - discount;
        // Redondear (redondear a 0.05€ más cercano si efectivo, si no, no redondear nada)
        // Fuente redondear 0.05 más cercano: https://stackoverflow.com/questions/9256005/java-rounding-to-nearest-0-05
        total = ( inCash ) ? Math.round(total * 20.0) / 20.0 : total;

        System.out.printf("%sPagando %s %s%s%n", GREEN, (inCash) ? "en efectivo" : "con tarjeta",
                (discount != 0) ? "(10% dto.)" : "(sin descuento)", RESET);

        System.out.println(BLUE + "=========== FACTURA ============" + RESET);
        System.out.printf("%sSubtotal .......... %.2f €%s%n", CYAN, subTotal, RESET);
        System.out.printf("%sDescuento .......... -%.2f €%s%n", CYAN, discount, RESET);
        System.out.printf("%sTOTAL .......... %.2f €%s%n", MAGENTA, total, RESET);
        System.out.println(CYAN + "Gracias por su compra, " + USER + RESET);

    }

    static double calculateSubTotal(double[] prices, int[] units){
        /*
        * Calcular subtotal de productos
        * Recorre array de precios y unidades y suma los precios * unidades
        * Devuelve el subtotal
        * */
        double subTotal = 0;
        for (int i = 0; i < prices.length; i++) {
            if ( units[i] > 0 ) {
                subTotal += prices[i] * units[i];
            }
        }
        return subTotal;
    }

}
