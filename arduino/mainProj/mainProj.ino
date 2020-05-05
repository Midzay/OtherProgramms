#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <Keypad.h> 
#include <EEPROM.h>

byte bukva_B[8]   = {B11110,B10000,B10000,B11110,B10001,B10001,B11110,B00000,}; // Буква "Б"
byte bukva_G[8]   = {B11111,B10001,B10000,B10000,B10000,B10000,B10000,B00000,}; // Буква "Г"
byte bukva_D[8]   = {B01111,B00101,B00101,B01001,B10001,B11111,B10001,B00000,}; // Буква "Д"
byte bukva_ZH[8]  = {B10101,B10101,B10101,B11111,B10101,B10101,B10101,B00000,}; // Буква "Ж"
byte bukva_Z[8]   = {B01110,B10001,B00001,B00010,B00001,B10001,B01110,B00000,}; // Буква "З"
byte bukva_I[8]   = {B10001,B10011,B10011,B10101,B11001,B11001,B10001,B00000,}; // Буква "И"
byte bukva_IY[8]  = {B01110,B00000,B10001,B10011,B10101,B11001,B10001,B00000,}; // Буква "Й"
byte bukva_L[8]   = {B00011,B00111,B00101,B00101,B01101,B01001,B11001,B00000,}; // Буква "Л"
byte bukva_P[8]   = {B11111,B10001,B10001,B10001,B10001,B10001,B10001,B00000,}; // Буква "П"
byte bukva_Y[8]   = {B10001,B10001,B10001,B01010,B00100,B01000,B10000,B00000,}; // Буква "У"
byte bukva_F[8]   = {B00100,B11111,B10101,B10101,B11111,B00100,B00100,B00000,}; // Буква "Ф"
byte bukva_TS[8]  = {B10010,B10010,B10010,B10010,B10010,B10010,B11111,B00001,}; // Буква "Ц"
byte bukva_CH[8]  = {B10001,B10001,B10001,B01111,B00001,B00001,B00001,B00000,}; // Буква "Ч"
byte bukva_Sh[8]  = {B10101,B10101,B10101,B10101,B10101,B10101,B11111,B00000,}; // Буква "Ш"
byte bukva_Shch[8]= {B10101,B10101,B10101,B10101,B10101,B10101,B11111,B00001,}; // Буква "Щ"
byte bukva_Mz[8]  = {B10000,B10000,B10000,B11110,B10001,B10001,B11110,B00000,}; // Буква "Ь"
byte bukva_IYI[8] = {B10001,B10001,B10001,B11001,B10101,B10101,B11001,B00000,}; // Буква "Ы"
byte bukva_Tz[8]  = {B11000,B01000,B01000,B01111,B01001,B01001,B01111,B00000,}; // Буква "Ъ"
byte bukva_Yu[8]  = {B10010,B10101,B10101,B11101,B10101,B10101,B10010,B00000,}; // Буква "Ю"
byte bukva_Ya[8]  = {B01111,B10001,B10001,B01111,B00101,B01001,B10001,B00000,}; // Буква "Я"
LiquidCrystal_I2C lcd(0x27, 20, 4);

int addr=0;


int count =0;
const byte ROWS = 4; // 4 строки
const byte COLS = 4; // 4 столбца
char keys[ROWS][COLS] = {
  {'1','2','3','A'},
  {'4','5','6','B'},
  {'7','8','9','C'},
  {'*','0','#','D'}
}; 
byte rowPins[ROWS] = {11,10, 9, 8}; 
byte colPins[COLS] = {7, 6, 5, 4}; 
Keypad keypad = Keypad( makeKeymap(keys), rowPins, colPins, ROWS, COLS );
int posCursor =0;
float totalLiters =0;
int numDisplay =0;
boolean flagPowerka=false;
boolean flagStart=false;


// НА счетчик
byte statusLed    = 13;
byte sensorInterrupt = 0;  // 0 = digital pin 2
byte sensorPin       = 2;
float calibrationFactor = 11;
volatile byte pulseCount;  
float flowRate;
unsigned int flowMilliLitres;
float totalMilliLitres;
unsigned long oldTime;
int posObem =3;
String strObem ="";



void setup()
{
Serial.begin(9600);
getStartDisplay(); 

pinMode(statusLed, OUTPUT);
digitalWrite(statusLed, HIGH);  // We have an active-low LED attached
pinMode(sensorPin, INPUT);
digitalWrite(sensorPin, HIGH);

pulseCount        = 0;
flowRate          = 0.0;
flowMilliLitres   = 0;
oldTime           = 0;
attachInterrupt(sensorInterrupt, pulseCounter, FALLING);

}

void loop()
{
//Serial.println(count);
//count++;
 char key = keypad.getKey();
  if (key){
    Serial.println(key); // Передаем название нажатой клавиши в сериал порт
    //Получаем позицию для указателя
    int newPosCursor = getPositionCursor(key,posCursor);
    
    if ((newPosCursor != 999) && (posCursor != newPosCursor)) {
        lcd.setCursor(1, posCursor); // 1 строка
        lcd.print(" ");      
        lcd.setCursor(1, newPosCursor); // 1 строка
        lcd.print(">");      
        posCursor=newPosCursor;
      }

  //  Обрабатываем клавишу ввод
    if ((numDisplay ==3) && (key=='D')) {
        int val = strObem.toInt();
        EEPROM.write(addr, val); 
        getStartDisplay();
        key='z'; 
        posCursor=0;
              
              }
              
  if (key=='D'){
            
      getNewDisplay(posCursor,numDisplay);
    }

    if ((numDisplay ==3)&& (key!='D') && (key !='*')){
      lcd.setCursor(posObem, 1); 
      lcd.print(key);
      lcd.setCursor(posObem++, 1); 
      strObem +=key;
      }

   if (key=='*'){
    getStartDisplay();
    posCursor = 0;
    numDisplay = 0;
    flagPowerka = false;
    flagStart = false;
    
        
    }
    if (key=='C'){
    
    flagStart=!flagStart;
        
    }
    
  }
  if (flagPowerka)
 startPowerka();
}

void getNewDisplay(int posCursor,int numDisp){
  
  //СТАРТ
  if ((posCursor==0)&& (numDisp==0)){
    numDisplay = 1;
    powerkaDisplay();
       }
       
    // ОБЪЕМ
  else if ((posCursor==1) && (numDisp==0)){
    numDisplay = 2;
    obemDisplay();
          }
  else if ((posCursor==1) && (numDisp==2)){
  numDisplay = 3;
  enterObem();
        }

    else if ((posCursor==2) && (numDisp==2)){
    strObem="";
    int  valRead = EEPROM.read(addr);
    if (valRead!=255){
    EEPROM.write(addr,255);}
    getStartDisplay();
          }
     
  //НАСТРОЙКА
  if ((posCursor==2) && (numDisp==0)){
    numDisplay = 4;
    lcd.clear();
    lcd.setCursor(3, 0); // 1 строка
    lcd.print("Version 5.9");
    lcd.setCursor(3, 2); 
    lcd.print("KO\4-BO IMPS 11");
    
    }
  }

void enterObem(){
    posObem =3;
    lcd.clear();
    lcd.setCursor(3, 0); // 1 строка
    lcd.print("BBED\3TE O\4\7EM");
     
  }  
void powerkaDisplay(){
    lcd.clear();
    lcd.setCursor(10, 0); // 1 строка
    lcd.print("\4\5TPOB");
    lcd.setCursor(10, 1); // 1 строка
    lcd.print("\4\5TPOB/M\5H");
    totalMilliLitres  = 0;
    flagPowerka =true;
    }
void obemDisplay(){
  lcd.begin();
    lcd.createChar(1, bukva_Y);  
    lcd.createChar(2, bukva_CH);
    lcd.createChar(3, bukva_I);      // Создаем символ под номером 1
    lcd.createChar(4, bukva_B);
    lcd.createChar(5, bukva_IYI);  
    lcd.createChar(6, bukva_Tz);  
    lcd.createChar(7,  bukva_Mz);  
    lcd.setCursor(2, 0); // 1 строка
    lcd.print("\1\2\3T\5BAT\7 O\4\7EM");
    lcd.setCursor(4, 1); // 1 строка
    lcd.print("DA");
    lcd.setCursor(4, 2); // 1 строка
    lcd.print("HET");
    lcd.setCursor(1, 1); // 1 строка
    lcd.print(">");
  
  }

int getPositionCursor(char key,int posCursor){
  int pos = posCursor;
  if (numDisplay==0){
    
  if (key=='A'){
    if (pos==0){return pos=2; }
    pos--;
    }
  if (key=='B'){
    if (pos==2){ return pos=0; }
    pos++;
    }  
  return pos;
    }
  else if (numDisplay == 2)
  {
       if (key=='A'){
      if (pos==1){return pos=2; }
      pos--;
      }
    if (key=='B'){
      if (pos==2){ return pos=1; }
      pos++;
      }  
    return pos;
    }

else if (numDisplay == 1)
  {
      return 999;
    }
    
    }
    
    

void getStartDisplay(){
numDisplay=0;
lcd.begin();
lcd.createChar(5, bukva_I);      // Создаем символ под номером 1
lcd.createChar(1, bukva_B);      // Создаем символ под номером 1
lcd.createChar(2, bukva_Tz);      // Создаем символ под номером 2
lcd.createChar(3, bukva_IY);
lcd.createChar(4, bukva_L); 

lcd.setCursor(1, 0); // 1 строка
lcd.print(">");
lcd.setCursor(3, 0); // 1 строка
lcd.print("CTAPT");

lcd.setCursor(3, 1); // 2 строка
lcd.print("O\1\2EM");
lcd.setCursor(3, 2); // 3 строка
lcd.print("HACTPO\3KA");

int valRead = EEPROM.read(addr);
if (valRead!=255){
  lcd.setCursor(10, 3); 
      lcd.print('V');
      lcd.setCursor(12, 3); 
      lcd.print(valRead);
  }      
  }

void startPowerka(){
  int valRead = EEPROM.read(addr);
    if (valRead!=255){
    if (totalMilliLitres/1000>valRead){
      flagStart =false;
      }
  }
   if((millis() - oldTime) > 1000)    // Only process counters once per second
  { 
    detachInterrupt(sensorInterrupt);
        
    flowRate = ((1000.0 / (millis() - oldTime)) * pulseCount) / calibrationFactor;
    lcd.setCursor(3, 1); // 1 строка
    if (flowRate<0.01){
    lcd.print("0.000");
    }else{
      lcd.print(flowRate,3);
      }
    oldTime = millis();
    flowMilliLitres = (flowRate / 60) * 1000;
    lcd.setCursor(3, 0);
    lcd.print(totalMilliLitres/1000,3);
    if (flagStart){
    totalMilliLitres += flowMilliLitres;
    lcd.setCursor(3, 0); 
    lcd.print(totalMilliLitres/1000,3);
    }
    unsigned int frac;
//    Serial.print("Flow rate: ");
//    Serial.print(int(flowRate));  // Print the integer part of the variable
//    Serial.print("L/min");
//    Serial.print("\t");       // Print tab space
//    Serial.print("Output Liquid Quantity: ");        
//    Serial.print(totalMilliLitres);
//    Serial.println("mL"); 
//    Serial.print("\t");       // Print tab space
//  Serial.print(totalMilliLitres/1000);
//  Serial.print("L");
    
    pulseCount = 0;
    
   
    attachInterrupt(sensorInterrupt, pulseCounter, FALLING);
  }
  
  }  

  void pulseCounter()
{
  // Increment the pulse counter
  pulseCount++;
}
