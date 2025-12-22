package ArbolSintactico;
import java.util.HashMap;
import CompiladoresMain.TablaDeAmbitos;
import CompiladoresMain.GeneradorAssembler; 

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
        
        System.out.println(prefijo + "Comparacion: " + op);
        izq.imprimir(prefijo + "  " + "Izq: ");
        der.imprimir(prefijo + "  " + "Der: ");
    }

    
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        String resIzq = izq.generarCodigo(G, TdA);
        String resDer = der.generarCodigo(G, TdA);
        
        String tipoIzq = izq.chequear(TdA);
        String tipoDer = der.chequear(TdA);
        String tipoComp = (tipoIzq.equals("dfloat") || tipoDer.equals("dfloat")) ? "dfloat" : "long";

        if (tipoComp.equals("long")) {
            
            
            
            G.agregarCodigo("MOV EAX, " + resIzq);
            G.agregarCodigo("CMP EAX, " + resDer); 

            
            
            switch (this.op) {
                case ">":  return "JLE"; 
                case "<":  return "JGE"; 
                case ">=": return "JL";  
                case "<=": return "JG";  
                case "==": return "JNE"; 
                case "!=": return "JE";  
            }

        } else {
            
            
            
            
            
            if (tipoIzq.equals("dfloat")) G.agregarCodigo("FLD " + resIzq);
            else G.convertirLongADFloat(resIzq); 
            
            
            if (tipoDer.equals("dfloat")) G.agregarCodigo("FLD " + resDer);
            else G.convertirLongADFloat(resDer); 

            
            
            
            G.agregarCodigo("FCOMIP ST(0), ST(1)"); 
            G.agregarCodigo("FFREE ST(0)"); 

            
            
            
            switch (this.op) {
                case ">":  return "JNA"; 
                case "<":  return "JNB"; 
                case ">=": return "JB";  
                case "<=": return "JA";  
                case "==": return "JNE"; 
                case "!=": return "JE";  
            }
        }

        return "JMP"; 
    }
}
