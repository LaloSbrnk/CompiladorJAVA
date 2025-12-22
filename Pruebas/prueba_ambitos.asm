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
_IN_FUNC__FUNC_TEST_SCOPE_PROGRAMA DB 0
_RET_0__FUNC_TEST_SCOPE_PROGRAMA DD ?
_RET_1__FUNC_TEST_SCOPE_PROGRAMA DQ ?
_0_0 DQ 0.0
_R2_PROGRAMA DQ ?
_R1_PROGRAMA DD ?
_VAR_LOCAL_PROGRAMA_FUNC_TEST_SCOPE DD ?
_Variable_local_dentro_ DB "Variable local dentro:", 0
_VAR_GLOBAL_PROGRAMA DD ?
_Intentando_acceder_a_variable_local_desde_fuera___ DB "Intentando acceder a variable local desde fuera...", 0
; --- Fin Variables y Constantes ---


.CODE

; --- Definicion de Funcion: FUNC%TEST%SCOPE ---
_FUNC_TEST_SCOPE_PROGRAMA PROC
push ebp
mov ebp, esp
push edi
push esi
MOV EAX, 99
MOV _VAR_LOCAL_PROGRAMA_FUNC_TEST_SCOPE, EAX
invoke printf, ADDR _format_string, ADDR _Variable_local_dentro_
invoke printf, ADDR _format_long, _VAR_LOCAL_PROGRAMA_FUNC_TEST_SCOPE
MOV EAX, _VAR_LOCAL_PROGRAMA_FUNC_TEST_SCOPE
MOV _RET_0__FUNC_TEST_SCOPE_PROGRAMA, EAX
FLD _0_0
FSTP _RET_1__FUNC_TEST_SCOPE_PROGRAMA
JMP _FUNC_TEST_SCOPE_PROGRAMA_exit
_FUNC_TEST_SCOPE_PROGRAMA_exit:
pop esi
pop edi
mov esp, ebp
pop ebp
RET
_FUNC_TEST_SCOPE_PROGRAMA ENDP
; --- Fin de Funcion: FUNC%TEST%SCOPE ---

START:
FINIT
; Chequeo de recursion (h)
CMP _IN_FUNC__FUNC_TEST_SCOPE_PROGRAMA, 1
JE _ERROR_RECURSION
MOV _IN_FUNC__FUNC_TEST_SCOPE_PROGRAMA, 1
CALL _FUNC_TEST_SCOPE_PROGRAMA
MOV _IN_FUNC__FUNC_TEST_SCOPE_PROGRAMA, 0
MOV EAX, _RET_0__FUNC_TEST_SCOPE_PROGRAMA
MOV _aux_long, EAX
MOV EAX, _RET_0__FUNC_TEST_SCOPE_PROGRAMA
MOV _R1_PROGRAMA, EAX
FLD _RET_1__FUNC_TEST_SCOPE_PROGRAMA
FSTP _R2_PROGRAMA
invoke printf, ADDR _format_string, ADDR _Intentando_acceder_a_variable_local_desde_fuera___

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
