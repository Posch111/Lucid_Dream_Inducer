EESchema Schematic File Version 4
LIBS:REM_sleep_analysis-cache
EELAYER 26 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 1 1
Title ""
Date ""
Rev ""
Comp ""
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
Text GLabel 1200 950  1    60   Input ~ 0
Signal1
Text GLabel 1200 1550 3    60   Input ~ 0
Signal2
$Comp
L REM_sleep_analysis-rescue:C-REM_sleep_analysis-rescue C1
U 1 1 59DE64B3
P 1450 1000
F 0 "C1" H 1475 1100 50  0000 L CNN
F 1 "1u" H 1475 900 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric" H 1488 850 50  0001 C CNN
F 3 "" H 1450 1000 50  0001 C CNN
	1    1450 1000
	0    1    1    0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:C-REM_sleep_analysis-rescue C2
U 1 1 59DE6527
P 1450 1500
F 0 "C2" H 1475 1600 50  0000 L CNN
F 1 "1u" H 1475 1400 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric" H 1488 1350 50  0001 C CNN
F 3 "" H 1450 1500 50  0001 C CNN
	1    1450 1500
	0    1    1    0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R1
U 1 1 59DE6556
P 1900 1150
F 0 "R1" V 1980 1150 50  0000 C CNN
F 1 "1M" V 1900 1150 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 1830 1150 50  0001 C CNN
F 3 "" H 1900 1150 50  0001 C CNN
	1    1900 1150
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R2
U 1 1 59DE6598
P 1900 1650
F 0 "R2" V 1980 1650 50  0000 C CNN
F 1 "1M" V 1900 1650 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 1830 1650 50  0001 C CNN
F 3 "" H 1900 1650 50  0001 C CNN
	1    1900 1650
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:INA128-REM_sleep_analysis-rescue U2
U 1 1 59DE69CE
P 3100 1300
F 0 "U2" H 3250 1425 50  0000 L CNN
F 1 "INA128" H 3250 1175 50  0000 L CNN
F 2 "Package_SO:SOIC-8_3.9x4.9mm_P1.27mm" H 3200 1300 50  0001 C CNN
F 3 "" H 3200 1300 50  0001 C CNN
	1    3100 1300
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R3
U 1 1 59DE6A42
P 2600 1300
F 0 "R3" V 2680 1300 50  0000 C CNN
F 1 "675" V 2600 1300 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 2530 1300 50  0001 C CNN
F 3 "" H 2600 1300 50  0001 C CNN
	1    2600 1300
	1    0    0    -1  
$EndComp
Text GLabel 3100 900  1    60   Input ~ 0
+3.0V
Text GLabel 3200 1800 3    60   Input ~ 0
Reference
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR01
U 1 1 59DE6B1A
P 3100 1600
F 0 "#PWR01" H 3100 1350 50  0001 C CNN
F 1 "GND" H 3100 1450 50  0000 C CNN
F 2 "" H 3100 1600 50  0001 C CNN
F 3 "" H 3100 1600 50  0001 C CNN
	1    3100 1600
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:C-REM_sleep_analysis-rescue C3
U 1 1 59DE6B4C
P 3900 1300
F 0 "C3" H 3925 1400 50  0000 L CNN
F 1 "1u" H 3925 1200 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric" H 3938 1150 50  0001 C CNN
F 3 "" H 3900 1300 50  0001 C CNN
	1    3900 1300
	0    1    1    0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R4
U 1 1 59DE6BC4
P 4300 1450
F 0 "R4" V 4380 1450 50  0000 C CNN
F 1 "200k" V 4300 1450 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 4230 1450 50  0001 C CNN
F 3 "" H 4300 1450 50  0001 C CNN
	1    4300 1450
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:C-REM_sleep_analysis-rescue C12
U 1 1 59DE6C40
P 5150 3250
F 0 "C12" H 5175 3350 50  0000 L CNN
F 1 "220n" H 5175 3150 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric" H 5188 3100 50  0001 C CNN
F 3 "" H 5150 3250 50  0001 C CNN
	1    5150 3250
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR02
U 1 1 59DE6CFF
P 5150 3400
F 0 "#PWR02" H 5150 3150 50  0001 C CNN
F 1 "GND" H 5150 3250 50  0000 C CNN
F 2 "" H 5150 3400 50  0001 C CNN
F 3 "" H 5150 3400 50  0001 C CNN
	1    5150 3400
	1    0    0    -1  
$EndComp
Text GLabel 5150 3100 1    60   Input ~ 0
+3.0V
$Comp
L REM_sleep_analysis-rescue:Conn_01x02_Male-REM_sleep_analysis-rescue J1
U 1 1 59DE7023
P 4550 4200
F 0 "J1" H 4550 4300 50  0000 C CNN
F 1 "Batt_Connector" H 4550 4000 50  0000 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x02_P2.54mm_Horizontal" H 4550 4200 50  0001 C CNN
F 3 "" H 4550 4200 50  0001 C CNN
	1    4550 4200
	1    0    0    1   
$EndComp
$Comp
L REM_sleep_analysis-rescue:C-REM_sleep_analysis-rescue C11
U 1 1 59DE708B
P 5200 4250
F 0 "C11" H 5225 4350 50  0000 L CNN
F 1 "220n" H 5225 4150 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric" H 5238 4100 50  0001 C CNN
F 3 "" H 5200 4250 50  0001 C CNN
	1    5200 4250
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR03
U 1 1 59DE719C
P 5200 4500
F 0 "#PWR03" H 5200 4250 50  0001 C CNN
F 1 "GND" H 5200 4350 50  0000 C CNN
F 2 "" H 5200 4500 50  0001 C CNN
F 3 "" H 5200 4500 50  0001 C CNN
	1    5200 4500
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R5
U 1 1 59DE7445
P 4950 1300
F 0 "R5" V 5030 1300 50  0000 C CNN
F 1 "21k" V 4950 1300 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 4880 1300 50  0001 C CNN
F 3 "" H 4950 1300 50  0001 C CNN
	1    4950 1300
	0    1    1    0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:C-REM_sleep_analysis-rescue C4
U 1 1 59DE7494
P 5400 1450
F 0 "C4" H 5425 1550 50  0000 L CNN
F 1 "220n" H 5425 1350 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric" H 5438 1300 50  0001 C CNN
F 3 "" H 5400 1450 50  0001 C CNN
	1    5400 1450
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:MCP602-REM_sleep_analysis-rescue U1
U 2 1 59DE74FA
P 6200 1400
F 0 "U1" H 6200 1600 50  0000 L CNN
F 1 "MCP602" H 6200 1200 50  0000 L CNN
F 2 "Package_SO:SOIC-8_3.9x4.9mm_P1.27mm" H 6200 1400 50  0001 C CNN
F 3 "" H 6200 1400 50  0001 C CNN
	2    6200 1400
	1    0    0    -1  
$EndComp
Text GLabel 6100 1000 1    60   Input ~ 0
+3.0V
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR04
U 1 1 59DE7646
P 6100 1800
F 0 "#PWR04" H 6100 1550 50  0001 C CNN
F 1 "GND" H 6100 1650 50  0000 C CNN
F 2 "" H 6100 1800 50  0001 C CNN
F 3 "" H 6100 1800 50  0001 C CNN
	1    6100 1800
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R6
U 1 1 59DE7998
P 5700 1650
F 0 "R6" V 5780 1650 50  0000 C CNN
F 1 "2.3k" V 5700 1650 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 5630 1650 50  0001 C CNN
F 3 "" H 5700 1650 50  0001 C CNN
	1    5700 1650
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R7
U 1 1 59DE7C14
P 6100 2100
F 0 "R7" V 6180 2100 50  0000 C CNN
F 1 "21k" V 6100 2100 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 6030 2100 50  0001 C CNN
F 3 "" H 6100 2100 50  0001 C CNN
	1    6100 2100
	0    1    -1   0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:C-REM_sleep_analysis-rescue C5
U 1 1 59DE7CB7
P 6100 2300
F 0 "C5" H 6125 2400 50  0000 L CNN
F 1 "220n" H 6125 2200 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric" H 6138 2150 50  0001 C CNN
F 3 "" H 6100 2300 50  0001 C CNN
	1    6100 2300
	0    1    1    0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R8
U 1 1 59DE7F4D
P 7450 1400
F 0 "R8" V 7530 1400 50  0000 C CNN
F 1 "102k" V 7450 1400 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 7380 1400 50  0001 C CNN
F 3 "" H 7450 1400 50  0001 C CNN
	1    7450 1400
	0    1    -1   0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R9
U 1 1 59DE7FA8
P 7950 1400
F 0 "R9" V 8030 1400 50  0000 C CNN
F 1 "102k" V 7950 1400 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 7880 1400 50  0001 C CNN
F 3 "" H 7950 1400 50  0001 C CNN
	1    7950 1400
	0    1    1    0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R10
U 1 1 59DE7FFC
P 7700 2050
F 0 "R10" V 7780 2050 50  0000 C CNN
F 1 "51k" V 7700 2050 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 7630 2050 50  0001 C CNN
F 3 "" H 7700 2050 50  0001 C CNN
	1    7700 2050
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:C-REM_sleep_analysis-rescue C6
U 1 1 59DE8059
P 7700 1550
F 0 "C6" H 7725 1650 50  0000 L CNN
F 1 "52n" H 7725 1450 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric" H 7738 1400 50  0001 C CNN
F 3 "" H 7700 1550 50  0001 C CNN
	1    7700 1550
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:C-REM_sleep_analysis-rescue C8
U 1 1 59DE80B6
P 7450 2200
F 0 "C8" H 7475 2300 50  0000 L CNN
F 1 "26n" H 7475 2100 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric" H 7488 2050 50  0001 C CNN
F 3 "" H 7450 2200 50  0001 C CNN
	1    7450 2200
	0    1    1    0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:C-REM_sleep_analysis-rescue C7
U 1 1 59DE8118
P 7950 2200
F 0 "C7" H 7975 2300 50  0000 L CNN
F 1 "26n" H 7975 2100 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric" H 7988 2050 50  0001 C CNN
F 3 "" H 7950 2200 50  0001 C CNN
	1    7950 2200
	0    1    1    0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R11
U 1 1 59DE9495
P 950 4500
F 0 "R11" V 1030 4500 50  0000 C CNN
F 1 "21k" V 950 4500 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 880 4500 50  0001 C CNN
F 3 "" H 950 4500 50  0001 C CNN
	1    950  4500
	0    1    1    0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R13
U 1 1 59DE9507
P 1900 5650
F 0 "R13" V 1980 5650 50  0000 C CNN
F 1 "21k" V 1900 5650 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 1830 5650 50  0001 C CNN
F 3 "" H 1900 5650 50  0001 C CNN
	1    1900 5650
	0    1    1    0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:C-REM_sleep_analysis-rescue C10
U 1 1 59DE958F
P 1900 5350
F 0 "C10" H 1925 5450 50  0000 L CNN
F 1 "220n" H 1925 5250 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric" H 1938 5200 50  0001 C CNN
F 3 "" H 1900 5350 50  0001 C CNN
	1    1900 5350
	0    1    1    0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:C-REM_sleep_analysis-rescue C9
U 1 1 59DE960C
P 1100 4800
F 0 "C9" H 1125 4900 50  0000 L CNN
F 1 "220n" H 1125 4700 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric" H 1138 4650 50  0001 C CNN
F 3 "" H 1100 4800 50  0001 C CNN
	1    1100 4800
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:MCP602-REM_sleep_analysis-rescue U1
U 1 1 59DE96F5
P 2000 4600
F 0 "U1" H 2000 4800 50  0000 L CNN
F 1 "MCP602" H 2000 4400 50  0000 L CNN
F 2 "Package_SO:SOIC-8_3.9x4.9mm_P1.27mm" H 2000 4600 50  0001 C CNN
F 3 "" H 2000 4600 50  0001 C CNN
	1    2000 4600
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:R_Variable-REM_sleep_analysis-rescue R12
U 1 1 59DE9994
P 1500 4850
F 0 "R12" V 1600 4750 50  0000 L CNN
F 1 "1.1k" V 1400 4800 50  0000 L CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 1430 4850 50  0001 C CNN
F 3 "" H 1500 4850 50  0001 C CNN
	1    1500 4850
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR05
U 1 1 59DE9E57
P 1900 5000
F 0 "#PWR05" H 1900 4750 50  0001 C CNN
F 1 "GND" H 1900 4850 50  0000 C CNN
F 2 "" H 1900 5000 50  0001 C CNN
F 3 "" H 1900 5000 50  0001 C CNN
	1    1900 5000
	1    0    0    -1  
$EndComp
Text GLabel 1900 4200 1    60   Input ~ 0
+3.0V
Text GLabel 2600 4600 2    60   Input ~ 0
MSP430_INPUT_1.1
Text GLabel 5300 5000 0    60   Input ~ 0
MSP430_INPUT_1.1
Text GLabel 5300 5900 0    60   Input ~ 0
+3.0V
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR06
U 1 1 59DED5E2
P 5350 6200
F 0 "#PWR06" H 5350 5950 50  0001 C CNN
F 1 "GND" H 5350 6050 50  0000 C CNN
F 2 "" H 5350 6200 50  0001 C CNN
F 3 "" H 5350 6200 50  0001 C CNN
	1    5350 6200
	0    1    1    0   
$EndComp
Text Label 7450 1150 0    60   ~ 0
Notch_Filter
Text Label 5800 650  0    60   ~ 0
Low_Pass/Gain
Text Label 1050 4200 0    60   ~ 0
Low_Pass/Gain
Text Label 1400 800  0    60   ~ 0
High_Pass_Filter
Text Label 3400 650  0    60   ~ 0
Instrumentation_Amp
$Comp
L REM_sleep_analysis-rescue:LED-RESCUE-REM_sleep_analysis-REM_sleep_analysis-rescue D2
U 1 1 59DF15BF
P 1200 7200
F 0 "D2" H 1200 7300 50  0000 C CNN
F 1 "LED" H 1200 7100 50  0000 C CNN
F 2 "LED_SMD:LED_1206_3216Metric" H 1200 7200 50  0001 C CNN
F 3 "" H 1200 7200 50  0001 C CNN
F 4 "511-1274-1-ND " H 1200 7200 60  0001 C CNN "PartNumber"
	1    1200 7200
	0    -1   -1   0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:LED-RESCUE-REM_sleep_analysis-REM_sleep_analysis-rescue D1
U 1 1 59DF1835
P 850 7200
F 0 "D1" H 850 7300 50  0000 C CNN
F 1 "LED" H 850 7100 50  0000 C CNN
F 2 "LED_SMD:LED_1206_3216Metric" H 850 7200 50  0001 C CNN
F 3 "" H 850 7200 50  0001 C CNN
F 4 "511-1272-1-ND " H 850 7200 60  0001 C CNN "PartNumber"
	1    850  7200
	0    -1   -1   0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR07
U 1 1 59DF1D1E
P 850 7450
F 0 "#PWR07" H 850 7200 50  0001 C CNN
F 1 "GND" H 850 7300 50  0000 C CNN
F 2 "" H 850 7450 50  0001 C CNN
F 3 "" H 850 7450 50  0001 C CNN
	1    850  7450
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR08
U 1 1 59DF1DCC
P 1200 7450
F 0 "#PWR08" H 1200 7200 50  0001 C CNN
F 1 "GND" H 1200 7300 50  0000 C CNN
F 2 "" H 1200 7450 50  0001 C CNN
F 3 "" H 1200 7450 50  0001 C CNN
	1    1200 7450
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R16
U 1 1 59DF282E
P 850 6850
F 0 "R16" V 930 6850 50  0000 C CNN
F 1 "500" V 850 6850 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 780 6850 50  0001 C CNN
F 3 "" H 850 6850 50  0001 C CNN
	1    850  6850
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R17
U 1 1 59DF290C
P 1200 6850
F 0 "R17" V 1280 6850 50  0000 C CNN
F 1 "500" V 1200 6850 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 1130 6850 50  0001 C CNN
F 3 "" H 1200 6850 50  0001 C CNN
	1    1200 6850
	1    0    0    -1  
$EndComp
Text GLabel 850  6550 1    60   Input ~ 0
LED1
Text GLabel 1200 6550 1    60   Input ~ 0
LED2
Text GLabel 5300 5300 0    60   Output ~ 0
LED1
Text GLabel 5300 5400 0    60   Output ~ 0
LED2
$Comp
L REM_sleep_analysis-rescue:Conn_01x03-REM_sleep_analysis-rescue J3
U 1 1 59E5210F
P 600 1300
F 0 "J3" H 600 1500 50  0000 C CNN
F 1 "Conn_01x03" H 600 1100 50  0000 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x03_P2.54mm_Horizontal" H 600 1300 50  0001 C CNN
F 3 "" H 600 1300 50  0001 C CNN
	1    600  1300
	-1   0    0    -1  
$EndComp
Text GLabel 950  1300 2    60   Input ~ 0
Reference
$Comp
L REM_sleep_analysis-rescue:MSP430G2553_PW20-REM_sleep_analysis-rescue U3
U 1 1 59F61A01
P 7500 5700
F 0 "U3" H 8950 6600 60  0000 C CNN
F 1 "MSP430G2553_PW20" H 6850 6650 60  0000 C CNN
F 2 "Package_SO:TSSOP-20_4.4x6.5mm_P0.65mm" H 7500 5700 60  0001 C CNN
F 3 "" H 7500 5700 60  0001 C CNN
	1    7500 5700
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:Conn_01x06_Female-REM_sleep_analysis-rescue J0
U 1 1 59F65D2C
P 3400 7200
F 0 "J0" H 3400 7500 50  0000 C CNN
F 1 "Conn_01x06_Female" H 3400 6800 50  0000 C CNN
F 2 "Connector_PinSocket_2.54mm:PinSocket_1x06_P2.54mm_Vertical" H 3400 7200 50  0001 C CNN
F 3 "" H 3400 7200 50  0001 C CNN
	1    3400 7200
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R30
U 1 1 59F65E55
P 9950 5950
F 0 "R30" V 10030 5950 50  0000 C CNN
F 1 "47k" V 9950 5950 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 9880 5950 50  0001 C CNN
F 3 "" H 9950 5950 50  0001 C CNN
	1    9950 5950
	0    1    1    0   
$EndComp
Text GLabel 10300 5950 2    60   Input ~ 0
+3.0V
Text GLabel 10300 6200 2    60   Input ~ 0
TEST
Text GLabel 10300 5800 2    60   Input ~ 0
RST
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR09
U 1 1 59F67F0E
P 3000 7500
F 0 "#PWR09" H 3000 7250 50  0001 C CNN
F 1 "GND" H 3000 7350 50  0000 C CNN
F 2 "" H 3000 7500 50  0001 C CNN
F 3 "" H 3000 7500 50  0001 C CNN
	1    3000 7500
	0    1    1    0   
$EndComp
Text GLabel 3000 7200 0    60   Input ~ 0
+3.0V
Text GLabel 3000 7300 0    60   Input ~ 0
TEST
Text GLabel 3000 7400 0    60   Input ~ 0
RST
Text GLabel 5300 5100 0    60   Input ~ 0
TXD
Text GLabel 5300 5200 0    60   Output ~ 0
RXD
Text Label 2650 6800 0    60   ~ 0
Connector_For_Programming_MCU
$Comp
L REM_sleep_analysis-rescue:C-REM_sleep_analysis-rescue C13
U 1 1 59F646B1
P 10600 2100
F 0 "C13" H 10625 2200 50  0000 L CNN
F 1 "100nF" H 10625 2000 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric" H 10638 1950 50  0001 C CNN
F 3 "" H 10600 2100 50  0001 C CNN
	1    10600 2100
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:C-REM_sleep_analysis-rescue C14
U 1 1 59F64760
P 10900 2100
F 0 "C14" H 10925 2200 50  0000 L CNN
F 1 "4.7uF" H 10925 2000 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric" H 10938 1950 50  0001 C CNN
F 3 "" H 10900 2100 50  0001 C CNN
	1    10900 2100
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR010
U 1 1 59F647FE
P 10900 2350
F 0 "#PWR010" H 10900 2100 50  0001 C CNN
F 1 "GND" H 10900 2200 50  0000 C CNN
F 2 "" H 10900 2350 50  0001 C CNN
F 3 "" H 10900 2350 50  0001 C CNN
	1    10900 2350
	1    0    0    -1  
$EndComp
Text GLabel 11100 1950 1    60   Input ~ 0
+3.0V
$Comp
L REM_sleep_analysis-rescue:USB_OTG-REM_sleep_analysis-rescue J2
U 1 1 59F68B85
P 4000 3500
F 0 "J2" H 3800 3950 50  0000 L CNN
F 1 "USB_OTG" H 3800 3850 50  0000 L CNN
F 2 "Connector_USB:USB_Micro-B_Molex_47346-0001" H 4150 3450 50  0001 C CNN
F 3 "" H 4150 3450 50  0001 C CNN
	1    4000 3500
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR011
U 1 1 59F68C3F
P 4000 4000
F 0 "#PWR011" H 4000 3750 50  0001 C CNN
F 1 "GND" H 4000 3850 50  0000 C CNN
F 2 "" H 4000 4000 50  0001 C CNN
F 3 "" H 4000 4000 50  0001 C CNN
	1    4000 4000
	1    0    0    -1  
$EndComp
Text GLabel 4500 3300 2    60   Input ~ 0
+5V
Text GLabel 6100 4600 3    60   Input ~ 0
+5V
$Comp
L REM_sleep_analysis-rescue:D-REM_sleep_analysis-rescue D3
U 1 1 59F6EA16
P 6100 3750
F 0 "D3" H 6100 3850 50  0000 C CNN
F 1 "D" H 6100 3650 50  0000 C CNN
F 2 "Diode_SMD:D_0603_1608Metric" H 6100 3750 50  0001 C CNN
F 3 "" H 6100 3750 50  0001 C CNN
	1    6100 3750
	0    1    1    0   
$EndComp
Text GLabel 6250 2600 2    60   Input ~ 0
+3.0V
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR012
U 1 1 59FA3E34
P 1550 7450
F 0 "#PWR012" H 1550 7200 50  0001 C CNN
F 1 "GND" H 1550 7300 50  0000 C CNN
F 2 "" H 1550 7450 50  0001 C CNN
F 3 "" H 1550 7450 50  0001 C CNN
	1    1550 7450
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR013
U 1 1 59FA3EF6
P 1900 7450
F 0 "#PWR013" H 1900 7200 50  0001 C CNN
F 1 "GND" H 1900 7300 50  0000 C CNN
F 2 "" H 1900 7450 50  0001 C CNN
F 3 "" H 1900 7450 50  0001 C CNN
	1    1900 7450
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:LED-RESCUE-REM_sleep_analysis-REM_sleep_analysis-rescue D4
U 1 1 59FA41F0
P 1550 7200
F 0 "D4" H 1550 7300 50  0000 C CNN
F 1 "LED" H 1550 7100 50  0000 C CNN
F 2 "LED_THT:LED_D3.0mm" H 1550 7200 50  0001 C CNN
F 3 "" H 1550 7200 50  0001 C CNN
F 4 " C503B-RCN-CW0Z0AA1-ND " H 1550 7200 60  0001 C CNN "PartNumber"
	1    1550 7200
	0    -1   -1   0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:LED-RESCUE-REM_sleep_analysis-REM_sleep_analysis-rescue D5
U 1 1 59FA42DA
P 1900 7200
F 0 "D5" H 1900 7300 50  0000 C CNN
F 1 "LED" H 1900 7100 50  0000 C CNN
F 2 "LED_THT:LED_D3.0mm" H 1900 7200 50  0001 C CNN
F 3 "" H 1900 7200 50  0001 C CNN
F 4 " C503B-RCN-CW0Z0AA1-ND " H 1900 7200 60  0001 C CNN "PartNumber"
	1    1900 7200
	0    -1   -1   0   
$EndComp
Text GLabel 1550 6550 1    60   Input ~ 0
LED3
Text GLabel 10050 5100 2    60   Output ~ 0
LED3
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R29
U 1 1 59FA33D1
P 1900 6850
F 0 "R29" V 1980 6850 50  0000 C CNN
F 1 "6" V 1900 6850 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 1830 6850 50  0001 C CNN
F 3 "" H 1900 6850 50  0001 C CNN
	1    1900 6850
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R28
U 1 1 59FA32B1
P 1550 6850
F 0 "R28" V 1630 6850 50  0000 C CNN
F 1 "6" V 1550 6850 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 1480 6850 50  0001 C CNN
F 3 "" H 1550 6850 50  0001 C CNN
	1    1550 6850
	1    0    0    1   
$EndComp
Text GLabel 5700 1850 3    60   Input ~ 0
1.5V
Text GLabel 7900 1800 3    60   Input ~ 0
1.5V
Text GLabel 1900 1800 3    60   Input ~ 0
1.5V
Text GLabel 1900 1350 2    60   Input ~ 0
1.5V
Text GLabel 1500 5100 3    60   Input ~ 0
1.5V
Text GLabel 1100 5050 3    60   Input ~ 0
1.5V
Text GLabel 5400 1700 3    60   Input ~ 0
1.5V
Text GLabel 4300 1650 3    60   Input ~ 0
1.5V
$Comp
L REM_sleep_analysis-rescue:MCP602-REM_sleep_analysis-rescue U4
U 2 1 5A020FC6
P 2850 3400
F 0 "U4" H 2850 3600 50  0000 L CNN
F 1 "MCP602" H 2850 3200 50  0000 L CNN
F 2 "Package_SO:SOIC-8_3.9x4.9mm_P1.27mm" H 2850 3400 50  0001 C CNN
F 3 "" H 2850 3400 50  0001 C CNN
	2    2850 3400
	1    0    0    -1  
$EndComp
Text GLabel 2750 2950 1    60   Input ~ 0
+3.0V
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR014
U 1 1 5A022546
P 2750 3750
F 0 "#PWR014" H 2750 3500 50  0001 C CNN
F 1 "GND" H 2750 3600 50  0000 C CNN
F 2 "" H 2750 3750 50  0001 C CNN
F 3 "" H 2750 3750 50  0001 C CNN
	1    2750 3750
	1    0    0    -1  
$EndComp
Text GLabel 3250 3400 2    60   Input ~ 0
1.5V
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R32
U 1 1 5A022F23
P 2250 3100
F 0 "R32" V 2330 3100 50  0000 C CNN
F 1 "100k" V 2250 3100 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 2180 3100 50  0001 C CNN
F 3 "" H 2250 3100 50  0001 C CNN
	1    2250 3100
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R33
U 1 1 5A023025
P 2250 3500
F 0 "R33" V 2330 3500 50  0000 C CNN
F 1 "100k" V 2250 3500 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 2180 3500 50  0001 C CNN
F 3 "" H 2250 3500 50  0001 C CNN
	1    2250 3500
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR015
U 1 1 5A02371A
P 2250 3750
F 0 "#PWR015" H 2250 3500 50  0001 C CNN
F 1 "GND" H 2250 3600 50  0000 C CNN
F 2 "" H 2250 3750 50  0001 C CNN
F 3 "" H 2250 3750 50  0001 C CNN
	1    2250 3750
	1    0    0    -1  
$EndComp
Text GLabel 2250 2900 1    60   Input ~ 0
+3.0V
$Comp
L REM_sleep_analysis-rescue:D-REM_sleep_analysis-rescue D6
U 1 1 5A02E2A0
P 6100 4200
F 0 "D6" H 6100 4300 50  0000 C CNN
F 1 "D" H 6100 4100 50  0000 C CNN
F 2 "Diode_SMD:D_0603_1608Metric" H 6100 4200 50  0001 C CNN
F 3 "" H 6100 4200 50  0001 C CNN
	1    6100 4200
	0    1    1    0   
$EndComp
Text GLabel 5400 3800 1    60   Input ~ 0
Batt
Text GLabel 8150 2650 0    60   Input ~ 0
RESET
Text GLabel 5300 5600 0    60   Output ~ 0
RESET
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R15
U 1 1 5A74F95D
P 6650 3500
F 0 "R15" V 6730 3500 50  0000 C CNN
F 1 "10k" V 6650 3500 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 6580 3500 50  0001 C CNN
F 3 "" H 6650 3500 50  0001 C CNN
	1    6650 3500
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR016
U 1 1 5A74FA85
P 6650 3750
F 0 "#PWR016" H 6650 3500 50  0001 C CNN
F 1 "GND" H 6650 3600 50  0000 C CNN
F 2 "" H 6650 3750 50  0001 C CNN
F 3 "" H 6650 3750 50  0001 C CNN
	1    6650 3750
	1    0    0    -1  
$EndComp
Text Label 5100 2600 0    60   ~ 0
Battery_Regulation
Text GLabel 3000 7000 0    60   Input ~ 0
TXD
Text GLabel 3000 7100 0    60   Input ~ 0
RXD
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R31
U 1 1 5A7BFFB4
P 3300 6200
F 0 "R31" V 3380 6200 50  0000 C CNN
F 1 "10k" V 3300 6200 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 3230 6200 50  0001 C CNN
F 3 "" H 3300 6200 50  0001 C CNN
	1    3300 6200
	-1   0    0    1   
$EndComp
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR017
U 1 1 5A7C0089
P 3850 6450
F 0 "#PWR017" H 3850 6200 50  0001 C CNN
F 1 "GND" H 3850 6300 50  0000 C CNN
F 2 "" H 3850 6450 50  0001 C CNN
F 3 "" H 3850 6450 50  0001 C CNN
	1    3850 6450
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR018
U 1 1 5A7C010D
P 3300 6450
F 0 "#PWR018" H 3300 6200 50  0001 C CNN
F 1 "GND" H 3300 6300 50  0000 C CNN
F 2 "" H 3300 6450 50  0001 C CNN
F 3 "" H 3300 6450 50  0001 C CNN
	1    3300 6450
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:Conn_01x02_Female-REM_sleep_analysis-rescue J4
U 1 1 5A7C0191
P 4050 5550
F 0 "J4" H 4050 5650 50  0000 C CNN
F 1 "Conn_01x02_Female" H 4350 5750 50  0000 C CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x02_P2.54mm_Vertical" H 4050 5550 50  0001 C CNN
F 3 "" H 4050 5550 50  0001 C CNN
	1    4050 5550
	1    0    0    -1  
$EndComp
Text GLabel 3850 5400 1    60   Input ~ 0
+3.0V
Text GLabel 5300 5700 0    60   Output ~ 0
MOTOR
Text GLabel 3100 6050 0    60   Input ~ 0
MOTOR
$Comp
L REM_sleep_analysis-rescue:D-REM_sleep_analysis-rescue D7
U 1 1 5A7C0E93
P 3500 5600
F 0 "D7" H 3500 5700 50  0000 C CNN
F 1 "D" H 3500 5500 50  0000 C CNN
F 2 "Diode_SMD:D_0603_1608Metric" H 3500 5600 50  0001 C CNN
F 3 "" H 3500 5600 50  0001 C CNN
	1    3500 5600
	0    1    1    0   
$EndComp
Text Notes 2800 5100 0    60   ~ 0
Motor/Vibrator Driving Circuit\n\n
$Comp
L REM_sleep_analysis-rescue:TEST-REM_sleep_analysis-rescue TP1
U 1 1 5A7C5EA6
P 2450 4500
F 0 "TP1" H 2450 4800 50  0000 C BNN
F 1 "TEST" H 2450 4750 50  0000 C CNN
F 2 "Connector_PinSocket_2.54mm:PinSocket_1x01_P2.54mm_Vertical" H 2450 4500 50  0001 C CNN
F 3 "" H 2450 4500 50  0001 C CNN
	1    2450 4500
	1    0    0    -1  
$EndComp
Wire Wire Line
	1600 1000 1900 1000
Wire Wire Line
	1600 1500 1900 1500
Wire Wire Line
	2800 1200 2700 1200
Wire Wire Line
	2700 1200 2700 1150
Wire Wire Line
	2700 1150 2600 1150
Wire Wire Line
	2800 1400 2700 1400
Wire Wire Line
	2700 1400 2700 1450
Wire Wire Line
	2700 1450 2600 1450
Connection ~ 1900 1500
Wire Wire Line
	2800 1000 2800 1100
Connection ~ 1900 1000
Wire Wire Line
	3500 1300 3750 1300
Wire Wire Line
	4050 1300 4300 1300
Wire Wire Line
	4750 4100 5200 4100
Wire Wire Line
	5200 4100 5200 3950
Wire Wire Line
	4750 4200 4750 4400
Wire Wire Line
	4750 4400 5200 4400
Connection ~ 4300 1300
Wire Wire Line
	5100 1300 5400 1300
Wire Wire Line
	5700 1500 5850 1500
Connection ~ 5400 1300
Wire Wire Line
	5950 2300 5900 2300
Wire Wire Line
	6250 2100 6250 2200
Wire Wire Line
	6500 1400 6500 2200
Wire Wire Line
	6500 2200 6250 2200
Connection ~ 6250 2200
Wire Wire Line
	7300 1400 7300 2200
Wire Wire Line
	7600 1400 7700 1400
Connection ~ 7700 1400
Wire Wire Line
	7700 1700 7700 1800
Wire Wire Line
	7900 1800 7700 1800
Connection ~ 7700 1800
Wire Wire Line
	7600 2200 7700 2200
Connection ~ 7700 2200
Wire Wire Line
	600  2450 600  4500
Wire Wire Line
	1100 4500 1100 4650
Wire Wire Line
	1500 4700 1650 4700
Wire Wire Line
	1500 5000 1500 5100
Wire Wire Line
	1700 4500 1100 4500
Wire Wire Line
	1650 4700 1650 5350
Wire Wire Line
	1650 5350 1750 5350
Connection ~ 1650 4700
Wire Wire Line
	1650 5650 1750 5650
Connection ~ 1650 5350
Wire Wire Line
	2050 5350 2300 5350
Wire Wire Line
	5300 5000 5600 5000
Wire Wire Line
	5350 6200 5600 6200
Wire Wire Line
	1900 4900 1900 5000
Wire Wire Line
	850  7350 850  7450
Wire Wire Line
	1200 7350 1200 7450
Wire Wire Line
	850  7000 850  7050
Wire Wire Line
	1200 7000 1200 7050
Wire Wire Line
	1200 6550 1200 6700
Wire Wire Line
	850  6550 850  6700
Wire Wire Line
	8200 2450 8200 1400
Wire Wire Line
	8200 1400 8150 1400
Wire Wire Line
	6100 1100 6100 1000
Wire Wire Line
	1300 1000 1200 1000
Wire Wire Line
	1000 1000 1000 1200
Wire Wire Line
	1000 1500 1200 1500
Wire Wire Line
	3200 1600 3200 1800
Wire Wire Line
	6100 1800 6100 1700
Wire Wire Line
	5200 4400 5200 4500
Wire Wire Line
	1000 1200 800  1200
Wire Wire Line
	5300 5900 5600 5900
Wire Wire Line
	9500 5950 9700 5950
Wire Wire Line
	10100 5950 10300 5950
Wire Wire Line
	9500 6200 10300 6200
Wire Wire Line
	10300 5800 9700 5800
Wire Wire Line
	9700 5600 9700 5800
Connection ~ 9700 5950
Wire Wire Line
	5300 5100 5600 5100
Wire Wire Line
	5300 5200 5600 5200
Wire Wire Line
	10400 1950 10600 1950
Connection ~ 10600 1950
Wire Wire Line
	10600 2250 10900 2250
Wire Wire Line
	10900 2250 10900 2350
Connection ~ 10900 1950
Wire Wire Line
	4000 3900 4000 4000
Wire Wire Line
	6100 4350 6100 4450
Wire Wire Line
	5700 1800 5700 1850
Wire Wire Line
	1900 1350 1900 1300
Wire Wire Line
	1100 4950 1100 5050
Wire Wire Line
	4300 1600 4300 1650
Wire Wire Line
	5400 1600 5400 1700
Wire Wire Line
	2750 2950 2750 3100
Wire Wire Line
	2750 3750 2750 3700
Wire Wire Line
	3150 3400 3250 3400
Wire Wire Line
	3250 3400 3250 4000
Wire Wire Line
	3250 4000 2500 4000
Wire Wire Line
	2500 4000 2500 3500
Wire Wire Line
	2500 3500 2550 3500
Wire Wire Line
	2550 3300 2250 3300
Wire Wire Line
	2250 3250 2250 3300
Connection ~ 2250 3300
Wire Wire Line
	2250 2950 2250 2900
Wire Wire Line
	2250 3750 2250 3650
Wire Wire Line
	1550 7350 1550 7450
Wire Wire Line
	1900 7350 1900 7450
Wire Wire Line
	1900 7000 1900 7050
Wire Wire Line
	2300 5650 2050 5650
Wire Wire Line
	1550 7000 1550 7050
Wire Wire Line
	3000 7000 3200 7000
Wire Wire Line
	3000 7100 3200 7100
Wire Wire Line
	3000 7200 3200 7200
Wire Wire Line
	3000 7400 3200 7400
Wire Wire Line
	800  1300 950  1300
Wire Wire Line
	1000 1500 1000 1400
Wire Wire Line
	1000 1400 800  1400
Wire Wire Line
	5300 5300 5600 5300
Wire Wire Line
	5600 5400 5300 5400
Wire Wire Line
	5600 5600 5300 5600
Wire Wire Line
	6100 3900 6100 3950
Connection ~ 6100 3950
Wire Wire Line
	6100 3600 6100 3550
Wire Wire Line
	6400 4450 6100 4450
Connection ~ 6100 4450
Connection ~ 6400 3350
Wire Wire Line
	6400 3350 6400 4450
Wire Wire Line
	6650 3650 6650 3750
Wire Wire Line
	6400 3350 6650 3350
Wire Wire Line
	6100 2600 6250 2600
Wire Wire Line
	3000 7300 3200 7300
Wire Wire Line
	3000 7500 3200 7500
Wire Wire Line
	3100 6050 3300 6050
Connection ~ 3300 6050
Wire Wire Line
	5300 5700 5600 5700
Wire Wire Line
	3850 6250 3850 6450
Wire Wire Line
	3300 6350 3300 6450
Wire Wire Line
	3850 5650 3850 5750
Wire Wire Line
	3850 5400 3850 5450
Wire Wire Line
	3500 5450 3850 5450
Connection ~ 3850 5450
Wire Wire Line
	3500 5750 3850 5750
Connection ~ 3850 5750
Wire Wire Line
	4300 3500 4500 3500
Wire Wire Line
	4300 3600 4500 3600
Wire Wire Line
	2450 4500 2450 4600
Text Notes 10600 7650 0    60   ~ 0
2.0\n
Text Notes 8150 7650 0    60   ~ 0
02-07-2018\n
Text Notes 7350 7500 0    60   ~ 0
NDSU SD1717 REM SLEEP ANALYSIS\n
$Comp
L REM_sleep_analysis-rescue:Q_PMOS_GSD-REM_sleep_analysis-rescue Q1
U 1 1 5A8233A9
P 6200 3350
F 0 "Q1" H 6400 3400 50  0000 L CNN
F 1 "Q_PMOS_GSD" H 6400 3300 50  0000 L CNN
F 2 "Package_TO_SOT_SMD:SOT-23" H 6400 3450 50  0001 C CNN
F 3 "" H 6200 3350 50  0001 C CNN
	1    6200 3350
	-1   0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:Q_NMOS_GSD-REM_sleep_analysis-rescue Q2
U 1 1 5A82407A
P 3750 6050
F 0 "Q2" H 3950 6100 50  0000 L CNN
F 1 "Q_NMOS_GSD" H 3950 6000 50  0000 L CNN
F 2 "Package_TO_SOT_SMD:SOT-723" H 3950 6150 50  0001 C CNN
F 3 "" H 3750 6050 50  0001 C CNN
	1    3750 6050
	1    0    0    -1  
$EndComp
Text GLabel 5300 5500 0    60   Input ~ 0
MSP430_INPUT_1.1
Wire Wire Line
	5300 5500 5600 5500
Wire Wire Line
	10050 5100 9500 5100
$Comp
L REM_sleep_analysis-rescue:sw_slide_7x3-REM_sleep_analysis-rescue S1
U 1 1 5AC2F761
P 6450 3150
F 0 "S1" H 6450 3250 60  0000 C CNN
F 1 "sw_slide_7x3" H 6450 3600 60  0000 C CNN
F 2 "KPS-1290:sw_slide_7x3" H 6450 3600 60  0001 C CNN
F 3 "" H 6450 3600 60  0001 C CNN
	1    6450 3150
	1    0    0    -1  
$EndComp
Wire Wire Line
	6100 2800 6300 2800
Wire Wire Line
	6100 3150 6100 2900
Wire Wire Line
	6100 2900 6300 2900
Wire Wire Line
	6100 2600 6100 2800
$Comp
L REM_sleep_analysis-rescue:Conn_01x02_Female-REM_sleep_analysis-rescue j5
U 1 1 5AC320FC
P 10500 5500
F 0 "j5" H 10500 5600 50  0000 C CNN
F 1 "Conn_01x02_Female" H 10500 5300 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" H 10500 5500 50  0001 C CNN
F 3 "" H 10500 5500 50  0001 C CNN
	1    10500 5500
	1    0    0    -1  
$EndComp
Wire Wire Line
	9700 5600 10300 5600
Connection ~ 9700 5800
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR0101
U 1 1 5AC32644
P 10300 5500
F 0 "#PWR0101" H 10300 5250 50  0001 C CNN
F 1 "GND" H 10100 5500 50  0000 C CNN
F 2 "" H 10300 5500 50  0001 C CNN
F 3 "" H 10300 5500 50  0001 C CNN
	1    10300 5500
	0    1    1    0   
$EndComp
$Comp
L REM_sleep_analysis-rescue:RN4020-REM_sleep_analysis-rescue U5
U 1 1 5AC3B9BA
P 9700 3250
F 0 "U5" H 9200 4700 60  0000 L CNN
F 1 "RN4020" H 9200 2000 60  0000 L CNN
F 2 "RN4020:RN4020" H 9500 3250 60  0001 C CNN
F 3 "" H 9500 3250 60  0000 C CNN
	1    9700 3250
	-1   0    0    -1  
$EndComp
Text GLabel 8800 1950 0    60   Input ~ 0
TXD
Text GLabel 8800 2050 0    60   Input ~ 0
RXD
Wire Wire Line
	8150 2650 8250 2650
Wire Wire Line
	8900 2050 8800 2050
Wire Wire Line
	8900 1950 8800 1950
Text GLabel 8800 2450 0    60   Input ~ 0
MLDP
Wire Wire Line
	8900 2450 8800 2450
Text GLabel 10000 5000 2    60   Output ~ 0
MLDP
Wire Wire Line
	9500 5000 10000 5000
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR0102
U 1 1 5AC3C950
P 10400 4500
F 0 "#PWR0102" H 10400 4250 50  0001 C CNN
F 1 "GND" H 10400 4350 50  0000 C CNN
F 2 "" H 10400 4500 50  0001 C CNN
F 3 "" H 10400 4500 50  0001 C CNN
	1    10400 4500
	-1   0    0    -1  
$EndComp
Wire Wire Line
	10400 4050 10400 4150
Connection ~ 10400 4150
Connection ~ 10400 4250
Connection ~ 10400 4350
Text GLabel 8800 2750 0    60   Input ~ 0
BT_WAKE
Text GLabel 9750 5500 2    60   Output ~ 0
BT_WAKE
Wire Wire Line
	8900 2750 8800 2750
$Comp
L REM_sleep_analysis-rescue:R-REM_sleep_analysis-rescue R14
U 1 1 5AC3E1BE
P 8250 2800
F 0 "R14" V 8330 2800 50  0000 C CNN
F 1 "10k" V 8250 2800 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric" V 8180 2800 50  0001 C CNN
F 3 "" H 8250 2800 50  0001 C CNN
	1    8250 2800
	1    0    0    -1  
$EndComp
$Comp
L REM_sleep_analysis-rescue:GND-REM_sleep_analysis-rescue #PWR0103
U 1 1 5AC3E5EE
P 8250 3050
F 0 "#PWR0103" H 8250 2800 50  0001 C CNN
F 1 "GND" H 8250 2900 50  0000 C CNN
F 2 "" H 8250 3050 50  0001 C CNN
F 3 "" H 8250 3050 50  0001 C CNN
	1    8250 3050
	1    0    0    -1  
$EndComp
Connection ~ 8250 2650
Wire Wire Line
	8250 2950 8250 3050
Text GLabel 8800 2250 0    60   Input ~ 0
RTS
Text GLabel 8800 2150 0    60   Input ~ 0
CTS
Text GLabel 9750 5400 2    60   BiDi ~ 0
RTS
Text GLabel 9750 5300 2    60   BiDi ~ 0
CTS
Wire Wire Line
	9500 5300 9750 5300
Wire Wire Line
	9500 5400 9750 5400
Wire Wire Line
	8800 2150 8900 2150
Wire Wire Line
	8800 2250 8900 2250
Text GLabel 8600 3950 0    60   Input ~ 0
MSP430_INPUT_1.1
Wire Wire Line
	8900 3950 8600 3950
Wire Wire Line
	1900 1500 2800 1500
Wire Wire Line
	1900 1000 2800 1000
Wire Wire Line
	4300 1300 4800 1300
Wire Wire Line
	5400 1300 5900 1300
Wire Wire Line
	6250 2200 6250 2300
Wire Wire Line
	7700 1400 7800 1400
Wire Wire Line
	7700 1800 7700 1900
Wire Wire Line
	7700 2200 7800 2200
Wire Wire Line
	1650 4700 1700 4700
Wire Wire Line
	1650 5350 1650 5650
Wire Wire Line
	9700 5950 9800 5950
Wire Wire Line
	10600 1950 10900 1950
Wire Wire Line
	10900 1950 11100 1950
Wire Wire Line
	2250 3300 2250 3350
Wire Wire Line
	2300 5350 2300 5650
Wire Wire Line
	6100 3950 6100 4050
Wire Wire Line
	6100 4450 6100 4600
Wire Wire Line
	3300 6050 3550 6050
Wire Wire Line
	3850 5450 3850 5550
Wire Wire Line
	3850 5750 3850 5850
Wire Wire Line
	2450 4600 2600 4600
Wire Wire Line
	9700 5800 9700 5950
Wire Wire Line
	10400 4150 10400 4250
Wire Wire Line
	10400 4250 10400 4350
Wire Wire Line
	10400 4350 10400 4500
Wire Wire Line
	8250 2650 8900 2650
Wire Wire Line
	9500 5500 9750 5500
Wire Wire Line
	9500 5200 10050 5200
Text GLabel 10050 5200 2    60   Output ~ 0
LED4
Text GLabel 1900 6550 1    60   Input ~ 0
LED4
Wire Wire Line
	1550 6700 1550 6550
Wire Wire Line
	1900 6550 1900 6700
Wire Wire Line
	7300 1400 6500 1400
Connection ~ 7300 1400
Connection ~ 6500 1400
Wire Wire Line
	2450 4600 2300 4600
Connection ~ 2450 4600
Wire Wire Line
	2300 5350 2300 4700
Wire Wire Line
	2300 4700 2450 4700
Wire Wire Line
	2450 4700 2450 4600
Connection ~ 2300 5350
Wire Wire Line
	5950 2100 5900 2100
Wire Wire Line
	5900 2100 5900 2300
Wire Wire Line
	5900 2100 5900 1550
Wire Wire Line
	5900 1550 5850 1550
Wire Wire Line
	5850 1550 5850 1500
Connection ~ 5900 2100
Connection ~ 5850 1500
Wire Wire Line
	5850 1500 5900 1500
Wire Wire Line
	8100 2200 8150 2200
Wire Wire Line
	8150 2200 8150 1400
Connection ~ 8150 1400
Wire Wire Line
	8150 1400 8100 1400
Wire Wire Line
	600  4500 800  4500
Wire Wire Line
	1900 4300 1900 4200
Wire Wire Line
	4500 3300 4300 3300
Wire Wire Line
	600  2450 8200 2450
Wire Wire Line
	3100 1000 3100 900 
Wire Wire Line
	1200 1550 1200 1500
Connection ~ 1200 1500
Wire Wire Line
	1200 1500 1300 1500
Wire Wire Line
	1200 950  1200 1000
Connection ~ 1200 1000
Wire Wire Line
	1200 1000 1000 1000
Wire Wire Line
	5200 3950 5400 3950
Wire Wire Line
	5400 3800 5400 3950
Connection ~ 5400 3950
Wire Wire Line
	5400 3950 6100 3950
$EndSCHEMATC
