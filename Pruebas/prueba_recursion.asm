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
_AUX_RECURSION_FACT DD ?
_N_RECURSION_FACT DD ?
_RESULTADO_RECURSION DD ?
_Calculando_factorial_de_5___ DB "Calculando factorial de 5...", 0
____Test_de_Recursividad__Factorial_____ DB "--- Test de Recursividad (Factorial) ---", 0
_El_resultado_deberia_ser_120__Obtenido_ DB "El resultado deberia ser 120. Obtenido:", 0
_IN_FUNC__FACT_RECURSION DB 0
_RET_0__FACT_RECURSION DD ?
; --- Fin Variables y Constantes ---


.CODE

; --- Definicion de Funcion: FACT ---
_FACT_RECURSION PROC
push ebp
mov ebp, esp
push edi
push esi
MOV EAX, _N_RECURSION_FACT
CMP EAX, 1
JG _label0
MOV EAX, 1
MOV _RET_0__FACT_RECURSION, EAX
JMP _FACT_RECURSION_exit
JMP _label1
_label0:
; Chequeo de recursion (h)
CMP _IN_FUNC__FACT_RECURSION, 1
JE _ERROR_RECURSION
MOV _IN_FUNC__FACT_RECURSION, 1
MOV EAX, 1
MOV EBX, EAX
MOV EAX, _N_RECURSION_FACT
SUB EAX, EBX
MOV EAX, EAX
MOV _N_RECURSION_FACT, EAX
CALL _FACT_RECURSION
MOV _IN_FUNC__FACT_RECURSION, 0
MOV EAX, _RET_0__FACT_RECURSION
MOV _aux_long, EAX
MOV EAX, _aux_long
MOV EBX, EAX
MOV EAX, _N_RECURSION_FACT
IMUL EBX
JO _ERROR_OVERFLOW_PROD
MOV EAX, EAX
MOV _AUX_RECURSION_FACT, EAX
MOV EAX, _AUX_RECURSION_FACT
MOV _RET_0__FACT_RECURSION, EAX
JMP _FACT_RECURSION_exit
_label1:
_FACT_RECURSION_exit:
pop esi
pop edi
mov esp, ebp
pop ebp
RET
_FACT_RECURSION ENDP
; --- Fin de Funcion: FACT ---

START:
FINIT
invoke printf, ADDR _format_string, ADDR ____Test_de_Recursividad__Factorial_____
invoke printf, ADDR _format_string, ADDR _Calculando_factorial_de_5___
; Chequeo de recursion (h)
CMP _IN_FUNC__FACT_RECURSION, 1
JE _ERROR_RECURSION
MOV _IN_FUNC__FACT_RECURSION, 1
MOV EAX, 5
MOV _N_RECURSION_FACT, EAX
CALL _FACT_RECURSION
MOV _IN_FUNC__FACT_RECURSION, 0
MOV EAX, _RET_0__FACT_RECURSION
MOV _aux_long, EAX
MOV EAX, _aux_long
MOV _RESULTADO_RECURSION, EAX
invoke printf, ADDR _format_string, ADDR _El_resultado_deberia_ser_120__Obtenido_
invoke printf, ADDR _format_long, _RESULTADO_RECURSION

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
