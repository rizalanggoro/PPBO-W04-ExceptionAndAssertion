import java.util.Map;

public class Receipt {
  private final Map<String, Integer> mapOrder;
  private final int totalPrice;
  private final int totalPPN;
  private final int cash;
  private final int cashback;

  public Receipt(
      Map<String, Integer> mapOrder,
      int totalPrice,
      int totalPPN,
      int cash,
      int cashback
  ) {
    this.mapOrder = mapOrder;
    this.totalPrice = totalPrice;
    this.totalPPN = totalPPN;
    this.cash = cash;
    this.cashback = cashback;
  }

  public Map<String, Integer> getMapOrder() {
    return this.mapOrder;
  }
  
  public int getTotalPrice() {
    return this.totalPrice;
  }

  public int getTotalPPN() {
    return this.totalPPN;
  }

  public int getCash() {
    return this.cash;
  }

  public int getCashback() {
    return this.cashback;
  }
}
