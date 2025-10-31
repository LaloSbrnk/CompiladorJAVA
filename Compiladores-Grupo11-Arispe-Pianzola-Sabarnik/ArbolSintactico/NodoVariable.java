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
        
        if (this.nombre.contains(".")) {
            // Es un nombre prefijado (ej. "FUNCION1.VAR_X")
            String[] partes = this.nombre.split("\\.", 2); // Divide en maximo 2 partes por el punto
            String nombreModulo = partes[0]; 
            String nombreVarLocal = partes[1]; 

            // Buscamos el Modulo/Funcion en la TdA actual
            AtributosTokens attrsModulo = TdA.buscar(nombreModulo);

            // Chequeamos si existe, es funcion y tiene ambito local
            if (attrsModulo == null || !attrsModulo.getUso().equals("funcion") || attrsModulo.getAmbitoLocal() == null) {
                System.err.println("ERROR Semantico: El prefijo '" + nombreModulo + "' no corresponde a una funcion declarada o accesible.");
                return "error";
            }

            // Buscamos la variable DENTRO del ambito local de la funcion
            HashMap<String, AtributosTokens> ambitoModulo = attrsModulo.getAmbitoLocal();
            AtributosTokens attrsVar = ambitoModulo.get(nombreVarLocal);

            // Chequeamos si la variable existe dentro de la funcion
            if (attrsVar == null || attrsVar.getUso() == null) {
                System.err.println("ERROR Semantico: La funcion '" + nombreModulo + "' no contiene una variable o parametro llamado '" + nombreVarLocal + "'.");
                return "error";
            }
            
            // Devolvemos el tipo de la variable prefijada
             System.out.println("DEBUG: Acceso prefijado '" + this.nombre + "' OK. Tipo: " + attrsVar.getTipoDato());
            return attrsVar.getTipoDato();

        } else {
            // --- (Variable no prefijada) ---
            AtributosTokens attrs = TdA.buscar(this.nombre);

            if (attrs == null || attrs.getUso() == null) {
                System.err.println("ERROR Semantico: Variable '" + this.nombre + "' no declarada en este ambito.");
                return "error";
            }
            
            // Devolvemos el tipo de la variable local/global
            return attrs.getTipoDato();
        }
    }

    public String getNombre(){
        return this.nombre;
    }
}