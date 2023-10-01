import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessLogic {
  public static final int PPN = 10;
  private final Map<String, Product> mapProduct = new HashMap<>();
  private final ArrayList<Receipt> arrayListReceiptHistory = new ArrayList<>();

  public BusinessLogic() {
    initializeProducts();
  }

  public void restockProduct(String input) throws Exception {
    String invalidInputMessage = "Masukan yang Anda berikan tidak valid!";

    // validate input
    if (input.trim().isEmpty() || !input.contains(","))
      throw new Exception(invalidInputMessage);

    String[] inputs = input.trim().split(",");
    if (inputs.length != 2)
      throw new Exception(invalidInputMessage);

    // validate input product name
    String productName = inputs[0];
    if (productName.trim().isEmpty())
      throw new Exception("Nama produk yang Anda berikan tidak valid!");

    // validate input product amount
    int productAmount;
    try {
      productAmount = Integer.parseInt(inputs[1].trim());
    } catch (Exception e) {
      throw new Exception("Jumlah produk yang Anda berikan tidak valid!");
    }

    // validate product name
    String productKey = Utils.Format.key(productName);
    if (!this.mapProduct.containsKey(productKey))
      throw new Exception("Nama produk tidak ditemukan!");

    // increase product stock
    int currentProductStock = this.mapProduct.get(productKey).getStock();
    this.mapProduct.get(productKey).setStock(currentProductStock + productAmount);
  }

  public void createOrder(
      Map<String, Integer> mapOrder,
      String input
  ) throws Exception {
    String invalidInputMessage = "Masukan yang Anda berikan tidak valid!";

    // validate input
    if (input.trim().isEmpty() || !input.contains(","))
      throw new Exception(invalidInputMessage);

    String[] inputs = input.trim().split(",");
    if (inputs.length != 2)
      throw new Exception(invalidInputMessage);

    // validate input product name
    String productName = inputs[0];
    if (productName.trim().isEmpty())
      throw new Exception("Nama produk yang Anda berikan tidak valid!");

    // validate input product amount
    int productAmount;
    try {
      productAmount = Integer.parseInt(inputs[1].trim());
    } catch (Exception e) {
      throw new Exception("Jumlah produk yang Anda berikan tidak valid!");
    }

    // validate product name
    String productKey = Utils.Format.key(productName);
    if (!this.mapProduct.containsKey(productKey))
      throw new Exception("Nama produk tidak ditemukan!");

    // validate product amount
    if (mapOrder.containsKey(productKey)) {
      int totalProductAmount = mapOrder.get(productKey) + productAmount;
      if (this.mapProduct.get(productKey).getStock() < totalProductAmount)
        throw new Exception("Stok produk tidak mencukupi!");

      mapOrder.put(productKey, totalProductAmount);
    } else {
      if (this.mapProduct.get(productKey).getStock() < productAmount)
        throw new Exception("Stok produk tidak mencukupi!");

      mapOrder.put(productKey, productAmount);
    }
  }

  public Invoice createInvoice(
      Map<String, Integer> mapOrder
  ) {
    int totalPrice = 0;
    for (Map.Entry<String, Integer> entry : mapOrder.entrySet()) {
      Product product = this.mapProduct.get(entry.getKey());
      int amount = entry.getValue();

      totalPrice += product.getPrice() * amount;
    }
    double totalPPN = (BusinessLogic.PPN / 100.0) * totalPrice;

    return new Invoice(
        mapOrder,
        totalPrice,
        (int) totalPPN
    );
  }

  public Receipt createReceipt(
      Invoice invoice,
      int cash
  ) throws Exception {
    int totalPayment = invoice.getTotalPrice() + invoice.getTotalPPN();
    if (cash < totalPayment)
      throw new Exception("Nominal yang Anda berikan tidak mencukupi!");

    int cashback = cash - totalPayment;

    // decrease product stock
    for (Map.Entry<String, Integer> entry : invoice.getMapOrder().entrySet()) {
      String productKey = entry.getKey();
      int productAmount = entry.getValue();

      int currentStock = this.mapProduct.get(productKey).getStock();
      this.mapProduct.get(productKey).setStock(currentStock - productAmount);
    }

    Receipt receipt = new Receipt(
        invoice.getMapOrder(),
        invoice.getTotalPrice(),
        invoice.getTotalPPN(),
        cash,
        cashback
    );
    this.arrayListReceiptHistory.add(receipt);

    return receipt;
  }

  private void initializeProducts() {
    Product[] products = {
        new Product("Nasi Ayam", 12000, 10),
        new Product("Nasi Opor", 12000, 10),
        new Product("Nasi Goreng", 11000, 10),
        new Product("Nasi Campur", 13000, 10),
        new Product("Teh", 2000, 10),
        new Product("Jeruk", 3000, 10),
        new Product("Kopi", 4000, 10),
        new Product("Nutrisari", 4000, 10),
    };
    for (Product product : products)
      this.mapProduct.put(
          Utils.Format.key(product.getName()),
          product
      );
  }

  public List<Product> getListProduct() {
    return new ArrayList<>(this.mapProduct.values());
  }

  public List<Receipt> getListReceiptHistory() {
    return this.arrayListReceiptHistory;
  }

  public Product getProductByKey(String key) {
    return this.mapProduct.get(key);
  }
}
