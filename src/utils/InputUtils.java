package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.Scanner;

public class InputUtils {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter
            .ofPattern("uuuu-MM-dd", Locale.ROOT)
            .withResolverStyle(ResolverStyle.STRICT);

    public static String readNonEmpty(Scanner sc, String label) {
        while (true) {
            System.out.print(label + ": ");
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("❌ El campo no puede estar vacío. Intentá nuevamente.");
        }
    }

    public static String readPattern(Scanner sc, String label, Pattern pattern, String errorMsg) {
        while (true) {
            System.out.print(label + ": ");
            String s = sc.nextLine().trim();
            if (pattern.matcher(s).matches()) return s;
            System.out.println("❌ " + errorMsg + " Intentá nuevamente.");
        }
    }

    public static int readInt(Scanner sc, String label) {
        while (true) {
            System.out.print(label + ": ");
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("❌ Debe ser un número entero. Intentá nuevamente.");
            }
        }
    }

    public static LocalDate readDate(Scanner sc, String label) {
        while (true) {
            System.out.print(label + " (YYYY-MM-DD): ");
            String s = sc.nextLine().trim();
            try {
                return LocalDate.parse(s, DATE_FMT);
            } catch (Exception e) {
                System.out.println("❌ Formato inválido. Usá YYYY-MM-DD. Intentá nuevamente.");
            }
        }
    }

    public static <E extends Enum<E>> E readEnum(Scanner sc, String label, Class<E> enumClass) {
        String opciones = String.join(", ",
                java.util.Arrays.stream(enumClass.getEnumConstants())
                        .map(Enum::name)
                        .toArray(String[]::new)
        );
        while (true) {
            System.out.print(label + " (" + opciones + "): ");
            String s = sc.nextLine().trim().toUpperCase(Locale.ROOT);
            try {
                return Enum.valueOf(enumClass, s);
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Valor inválido. Opciones: " + opciones + ". Intentá nuevamente.");
            }
        }
    }

    // Helpers específicos:
    public static String readEmail(Scanner sc) {
        Pattern p = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
        return readPattern(sc, "Correo", p, "Correo electrónico inválido.");
    }

    public static String readDni(Scanner sc) {
        Pattern p = Pattern.compile("^\\d{7,10}$"); // ajustá rango si querés
        return readPattern(sc, "DNI", p, "El DNI debe tener solo dígitos (7 a 10).");
    }

    public static String readAlfanumerico(Scanner sc, String label, int min, int max) {
        Pattern p = Pattern.compile("^[A-Za-z0-9]{"+min+","+max+"}$");
        return readPattern(sc, label, p, label+" debe ser alfanumérico ("+min+"-"+max+" caracteres).");
    }
}
