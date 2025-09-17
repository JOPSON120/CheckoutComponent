# 🛒 CheckoutComponent

CheckoutComponent is a Java-based component for handling checkout operations.  
It is built with **Gradle** and can be packaged into a runnable JAR file.

---

## 🚀 Getting Started

Follow these steps to build and run the project:

1. **Clone the Repository**
   ```bash
   git clone https://github.com/JOPSON120/CheckoutComponent.git
   cd CheckoutComponent
2. **Build the project**
   ```bash
   ./gradlew build     # for linux
   .\gradlew.bat build # for windows
3. **Remove old artifacts and create jar file**
   ```bash
   rm build/libs       # for linux
   del build/libs      # for windows
   ./gradlew jar     # for linux
   .\gradlew.bat jar # for windows
4. **Run the jar executable**
   ```bash
   java -jar build/libs/CheckoutComponent*.jar

## 📡 API Reference

The CheckoutComponent exposes a REST API to handle checkout operations. Below are the available endpoints:

### Start Checkout
**POST** `/api/checkout/start`  
Starts a new checkout session and notifies the service that the session is ready to accept items.

**Request:**  
_No parameters required._

**Response:**
- `201 CREATED` if the checkout session was successfully started.

---

### Add Item to Checkout
**POST** `/api/checkout/add`  
Adds an item to the current checkout session.

**Parameters:**
- `item_id` (required) – ID of the item to add.
- `quantity` (optional, default `1`) – Number of items to add.

**Response:**
- `200 OK` if the item has been successfully added.
- `404 NOT FOUND` if the item doesn't exist.
- `400 BAD REQUEST` if couldn't add the item because of another reason.
- Returns the price of the added items as plain text in the response body.

### Generate Receipt
**GET** `/api/checkout/receipt`  
Generates the receipt for the current checkout session.

**Response:**
- JSON containing details of the purchased items, individual prices, total price, and saved money.
- For detailed response structure take a look at [Receipt.java](src/main/java/checkout/component/dto/Receipt.java)