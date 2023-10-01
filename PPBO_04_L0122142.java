// Nama : Rizal Dwi Anggoro
// NIM  : L0122142

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PPBO_04_L0122142 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    BusinessLogic logic = new BusinessLogic();

    while (true) {
      try {
        int option = printMainMenu(scanner, logic);
        assert (option >= 1 && option <= 4) : "Opsi yang Anda masukkan tidak valid!";

        if (option == 1) createNewOrder(scanner, logic);
        else if (option == 2) restockProduct(scanner, logic);
        else if (option == 3) statisticStore(scanner, logic);
        else {
          System.out.println("\nKeluar program...");
          break;
        }
      } catch (AssertionError e) {
        System.out.printf("Error: %s\n", e.getMessage());
      }
    }

    scanner.close();
  }

  private static void statisticStore(Scanner scanner, BusinessLogic logic) {
    System.out.println("\nStatistik Burjo\n");

    int totalIncome = 0;
    int totalTaxIncome = 0;
    for (Receipt receipt : logic.getListReceiptHistory()) {
      totalIncome += receipt.getTotalPrice();
      totalTaxIncome += receipt.getTotalPPN();
    }

    System.out.printf("Pemasukan       : Rp %s\n", Utils.Format.currency(totalIncome));
    System.out.printf("Pemasukan pajak : Rp %s\n", Utils.Format.currency(totalTaxIncome));
    System.out.printf(
        "Total pemasukan : Rp %s\n",
        Utils.Format.currency(totalIncome + totalTaxIncome)
    );
    System.out.printf("Total transaksi : %d\n", logic.getListReceiptHistory().size());

    Utils.Input.enterToContinue(scanner);
  }

  private static void restockProduct(Scanner scanner, BusinessLogic logic) {
    System.out.println("\nRestok Produk\n");

    System.out.println("Petunjuk:");
    System.out.println("- Gunakan format berikut untuk melakukan restok:");
    System.out.println("  <nama_produk>, <jumlah_produk>");
    System.out.println("  Contoh: ");
    System.out.println("    Nasi campur, 3");
    System.out.println("- Gunakan kata kunci \"done\" untuk menyelesaikan");
    System.out.println("  restok.\n");

    while (true) {
      try {
        String input = Utils.Input.getString(scanner);
        if (input.equalsIgnoreCase("done"))
          break;
        logic.restockProduct(input);
      } catch (Exception e) {
        System.out.printf("  Error: %s\n", e.getMessage());
      }
    }

    System.out.println("\n$ Berhasil melakukan restok...");
    Utils.Input.enterToContinue(scanner);
  }

  private static void createNewOrder(Scanner scanner, BusinessLogic logic) {
    System.out.println("\nBuat Pesanan Baru\n");

    System.out.println("Petunjuk:");
    System.out.println("- Gunakan format berikut untuk melakukan pemesanan:");
    System.out.println("  <nama_produk>, <jumlah_produk>");
    System.out.println("  Contoh: ");
    System.out.println("    Nasi campur, 3");
    System.out.println("- Gunakan kata kunci \"done\" untuk menyelesaikan");
    System.out.println("  pemesanan.\n");

    Map<String, Integer> mapOrder = new HashMap<>();

    while (true) {
      try {
        String input = Utils.Input.getString(scanner);
        if (input.equalsIgnoreCase("done"))
          break;
        logic.createOrder(mapOrder, input);
      } catch (Exception e) {
        System.out.printf("  Error: %s\n", e.getMessage());
      }
    }

    // invoice
    System.out.println("\n$ Membuat tagihan untuk pesanan Anda...\n");
    Invoice invoice = logic.createInvoice(mapOrder);

    Utils.Output.printLine("=", 56);
    System.out.printf(
        " Harga            : Rp %s \n",
        Utils.Format.currency(invoice.getTotalPrice())
    );
    System.out.printf(
        " PPN              : Rp %s \n",
        Utils.Format.currency(invoice.getTotalPPN())
    );
    Utils.Output.printLine("-", 56);
    System.out.printf(
        " Total pembayaran : Rp %s \n",
        Utils.Format.currency(invoice.getTotalPrice() + invoice.getTotalPPN())
    );
    Utils.Output.printLine("=", 56);

    // payment
    Receipt receipt;
    while (true) {
      try {
        System.out.println("\nMasukkan nominal pembayaran");
        int payment = Utils.Input.getInt(scanner);
        receipt = logic.createReceipt(invoice, payment);
        if (receipt != null)
          break;
      } catch (Exception e) {
        System.out.printf("  Error: %s\n", e.getMessage());
      }
    }

    System.out.println("\n$ Membuat struk untuk belanjaan Anda...\n");

    // show receipt
    Utils.Output.printLine("=", 67);
    System.out.printf(
        " %-16s | %-8s | %-16s | %-16s \n",
        "Nama", "Jumlah", "Satuan", "Harga"
    );
    Utils.Output.printLine("-", 67);

    for (Map.Entry<String, Integer> entry : receipt.getMapOrder().entrySet()) {
      String productKey = entry.getKey();
      int productAmount = entry.getValue();

      final Product product = logic.getProductByKey(productKey);
      System.out.printf(
          " %-16s | %8d | Rp%14s | Rp%14s \n",
          product.getName(), productAmount,
          Utils.Format.currency(product.getPrice()),
          Utils.Format.currency(product.getPrice() * productAmount)
      );
    }
    Utils.Output.printLine("-", 67);

    System.out.printf(
        " %-48s Rp%14s \n",
        "Total harga",
        Utils.Format.currency(receipt.getTotalPrice())
    );
    System.out.printf(
        " %-48s Rp%14s \n",
        "Total PPN (".concat(String.valueOf(BusinessLogic.PPN)).concat("%)"),
        Utils.Format.currency(receipt.getTotalPPN())
    );
    System.out.printf(
        " %-48s Rp%14s \n",
        "Total pembayaran",
        Utils.Format.currency(
            receipt.getTotalPrice() + receipt.getTotalPPN()
        )
    );
    Utils.Output.printLine("-", 67);
    System.out.printf(
        " %-48s Rp%14s \n",
        "Tunai",
        Utils.Format.currency(receipt.getCash())
    );
    System.out.printf(
        " %-48s Rp%14s \n",
        "Kembali",
        Utils.Format.currency(receipt.getCashback())
    );
    Utils.Output.printLine("=", 67);

    Utils.Input.enterToContinue(scanner);
  }

  private static int printMainMenu(Scanner scanner, BusinessLogic logic) {
    System.out.println("\nBurjo Seadanya\n");
    printFoodAndBeverageMenu(logic);

    System.out.println("\nOpsi:");
    System.out.println("1. Buat pesanan baru");
    System.out.println("2. Restok produk");
    System.out.println("3. Statistik penjualan");
    System.out.println("4. Keluar program");

    return Utils.Input.getInt(scanner);
  }

  private static void printFoodAndBeverageMenu(BusinessLogic logic) {
    Utils.Output.printLine("=", 48);
    System.out.printf(
        " %-16s | %-16s | %-8s \n",
        "Nama", "Harga", "Stok"
    );
    Utils.Output.printLine("-", 48);

    for (Product product : logic.getListProduct())
      System.out.printf(
          " %-16s | Rp%14s | %8d \n",
          product.getName(),
          Utils.Format.currency(product.getPrice()),
          product.getStock()
      );


    Utils.Output.printLine("=", 48);
  }
}
