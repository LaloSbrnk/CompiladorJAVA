option casemap :none
include \masm32\include\masm32rt.inc
includelib \masm32\lib\masm32.lib
printf PROTO C :VARARG

.DATA
_new_line_ DB 13, 10, 0
_format_long DB "%d", 13, 10, 0
_format_dfloat DB "%.20Lf", 13, 10, 0
_format_string DB "%s", 13, 10, 0
_MSG_ERROR_DIV_CERO DB "ERROR EN TIEMPO DE EJECUCION: Division por cero.", 0
_MSG_ERROR_DIV_CERO_FLOAT DB "ERROR EN TIEMPO DE EJECUCION: Division por cero (flotante).", 0
_MSG_ERROR_OVERFLOW_PROD DB "ERROR EN TIEMPO DE EJECUCION: Overflow en producto de enteros.", 0
_MSG_ERROR_RECURSION DB "ERROR EN TIEMPO DE EJECUCION: Recursion no permitida.", 0
_aux_long DD ?
_aux_dfloat DQ ?

; --- Variables y Constantes del Programa ---
_TOTAL_FLOAT DQ ?
_12_5D_0 DQ 12.5
_Sumando_10__Long__al_resultado__debe_ser_16_25__ DB "Sumando 10 (Long) al resultado (debe ser 16.25):", 0
_A_vale__12_5__ DB "A vale (12.5):", 0
_B_FLOAT DQ ?
_A___B__debe_ser_6_25__ DB "A * B (debe ser 6.25):", 0
_0_5D_0 DQ 0.5
____Test_de_Punto_Flotante_y_Conversiones____ DB "--- Test de Punto Flotante y Conversiones ---", 0
_15_0D_0 DQ 15.0
_ENTERO_FLOAT DD ?
_A_FLOAT DQ ?
_Correcto__El_resultado_es_mayor_a_15_0 DB "Correcto: El resultado es mayor a 15.0", 0
_Error__La_comparacion_fallo DB "Error: La comparacion fallo", 0
; --- Fin Variables y Constantes ---


.CODE
START:
FINIT
invoke printf, ADDR _format_string, ADDR ____Test_de_Punto_Flotante_y_Conversiones____
FLD _12_5D_0
FSTP _A_FLOAT
FLD _0_5D_0
FSTP _B_FLOAT
MOV EAX, 10
MOV _ENTERO_FLOAT, EAX
invoke printf, ADDR _format_string, ADDR _A_vale__12_5__
invoke printf, ADDR _format_dfloat, _A_FLOAT
FLD _A_FLOAT
FLD _B_FLOAT
FMUL
FSTP _aux_dfloat
FLD _aux_dfloat
FSTP _TOTAL_FLOAT
invoke printf, ADDR _format_string, ADDR _A___B__debe_ser_6_25__
invoke printf, ADDR _format_dfloat, _TOTAL_FLOAT
FLD _TOTAL_FLOAT
FILD _ENTERO_FLOAT
FADD
FSTP _aux_dfloat
FLD _aux_dfloat
FSTP _TOTAL_FLOAT
invoke printf, ADDR _format_string, ADDR _Sumando_10__Long__al_resultado__debe_ser_16_25__
invoke printf, ADDR _format_dfloat, _TOTAL_FLOAT
FLD _TOTAL_FLOAT
FLD _15_0D_0
FCOMIP ST(0), ST(1)
FFREE ST(0)
JNA _label0
invoke printf, ADDR _format_string, ADDR _Correcto__El_resultado_es_mayor_a_15_0
JMP _label1
_label0:
invoke printf, ADDR _format_string, ADDR _Error__La_comparacion_fallo
_label1:

; --- Fin del programa principal ---
invoke ExitProcess, 0


; --- Rutinas de Error en Tiempo de Ejecucion ---
_ERROR_DIV_CERO:
invoke printf, ADDR _MSG_ERROR_DIV_CERO
invoke ExitProcess, 1
_ERROR_DIV_CERO_FLOAT:
invoke printf, ADDR _MSG_ERROR_DIV_CERO_FLOAT
invoke ExitProcess, 1
_ERROR_OVERFLOW_PROD:
invoke printf, ADDR _MSG_ERROR_OVERFLOW_PROD
invoke ExitProcess, 1
_ERROR_RECURSION:
invoke printf, ADDR _MSG_ERROR_RECURSION
invoke ExitProcess, 1

END START
