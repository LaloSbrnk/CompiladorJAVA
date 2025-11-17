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
_CERO_L_PROGRAMA DD ?
_P_PROGRAMA_FUNC_RECURSIVA DD ?
_Esta_linea_no_deberia_imprimirse_ DB "Esta linea no deberia imprimirse.", 0
_DIEZ_D_PROGRAMA DQ ?
_GRANDE_PROGRAMA DD ?
_IN_FUNC__FUNC_RECURSIVA_PROGRAMA DB 0
_RET_0__FUNC_RECURSIVA_PROGRAMA DD ?
_RET_1__FUNC_RECURSIVA_PROGRAMA DD ?
_CERO_D_PROGRAMA DQ ?
_0_0 DQ 0.0
_Llamada_recursiva___ DB "Llamada recursiva...", 0
_DIEZ_L_PROGRAMA DD ?
_10_0 DQ 10.0
; --- Fin Variables y Constantes ---


.CODE

; --- Definicion de Funcion: FUNC%RECURSIVA ---
_FUNC_RECURSIVA_PROGRAMA PROC
push ebp
mov ebp, esp
push edi
push esi
invoke printf, ADDR _format_string, ADDR _Llamada_recursiva___
; Chequeo de recursion (h)
CMP _IN_FUNC__FUNC_RECURSIVA_PROGRAMA, 1
JE _ERROR_RECURSION
MOV _IN_FUNC__FUNC_RECURSIVA_PROGRAMA, 1
MOV EAX, _P_PROGRAMA_FUNC_RECURSIVA
MOV _P_PROGRAMA_FUNC_RECURSIVA, EAX
CALL _FUNC_RECURSIVA_PROGRAMA
MOV _IN_FUNC__FUNC_RECURSIVA_PROGRAMA, 0
MOV EAX, _RET_0__FUNC_RECURSIVA_PROGRAMA
MOV _aux_long, EAX
MOV EAX, _aux_long
MOV _RET_0__FUNC_RECURSIVA_PROGRAMA, EAX
JMP _FUNC_RECURSIVA_PROGRAMA_exit
_FUNC_RECURSIVA_PROGRAMA_exit:
pop esi
pop edi
mov esp, ebp
pop ebp
RET
_FUNC_RECURSIVA_PROGRAMA ENDP
; --- Fin de Funcion: FUNC%RECURSIVA ---

START:
FINIT
MOV EAX, 0
MOV _CERO_L_PROGRAMA, EAX
MOV EAX, 10
MOV _DIEZ_L_PROGRAMA, EAX
FLD _0_0
FSTP _CERO_D_PROGRAMA
FLD _10_0
FSTP _DIEZ_D_PROGRAMA
MOV EAX, 2147483647
MOV _GRANDE_PROGRAMA, EAX
; Chequeo de recursion (h)
CMP _IN_FUNC__FUNC_RECURSIVA_PROGRAMA, 1
JE _ERROR_RECURSION
MOV _IN_FUNC__FUNC_RECURSIVA_PROGRAMA, 1
MOV EAX, 1
MOV _P_PROGRAMA_FUNC_RECURSIVA, EAX
CALL _FUNC_RECURSIVA_PROGRAMA
MOV _IN_FUNC__FUNC_RECURSIVA_PROGRAMA, 0
MOV EAX, _RET_0__FUNC_RECURSIVA_PROGRAMA
MOV _aux_long, EAX
MOV EAX, _DIEZ_L_PROGRAMA
CMP _CERO_L_PROGRAMA, 0
JE _ERROR_DIV_CERO
CDQ
IDIV _CERO_L_PROGRAMA
MOV _aux_long, EAX
invoke printf, ADDR _format_long, _aux_long
FLD _DIEZ_D_PROGRAMA
FLD _CERO_D_PROGRAMA
FTST
FSTSW AX
SAHF
JE _ERROR_DIV_CERO_FLOAT
FDIVR
FSTP _aux_dfloat
invoke printf, ADDR _format_dfloat, _aux_dfloat
MOV EAX, _GRANDE_PROGRAMA
IMUL EAX, 2
JO _ERROR_OVERFLOW_PROD
MOV _aux_long, EAX
invoke printf, ADDR _format_long, _aux_long
invoke printf, ADDR _format_string, ADDR _Esta_linea_no_deberia_imprimirse_

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
