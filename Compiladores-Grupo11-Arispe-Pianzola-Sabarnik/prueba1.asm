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
_A_es_mayor_a_5 DB "A es mayor a 5", 0
_A_es_menor_a_5 DB "A es menor a 5", 0
_A_PROGRAMA DD ?
_A_NO_es_menor_a_5 DB "A NO es menor a 5", 0
_A_NO_es_mayor_a_5 DB "A NO es mayor a 5", 0
; --- Fin Variables y Constantes ---


.CODE
START:
FINIT
MOV EAX, 10
MOV _A_PROGRAMA, EAX
MOV EAX, _A_PROGRAMA
CMP EAX, 5
JLE _label0
invoke printf, ADDR _format_string, ADDR _A_es_mayor_a_5
JMP _label1
_label0:
invoke printf, ADDR _format_string, ADDR _A_NO_es_mayor_a_5
_label1:
MOV EAX, _A_PROGRAMA
CMP EAX, 5
JGE _label2
invoke printf, ADDR _format_string, ADDR _A_es_menor_a_5
JMP _label3
_label2:
invoke printf, ADDR _format_string, ADDR _A_NO_es_menor_a_5
_label3:

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
