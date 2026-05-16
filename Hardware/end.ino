#include <Wire.h>
#include <Adafruit_PN532.h>
#include <HX711.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>

// ─── 핀 설정 ─────────────────────────────────────────────────
#define HX711_DT    18
#define HX711_SCK    4
#define LED_PIN      2

// ─── Wi-Fi / 서버 설정 ────────────────────────────────────────
const char* WIFI_SSID  = "고나리";           // ← 실제 SSID 입력
const char* WIFI_PASS  = "27196514";           // ← 실제 비밀번호 입력
const char* SERVER_URL = "http://10.118.57.106:8080/api/hardwares/data"; // ← 실제 주소 입력

// ─── 동작 파라미터 ────────────────────────────────────────────
const float HX711_SCALE = 420.0;

// ─── 전역 변수 ───────────────────────────────────────────────
String deviceId = "";

Adafruit_PN532 nfc(-1, -1);
HX711 scale;

// ─── 함수 선언 ───────────────────────────────────────────────
void   connectWifi();
String readNfcTag();
float  readWeight();
String buildJson(const String& tagUid, float weight);
void   sendData(String tagUid, float weight);
void   blinkLED(int count, int duration);

// ════════════════════════════════════════════════════════════
//  setup()
// ════════════════════════════════════════════════════════════
void setup() {
  Serial.begin(115200);
  pinMode(LED_PIN, OUTPUT);
  delay(3000);
  blinkLED(3, 200);

  // ── 1. ESP32 고유 번호 체크 ───────────────────────────────
  uint64_t chipId = ESP.getEfuseMac();
  deviceId = String((uint32_t)(chipId >> 32), HEX) + String((uint32_t)chipId, HEX);
  deviceId.toUpperCase();
  Serial.printf("[1] 디바이스 ID: %s\n", deviceId.c_str());

  // ── HX711 초기화 ──────────────────────────────────────────
  scale.begin(HX711_DT, HX711_SCK);
  scale.set_scale(HX711_SCALE);
  scale.tare();
  Serial.println("[HX711] 초기화 완료");

  // ── PN532 초기화 ──────────────────────────────────────────
  Wire.begin(21, 22);
  nfc.begin();
  uint32_t versiondata = nfc.getFirmwareVersion();
  if (!versiondata) {
    Serial.println("[PN532] 오류!");
    blinkLED(10, 100);
    while (true) delay(1000);
  }
  nfc.SAMConfig();
  Serial.println("[PN532] 초기화 완료");

  // ── Wi-Fi 연결 ────────────────────────────────────────────
  connectWifi();

  digitalWrite(LED_PIN, HIGH);
  Serial.println("==== 초기화 완료 / NFC 태그를 가져다 대세요 ====");
}

// ════════════════════════════════════════════════════════════
//  loop()
// ════════════════════════════════════════════════════════════
void loop() {
  // ── 2. NFC 탐지 ───────────────────────────────────────────
  Serial.println("[2] NFC 태그 대기 중...");
  String tagUid = readNfcTag();
  if (tagUid == "") return;
  Serial.printf("[2] 태그 인식! ID: %s\n", tagUid.c_str());
  blinkLED(2, 200);

  // ── 3. 무게 측정 ──────────────────────────────────────────
  float currentWeight = readWeight();
  Serial.printf("[3] 현재 무게: %.2f g\n", currentWeight);

  // ── 4. 서버 전송 ──────────────────────────────────────────
  sendData(tagUid, currentWeight);
}

// ════════════════════════════════════════════════════════════
//  JSON 빌드
// ════════════════════════════════════════════════════════════
String buildJson(const String& tagUid, float weight) {
  StaticJsonDocument<256> doc;
  doc["deviceCode"] = deviceId;
  doc["tagUid"]     = tagUid;
  doc["weight"]     = serialized(String(weight, 2));

  String output;
  serializeJson(doc, output);
  return output;
}

// ════════════════════════════════════════════════════════════
//  서버 전송
// ════════════════════════════════════════════════════════════
void sendData(String tagUid, float weight) {
  if (WiFi.status() != WL_CONNECTED) {
    connectWifi();
    if (WiFi.status() != WL_CONNECTED) {
      Serial.println("[HTTP] Wi-Fi 미연결 → 전송 건너뜀");
      return;
    }
  }

  String payload = buildJson(tagUid, weight);
  Serial.printf("[4] 전송 데이터: %s\n", payload.c_str());

  HTTPClient http;
  http.begin(SERVER_URL);
  http.addHeader("Content-Type", "application/json");
  http.setTimeout(8000);

  int httpCode = http.POST(payload);
  if (httpCode > 0) {
    Serial.printf("[4] 응답 코드: %d\n", httpCode);
    Serial.printf("[4] 응답 바디: %s\n", http.getString().c_str());
  } else {
    Serial.printf("[4] 전송 오류: %s\n", http.errorToString(httpCode).c_str());
  }
  http.end();
}

// ════════════════════════════════════════════════════════════
//  Wi-Fi 연결
// ════════════════════════════════════════════════════════════
void connectWifi() {
  Serial.println("[Wi-Fi] 연결 시도 중...");
  WiFi.begin(WIFI_SSID, WIFI_PASS);

  unsigned long start = millis();
  while (WiFi.status() != WL_CONNECTED) {
    if (millis() - start > 10000) {
      Serial.println("[Wi-Fi] 연결 실패!");
      return;
    }
    delay(500);
    Serial.print(".");
  }
  Serial.println();
  Serial.printf("[Wi-Fi] 연결 성공! IP: %s\n", WiFi.localIP().toString().c_str());
}

// ════════════════════════════════════════════════════════════
//  NFC 태그 읽기
// ════════════════════════════════════════════════════════════
String readNfcTag() {
  uint8_t uid[7];
  uint8_t uidLength;

  bool detected = nfc.readPassiveTargetID(
    PN532_MIFARE_ISO14443A, uid, &uidLength, 5000
  );

  if (!detected) return "";

  String tagUid = "";
  for (uint8_t i = 0; i < uidLength; i++) {
    if (uid[i] < 0x10) tagUid += "0";
    tagUid += String(uid[i], HEX);
    if (i < uidLength - 1) tagUid += ":";
  }
  tagUid.toUpperCase();
  return tagUid;
}

// ════════════════════════════════════════════════════════════
//  무게 측정
// ════════════════════════════════════════════════════════════
float readWeight() {
  if (!scale.is_ready()) return 0;
  float w = scale.get_units(5);
  if (w < 0) w = 0;
  return w;
}

// ════════════════════════════════════════════════════════════
//  LED 깜빡이기
// ════════════════════════════════════════════════════════════
void blinkLED(int count, int duration) {
  for (int i = 0; i < count; i++) {
    digitalWrite(LED_PIN, LOW);
    delay(duration);
    digitalWrite(LED_PIN, HIGH);
    delay(duration);
  }
}