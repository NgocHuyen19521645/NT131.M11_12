/*
 * Kết nối:
 *          DHT                   Arduino
 *          VCC                     5V
 *          GND                     GND
 *         Tín hiệu                  2
 *         
 * Tùy chọn tên của cảm biến cho phù hợp
 * Nạp code mở Serial Monitor, chọn No line ending, baud 9600
 * 
 * 
 * 
 * 
 * 
 */
#include "DHT.h"
#include "ESP8266WiFi.h"

#define DHTPIN 2     // what digital pin we're connected to
#define LIGHT 3

// Chọn loại cảm biến cho phù hợp ---------------------------------------------------------------------------------------
#define DHTTYPE DHT11   // DHT 11
//#define DHTTYPE DHT22   // DHT 22  (AM2302), AM2321
//#define DHTTYPE DHT21   // DHT 21 (AM2301)


DHT dht(DHTPIN, DHTTYPE);

// Wifi SSID, pass
const char* ssid = "Ngoc Phong";
const char* pass = "01657096210";

void setup() {
  Serial.begin(9600);
  Serial.println("DHTxx test!");
  
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.hostname("Wemos D1");
  WiFi.begin(ssid, pass);
 
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");
 
  // Print the IP address
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  
  dht.begin();
}

void loop() {
  // Wait a few seconds between measurements.
  delay(2000);

  Serial.println(digitalRead(LIGHT));

  // Reading temperature or humidity takes about 250 milliseconds!
  // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
  float h = dht.readHumidity();
  // Read temperature as Celsius (the default)
  float t = dht.readTemperature();
  // Read temperature as Fahrenheit (isFahrenheit = true)
  float f = dht.readTemperature(true);

  // Check if any reads failed and exit early (to try again).
  if (isnan(h) || isnan(t) || isnan(f)) {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }

  // Compute heat index in Fahrenheit (the default)
  float hif = dht.computeHeatIndex(f, h);
  // Compute heat index in Celsius (isFahreheit = false)
  float hic = dht.computeHeatIndex(t, h, false);

  Serial.print("Humidity: ");
  Serial.print(h);
  Serial.print(" %t");
  Serial.print("Temperature: ");
  Serial.print(t);
  Serial.print(" *C ");
  Serial.print(f);
  Serial.print(" *Ft");
  Serial.print("Heat index: ");
  Serial.print(hic);
  Serial.print(" *C ");
  Serial.print(hif);
  Serial.println(" *F");
}
