package advration;

//---------------- CUSTOMER CLASS ----------------
class Customer {
 private int id;
 private String name;
 private String rationCard;
 public Customer(int id, String name, String rationCard) {
     this.id = id;
     this.name = name;
     this.rationCard = rationCard;
 }
 public int getId() { return id; }
 public String getName() { return name; }
 public String getRationCard() { return rationCard; }
 @Override
 public String toString() {
     return "ID: " + id + " | Name: " + name + " | Card: " + rationCard;
 }
}
//---------------- RATION ITEM CLASS ----------------
class RationItem {
 private String itemId;
 private String itemName;
 private int quantity;
 public RationItem(String itemId, String itemName, int quantity) {
     this.itemId = itemId;
     this.itemName = itemName;
     this.quantity = quantity;
 }
 public String getItemId() { return itemId; }
 public String getItemName() { return itemName; }
 public int getQuantity() { return quantity; }
 @Override
 public String toString() {
     return "Item ID: " + itemId + " | Name: " + itemName + " | Qty: " + quantity;
 }
}
//---------------- TOKEN CLASS ----------------
class Token {
 private int tokenNo;
 private int customerId;
 public Token(int tokenNo, int customerId) {
     this.tokenNo = tokenNo;
     this.customerId = customerId;
 }
 public int getTokenNo() { return tokenNo; }
 public int getCustomerId() { return customerId; }
 @Override
 public String toString() {
     return "Token No: [" + tokenNo + "] -> Customer ID: " + customerId;
 }
}