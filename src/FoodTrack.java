import java.util.Scanner;

public class FoodTrack {

    static final String USER = "ana";
    static final String KEY = "java123";

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
            drawMenu(products, prices);
            // Elegir opción
            System.out.print("Elige opción: ");
            option = sc.nextInt();
            sc.nextLine();
            switch (option) {
                case 0:
                    System.out.println("Finalizando compra...");
                    break;
                case 1, 2, 3, 4, 5:
                    // Rellenar el carrito
                    units[option - 1]++;
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }


        } while (option !=0);

        // Comprobar si tiene la cesta vacía
        if ( !hasItems(units) ) {
            System.out.println("Tienes la cesta vacía. Gracias por su visita, " + USER);
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
        System.out.print("¿Pagar en efectivo? (s/n): ");
        char inCash = sc.nextLine().toLowerCase().charAt(0);
        return inCash == 's';

    }

    private static boolean hasItems(int[] units) {
        int totalUnits = 0;
        for (int unit : units) {
            totalUnits += unit;
        }
        return totalUnits > 0;
    }


    static boolean login (Scanner sc) {
        // Número máximo de intentos
        final int MAX_ATTEMPTS = 3;
        // Número de intentos actuales del usuario
        int userAttempts = 0;

        while ( userAttempts < MAX_ATTEMPTS ) {
            System.out.println("===== INICIAR SESIÓN =====");
            System.out.printf("(Intentos restantes: %d de %d)%n",
                    (MAX_ATTEMPTS - userAttempts), MAX_ATTEMPTS);
            // Pedir usuario
            System.out.print("Ingresa nombre de usuario: ");
            String userRequest = sc.nextLine();
            // Pedir contraseña
            System.out.print("Ingresa clave: ");
            String keyRequest = sc.nextLine();

            userAttempts++;


            if ( userRequest.equals(USER) && keyRequest.equals(KEY)) {
                // Éxito de inicio de sesión. Devolver true
                System.out.printf("[OK] Acceso concedido (intento %d de %d)%n",
                        userAttempts, MAX_ATTEMPTS);
                return true;
            }
            System.out.println("[ERROR] Nombre de usuario o contraseña incorrecta");

        }
        System.out.println("[ERROR] Has agotado los intentos de inicio de sesión");
        return false;
    }

    static void drawMenu(String[] products, double[] prices) {
        System.out.println("===== MENÚ DEL DÍA =====");
        for (int i = 0; i < products.length  ; i++) {
            System.out.printf("%d. %s ..... %.2f €%n", i + 1, products[i], prices[i]);
        }
        System.out.println("0. Finalizar pedido");

    }

    static void showCart(String[] products, double[] prices, int[] units) {
        System.out.println("===== CESTA DE PRODUCTOS =====");
        for (int i = 0; i < products.length; i++) {
            if ( units[i] > 0 ){
                System.out.printf("[+] %s ..... %.2f € ..... Unidades: %d%n",
                        products[i], prices[i], units[i]);
            }
        }
    }
    private static void generateInvoice( double[] prices,
                                        int[] units,
                                        boolean inCash) {
        // Calcular subtotal
        double subTotal = calculateSubTotal(prices, units);
        // Descuentos: Si el subtotal supera 20 € 10% de descuento, si no, sin descuento (cero)
        double discount = ( subTotal > 20 ) ? 0.1 * subTotal : 0 ;
        double total = subTotal - discount;
        // Redondear (redondear a 0.05€ más cercano si efectivo, si no, no redondear nada)
        // Fuente redondear 0.05 más cercano: https://stackoverflow.com/questions/9256005/java-rounding-to-nearest-0-05
        total = ( inCash ) ? Math.round(total * 20.0) / 20.0 : total;

        System.out.printf("Pagando %s %s%n",(inCash) ? "en efectivo" : "con tarjeta",
                (discount != 0) ? "(10% dto.)" : "(sin descuento)");

        System.out.println("=========== FACTURA ============");
        System.out.printf("Subtotal .......... %.2f€%n", subTotal);
        System.out.printf("Descuento .......... -%.2f€%n", discount);
        System.out.printf("TOTAL .......... %.2f€%n", total);
        System.out.println("Gracias por su compra, " + USER);

    }

    static double calculateSubTotal(double[] prices, int[] units){
        double subTotal = 0;
        for (int i = 0; i < prices.length; i++) {
            if ( units[i] > 0 ) {
                subTotal += prices[i] * units[i];
            }
        }
        return subTotal;
    }



}
