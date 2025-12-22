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
_Fin_del_test DB "Fin del test", 0
_I_vale_1__Dentro_del_DO_ DB "I vale 1 (Dentro del DO)", 0
_I_PROGRAMA DD ?
____Inicio_Anidamiento____ DB "--- Inicio Anidamiento ---", 0
_Iterando___ DB "Iterando...", 0
; --- Fin Variables y Constantes ---


.CODE
START:
FINIT
MOV EAX, 0
MOV _I_PROGRAMA, EAX
invoke printf, ADDR _format_string, ADDR ____Inicio_Anidamiento____
_label0:
MOV EAX, _I_PROGRAMA
CMP EAX, 1
JNE _label1
invoke printf, ADDR _format_string, ADDR _I_vale_1__Dentro_del_DO_
JMP _label2
_label1:
invoke printf, ADDR _format_string, ADDR _Iterando___
_label2:
MOV EAX, _I_PROGRAMA
ADD EAX, 1
MOV _aux_long, EAX
MOV EAX, _aux_long
MOV _I_PROGRAMA, EAX
MOV EAX, _I_PROGRAMA
CMP EAX, 3
JL _label0
invoke printf, ADDR _format_string, ADDR _Fin_del_test

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
