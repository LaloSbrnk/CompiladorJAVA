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
_ERROR__Si_ves_este_mensaje__el_control_de_errores_fallo_ DB "ERROR: Si ves este mensaje, el control de errores fallo.", 0
_RES_ERROR DD ?
____Test_de_Error_en_Tiempo_de_Ejecucion____ DB "--- Test de Error en Tiempo de Ejecucion ---", 0
_Intentando_dividir_por_cero___ DB "Intentando dividir por cero...", 0
_DENOM_ERROR DD ?
_NUM_ERROR DD ?
; --- Fin Variables y Constantes ---


.CODE
START:
FINIT
invoke printf, ADDR _format_string, ADDR ____Test_de_Error_en_Tiempo_de_Ejecucion____
MOV EAX, 100
MOV _NUM_ERROR, EAX
MOV EAX, 0
MOV _DENOM_ERROR, EAX
invoke printf, ADDR _format_string, ADDR _Intentando_dividir_por_cero___
MOV EAX, _DENOM_ERROR
MOV EBX, EAX
MOV EAX, _NUM_ERROR
CMP EBX, 0
JE _ERROR_DIV_CERO
CDQ
IDIV EBX
MOV EAX, EAX
MOV _RES_ERROR, EAX
invoke printf, ADDR _format_string, ADDR _ERROR__Si_ves_este_mensaje__el_control_de_errores_fallo_

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
