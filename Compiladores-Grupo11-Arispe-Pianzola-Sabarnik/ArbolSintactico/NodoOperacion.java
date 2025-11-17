package ArbolSintactico;
import java.util.HashMap;
import CompiladoresMain.TablaDeAmbitos;
import CompiladoresMain.GeneradorAssembler; // <--- NUEVO
import CompiladoresMain.TiposToken; // <--- NUEVO
import CompiladoresMain.AtributosTokens; // <--- NUEVO
import CompiladoresMain.AnalizadorLexico; // <--- NUEVO

public class NodoOperacion extends Nodo {
    private String op;
    private Nodo izq;
    private Nodo der;
    private String tipoRes = null; // Cachear tipo
    
    public NodoOperacion(String op, Nodo izq, Nodo der) {
        this.op = op;
        this.izq = izq;
        this.der = der;
    }
    
    @Override
    public String chequear(TablaDeAmbitos TdA) { 
        if (this.tipoRes != null) return this.tipoRes;

        String tipoIzq = izq.chequear(TdA);
        String tipoDer = (der != null) ? der.chequear(TdA) : null; 
        
        if (this.op.equals("UMINUS")) {
            if (tipoIzq.equals("long") || tipoIzq.equals("dfloat")) {
                this.tipoRes = tipoIzq;
                return tipoIzq;
            } else {
                System.err.println("ERROR Semantico: Operador 'menos unario' no aplicable a tipo '" + tipoIzq + "'.");
                this.tipoRes = "error";
                return "error";
            }
        }
        
        if (tipoIzq.equals("error") || tipoDer.equals("error")) {
            this.tipoRes = "error";
            return "error";
        }
        
        if ((tipoIzq.equals("long") || tipoIzq.equals("dfloat")) &&
            (tipoDer.equals("long") || tipoDer.equals("dfloat"))) {
            
            if (tipoIzq.equals("dfloat") || tipoDer.equals("dfloat")) {
                this.tipoRes = "dfloat"; // Promoción (Tema 30)
                return "dfloat";
            } else {
                this.tipoRes = "long";
                return "long";
            }
        }
        
        System.err.println("ERROR Semantico: Tipos incompatibles en la operacion '" + op + "': " + tipoIzq + ", " + tipoDer);
        this.tipoRes = "error";
        return "error";
    }
    
    @Override
    public void imprimir(String prefijo) {
        // ... (código de imprimir existente sin cambios) ...
        System.out.println(prefijo + "Operacion: " + op);
        izq.imprimir(prefijo + "  " + "Izq: ");
        if (der != null) {
            der.imprimir(prefijo + "  " + "Der: ");
        }
    }

    // --- NUEVO METODO ---
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        String tipoResultado = this.chequear(TdA);
        if ("error".equals(tipoResultado)) return null;

        String resIzq = izq.generarCodigo(G, TdA);
        String tipoIzq = izq.chequear(TdA);

        // Caso UMINUS (unitario)
        if (this.op.equals("UMINUS")) {
            String aux = G.getNombreAsm(GeneradorAssembler.AUX_LONG);
            if (tipoResultado.equals("long")) {
                G.agregarCodigo("MOV EAX, 0");
                G.agregarCodigo("SUB EAX, " + resIzq);
                G.agregarCodigo("MOV " + aux + ", EAX");
            } else {
                aux = G.getNombreAsm(GeneradorAssembler.AUX_DFLOAT);
                G.agregarCodigo("FLD " + resIzq);
                G.agregarCodigo("FCHS"); // Float Change Sign
                G.agregarCodigo("FSTP " + aux);
            }
            return aux;
        }

        // Casos binarios
        String resDer = der.generarCodigo(G, TdA);
        String tipoDer = der.chequear(TdA);

        if (tipoResultado.equals("long")) {
            // --- Aritmética de Enteros (long) --- [cite: 1756]
            String aux = G.getNombreAsm(GeneradorAssembler.AUX_LONG);
            G.agregarCodigo("MOV EAX, " + resIzq);

            switch (this.op) {
                case "+":
                    G.agregarCodigo("ADD EAX, " + resDer); // [cite: 2067]
                    break;
                case "-":
                    G.agregarCodigo("SUB EAX, " + resDer); // [cite: 2075]
                    break;
                case "*":
                    G.agregarCodigo("IMUL EAX, " + resDer); // [cite: 2104, 2113]
                    // (d) Chequeo Overflow producto entero [cite: 1774]
                    G.agregarCodigo("JO _ERROR_OVERFLOW_PROD"); // [cite: 2237]
                    break;
                case "/":
                    // (a) Chequeo División por Cero (Entero) [cite: 1763]
                    G.agregarCodigo("CMP " + resDer + ", 0"); // [cite: 2083]
                    G.agregarCodigo("JE _ERROR_DIV_CERO"); // [cite: 2224]
                    G.agregarCodigo("CDQ"); // Extender signo EAX a EDX:EAX [cite: 2151]
                    G.agregarCodigo("IDIV " + resDer); // [cite: 2133]
                    break;
            }
            G.agregarCodigo("MOV " + aux + ", EAX");
            return aux;

        } else {
            // --- Aritmética de Flotantes (dfloat) --- [cite: 1757]
            String aux = G.getNombreAsm(GeneradorAssembler.AUX_DFLOAT);

            // Cargar Operando Izquierdo (con conversión si es long)
            if (tipoIzq.equals("dfloat")) {
                G.agregarCodigo("FLD " + resIzq);
            } else {
                G.convertirLongADFloat(resIzq); // (Tema 30) [cite: 1803]
            }

            // Cargar Operando Derecho (con conversión si es long)
            if (tipoDer.equals("dfloat")) {
                G.agregarCodigo("FLD " + resDer);
            } else {
                G.convertirLongADFloat(resDer); // (Tema 30)
            }
            
            // Pila: [opDer, opIzq]

            switch (this.op) {
                case "+":
                    G.agregarCodigo("FADD"); // ST(0) = ST(0) + ST(1) -> opDer + opIzq
                    break;
                case "-":
                    // Queremos Izq - Der
                    // Pila: [opDer, opIzq] -> FSUBR (Reverse)
                    G.agregarCodigo("FSUBR"); // ST(0) = ST(1) - ST(0) -> opIzq - opDer
                    break;
                case "*":
                    G.agregarCodigo("FMUL"); // ST(0) = ST(0) * ST(1)
                    break;
                case "/":
                    // Queremos Izq / Der
                    // Pila: [opDer, opIzq]
                    
                    // (a) Chequeo División por Cero (Flotante) [cite: 1763]
                    // El divisor es opDer (ST(0))
                    G.agregarCodigo("FTST"); // Compara ST(0) con 0
                    G.agregarCodigo("FSTSW AX"); // Almacena flags del FPU en AX
                    G.agregarCodigo("SAHF"); // Mueve AH a los flags del CPU [cite: 2043]
                    G.agregarCodigo("JE _ERROR_DIV_CERO_FLOAT"); // Salta si ZF=1 (es cero) [cite: 2224]

                    // Realizar la división (Reverse)
                    G.agregarCodigo("FDIVR"); // ST(0) = ST(1) / ST(0) -> opIzq / opDer
                    break;
            }
            
            // En FADD, FSUBR, FMUL, FDIVR, el resultado queda en ST(0) y ST(1) se popea.
            G.agregarCodigo("FSTP " + aux); // Guardar resultado y popear [cite: 2906]
            return aux;
        }
    }
}