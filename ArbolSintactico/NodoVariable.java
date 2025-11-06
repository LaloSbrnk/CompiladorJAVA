package ArbolSintactico;
import CompiladoresMain.*; 
import java.util.HashMap;
public class NodoVariable extends Nodo {
    private String nombre;
    public NodoVariable(String nombre) {
        this.nombre = nombre;
    }
@Override
    public String chequear(TablaDeAmbitos TdA) {
        AtributosTokens attrs = null;
        if (this.nombre.contains(".")) {
            String[] partes = this.nombre.split("\\.", 2); 
            String nombreModulo = partes[0]; 
            String nombreVarLocal = partes[1];
            String rootScope = TdA.getRootScopeName();
            if (rootScope != null && nombreModulo.equals(rootScope)) {
                String mangledName = nombreVarLocal + ":" + rootScope;
                attrs = AnalizadorLexico.tablaSimbolos.get(mangledName);
                if (attrs == null || attrs.getUso() == null) {
                     System.err.println("ERROR Semantico: Variable '" + nombreVarLocal + "' no declarada en el ambito global '" + rootScope + "'.");
                    return "error";
                }
            } else {
                attrs = TdA.buscarPrefijado(this.nombre);
                if (attrs == null) {
                    return "error";
                }
            }
        } else {
            attrs = TdA.buscar(this.nombre);
            if (attrs == null || attrs.getUso() == null) {
                System.err.println("ERROR Semantico: Variable '" + this.nombre + "' no declarada en este ambito o ambitos superiores.");
                return "error";
            }
        }
        return attrs.getTipoDato();
    }
    public String getNombre(){
        return this.nombre;
    }
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Variable: " + nombre);
    }
}