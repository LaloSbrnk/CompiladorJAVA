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
_0_0 DQ 0.0
_R2_PROGRAMA DQ ?
_R1_PROGRAMA DD ?
_Iniciando_prueba_de_argumentos_incorrectos___ DB "Iniciando prueba de argumentos incorrectos...", 0
_P_LONG_PROGRAMA_FUNC_TEST_ARGS DD ?
_1_5 DQ 1.5
_IN_FUNC__FUNC_TEST_ARGS_PROGRAMA DB 0
_RET_0__FUNC_TEST_ARGS_PROGRAMA DD ?
_RET_1__FUNC_TEST_ARGS_PROGRAMA DQ ?
; --- Fin Variables y Constantes ---


.CODE

; --- Definicion de Funcion: FUNC%TEST%ARGS ---
_FUNC_TEST_ARGS_PROGRAMA PROC
push ebp
mov ebp, esp
push edi
push esi
invoke printf, ADDR _format_long, _P_LONG_PROGRAMA_FUNC_TEST_ARGS
MOV EAX, _P_LONG_PROGRAMA_FUNC_TEST_ARGS
MOV _RET_0__FUNC_TEST_ARGS_PROGRAMA, EAX
FLD _0_0
FSTP _RET_1__FUNC_TEST_ARGS_PROGRAMA
JMP _FUNC_TEST_ARGS_PROGRAMA_exit
_FUNC_TEST_ARGS_PROGRAMA_exit:
pop esi
pop edi
mov esp, ebp
pop ebp
RET
_FUNC_TEST_ARGS_PROGRAMA ENDP
; --- Fin de Funcion: FUNC%TEST%ARGS ---

START:
FINIT
invoke printf, ADDR _format_string, ADDR _Iniciando_prueba_de_argumentos_incorrectos___
MOV EAX, _RET_0__FUNC_TEST_ARGS_PROGRAMA
MOV _R1_PROGRAMA, EAX
FLD _RET_1__FUNC_TEST_ARGS_PROGRAMA
FSTP _R2_PROGRAMA

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
