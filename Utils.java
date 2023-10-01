import java.text.DecimalFormat;
import java.util.Scanner;

public class Utils {
  public static class Format {
    public static String currency(int num) {
      DecimalFormat decimalFormat = new DecimalFormat("#,###");
      return decimalFormat.format(num);
    }

    public static String key(String str) {
      return str.toLowerCase().trim().replace(" ", "_");
    }
  }

  public static class Output {
    public static void printLine(String type, int count) {
      for (int a = 0; a < count; a++)
        System.out.print(type);
      System.out.println();
    }
  }

  public static class Input {
    public static String getString(Scanner scanner) {
      System.out.print("> ");
      return scanner.nextLine();
    }

    public static int getInt(Scanner scanner) {
      int result;
      while (true) {
        try {
          System.out.print("> ");
          result = scanner.nextInt();
          scanner.nextLine();
          break;
        } catch (Exception e) {
          System.out.println("Error: Masukkan yang Anda berikan tidak valid!");
          scanner.nextLine();
        }
      }
      return result;
    }

    public static void enterToContinue(Scanner scanner) {
      System.out.println("\n$ Tekan <enter> untuk melanjutkan...");
      String a = scanner.nextLine();
    }
  }
}
