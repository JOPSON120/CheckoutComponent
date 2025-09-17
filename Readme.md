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