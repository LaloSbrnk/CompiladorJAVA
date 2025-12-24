package ArbolSintactico;
import java.util.HashMap;
import CompiladoresMain.TablaDeAmbitos;
import CompiladoresMain.GeneradorAssembler; 
import CompiladoresMain.TiposToken; 
import CompiladoresMain.AtributosTokens; 
import CompiladoresMain.AnalizadorLexico; 

public class NodoOperacion extends Nodo {
    private String op;
    private Nodo izq;
    private Nodo der;
    private String tipoRes = null; 
    
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
                this.tipoRes = "dfloat"; 
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
        
        System.out.println(prefijo + "Operacion: " + op);
        izq.imprimir(prefijo + "  " + "Izq: ");
        if (der != null) {
            der.imprimir(prefijo + "  " + "Der: ");
        }
    }

    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        
        String tipoResultado = this.chequear(TdA);
        
        if ("error".equals(tipoResultado)) return null;

        
        String resIzq = izq.generarCodigo(G, TdA);
        String tipoIzq = izq.chequear(TdA);

        
        if (this.op.equals("UMINUS")) {
            String aux = G.getNombreAsm(GeneradorAssembler.AUX_LONG);
            if (tipoResultado.equals("long")) {
                G.agregarCodigo("MOV EAX, 0");
                G.agregarCodigo("SUB EAX, " + resIzq);
                G.agregarCodigo("MOV " + aux + ", EAX");
            } else {
                aux = G.getNombreAsm(GeneradorAssembler.AUX_DFLOAT);
                G.agregarCodigo("FLD " + resIzq);
                G.agregarCodigo("FCHS"); 
                G.agregarCodigo("FSTP " + aux);
            }
            return aux;
        }

        
        String resDer = der.generarCodigo(G, TdA);
        String tipoDer = der.chequear(TdA);

        
        
        
        if (tipoResultado.equals("long")) {
            
            
            
            G.agregarCodigo("MOV EAX, " + resDer); 
            G.agregarCodigo("MOV EBX, EAX"); 
            
            
            G.agregarCodigo("MOV EAX, " + resIzq);
            
            

            switch (op) {
                case "+":
                    G.agregarCodigo("ADD EAX, EBX");
                    break;
                case "-":
                    G.agregarCodigo("SUB EAX, EBX");
                    break;
                    
                case "*":
                    G.agregarCodigo("IMUL EBX");
                    
                    G.agregarCodigo("JO _ERROR_OVERFLOW_PROD"); 
                    
                    break;
                    
                case "/":
                    
                    G.agregarCodigo("CMP EBX, 0");        
                    G.agregarCodigo("JE _ERROR_DIV_CERO"); 
                    
                    
                    G.agregarCodigo("CDQ"); 
                    G.agregarCodigo("IDIV EBX"); 
                    break;
            }
            
            
            return "EAX";
        }
        
        
        
        
        else { 
            String aux = G.getNombreAsm(GeneradorAssembler.AUX_DFLOAT);

            
            if (tipoIzq.equals("long")) {
                G.convertirLongADFloat(resIzq); 
            } else {
                
                G.agregarCodigo("FLD " + resIzq);
            }

            
            if (tipoDer.equals("long")) {
                G.convertirLongADFloat(resDer); 
            } else {
                G.agregarCodigo("FLD " + resDer);
            }
            
            

            switch (this.op) {
                case "+": G.agregarCodigo("FADD"); break;
                case "-": G.agregarCodigo("FSUB"); break;
                case "*": G.agregarCodigo("FMUL"); break;
                case "/":
                    
                    
                    G.agregarCodigo("FTST");      
                    G.agregarCodigo("FSTSW AX");
                    G.agregarCodigo("SAHF");
                    G.agregarCodigo("JE _ERROR_DIV_CERO_FLOAT");
                    
                    
                    G.agregarCodigo("FDIV"); 
                    break;
            }
            
            
            G.agregarCodigo("FSTP " + aux); 
            return aux;
        }
    }
}
