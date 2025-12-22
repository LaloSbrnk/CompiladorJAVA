package ArbolSintactico;
import CompiladoresMain.*;
import java.util.HashMap;

public class NodoLambdaInline extends Nodo {
    private String nombreParam;
    private String tipoParam;
    private NodoBloque cuerpo;
    private NodoArgumento argumento;
    private String mangledParamName = null; 
    
    public NodoLambdaInline(String tipoParam, String nombreParam, NodoBloque cuerpo, NodoArgumento argumento) {
        this.tipoParam = tipoParam;
        this.nombreParam = nombreParam;
        this.cuerpo = cuerpo;
        this.argumento = argumento;
    }
    
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        
        String tipoArgumentoReal = argumento.chequear(TdA);
        if (tipoArgumentoReal.equals("error")) {
            return "error"; 
        }
        if (tipoParam.equals("long") && tipoArgumentoReal.equals("dfloat")) {
             System.err.println("ERROR Semantico: Lambda esperaba argumento tipo '" + tipoParam +
                                "' pero recibio '" + tipoArgumentoReal + "' (posible perdida de datos).");
             return "error";
        }
        if (!tipoParam.equals(tipoArgumentoReal) && !(tipoParam.equals("dfloat") && tipoArgumentoReal.equals("long"))) {
             System.err.println("ERROR Semantico: Lambda esperaba argumento tipo '" + tipoParam +
                                "' pero recibio '" + tipoArgumentoReal + "'.");
             return "error";
        }
        
        TdA.abrirAmbito("LAMBDA"); 
        
        AtributosTokens attrsParam = new AtributosTokens(TiposToken.IDENTIFICADOR);
        attrsParam.setTipoDato(this.tipoParam);
        attrsParam.setUso("parametro_lambda");
        if (!TdA.agregar(this.nombreParam, attrsParam)) {
            System.err.println("ERROR Interno: No se pudo agregar el parametro lambda '" + this.nombreParam + "'.");
            TdA.cerrarAmbito(); 
            return "error";
        } else {
             this.mangledParamName = attrsParam.getMangledName(); 
        }
        
        cuerpo.chequear(TdA);
        TdA.cerrarAmbito();
        return "void"; 
    }
    
    @Override
    public void imprimir(String prefijo) {
        
    }

    
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        
        
        
        if (this.mangledParamName == null) {
            return null;
        }
        

        
        String resArg = argumento.generarCodigo(G, TdA);
        String tipoArg = argumento.chequear(TdA);
        
        
        String paramAsmName = G.getNombreAsm(this.mangledParamName);
        
        
        if (this.tipoParam.equals("long")) {
            G.agregarCodigo("MOV EAX, " + resArg);
            G.agregarCodigo("MOV " + paramAsmName + ", EAX");
        } else {
            if (tipoArg.equals("long")) {
                G.convertirLongADFloat(resArg);
            } else {
                G.agregarCodigo("FLD " + resArg);
            }
            G.agregarCodigo("FSTP " + paramAsmName);
        }

        
        TdA.abrirAmbito("LAMBDA");
        cuerpo.generarCodigo(G, TdA);
        TdA.cerrarAmbito();
        
        return null;
    }
}
