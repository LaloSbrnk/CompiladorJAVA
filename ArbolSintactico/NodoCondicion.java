package ArbolSintactico;
import java.util.HashMap;
import CompiladoresMain.TablaDeAmbitos;
import CompiladoresMain.GeneradorAssembler; // <--- NUEVO

public class NodoCondicion extends Nodo {
    private Nodo izq;
    private Nodo der;
    private String op; 
    
    public NodoCondicion(Nodo izq, Nodo der, String op) {
        this.izq = izq;
        this.der = der;
        this.op = op;
    }
    
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        // ... (código de chequeo existente sin cambios) ...
        String tipoIzq = izq.chequear(TdA);
        String tipoDer = der.chequear(TdA);
        if (tipoIzq.equals("error") || tipoDer.equals("error")) {
            return "error";
        }
        if (! (tipoIzq.equals("long") || tipoIzq.equals("dfloat")) ) {
            System.err.println("ERROR Semantico: Tipo incompatible en la comparacion. No se puede comparar un '" + tipoIzq + "'.");
            return "error";
        }
        if (! (tipoDer.equals("long") || tipoDer.equals("dfloat")) ) {
            System.err.println("ERROR Semantico: Tipo incompatible en la comparacion. No se puede comparar un '" + tipoDer + "'.");
            return "error";
        }
        return "boolean"; 
    }
    
    @Override
    public void imprimir(String prefijo) {
        // ... (código de imprimir existente sin cambios) ...
        System.out.println(prefijo + "Comparacion: " + op);
        izq.imprimir(prefijo + "  " + "Izq: ");
        der.imprimir(prefijo + "  " + "Der: ");
    }

    // --- NUEVO METODO ---
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        String resIzq = izq.generarCodigo(G, TdA);
        String resDer = der.generarCodigo(G, TdA);
        
        String tipoIzq = izq.chequear(TdA);
        String tipoDer = der.chequear(TdA);
        String tipoComp = (tipoIzq.equals("dfloat") || tipoDer.equals("dfloat")) ? "dfloat" : "long";

        if (tipoComp.equals("long")) {
            // --- Comparación de Enteros ---
            G.agregarCodigo("MOV EAX, " + resIzq);
            G.agregarCodigo("CMP EAX, " + resDer); // [cite: 2083]
        } else {
            // --- Comparación de Flotantes ---
            
            // Cargar Izquierdo
            if (tipoIzq.equals("dfloat")) G.agregarCodigo("FLD " + resIzq);
            else G.convertirLongADFloat(resIzq); // Tema 30
            
            // Cargar Derecho
            if (tipoDer.equals("dfloat")) G.agregarCodigo("FLD " + resDer);
            else G.convertirLongADFloat(resDer); // Tema 30

            // Pila: [opDer, opIzq]
            // Comparamos ST(0) con ST(1) y popeamos ST(0)
            G.agregarCodigo("FCOMIP ST(0), ST(1)"); 
            G.agregarCodigo("FFREE ST(0)"); // Popeamos ST(1) (que ahora es ST(0))
        }

        // Devolvemos la instrucción de SALTO OPUESTA
        // (Saltamos si la condición es FALSA)
        switch (this.op) {
            case ">":  return "JLE"; // Jump if Less or Equal [cite: 40, 2174]
            case "<":  return "JGE"; // Jump if Greater or Equal [cite: 2189]
            case ">=": return "JL";  // Jump if Less [cite: 2173]
            case "<=": return "JG";  // Jump if Greater [cite: 2190]
            case "==": return "JNE"; // Jump if Not Equal [cite: 2183]
            case "!=": return "JE";  // Jump if Equal [cite: 2181]
        }
        return "JMP"; // Fallback
    }
}