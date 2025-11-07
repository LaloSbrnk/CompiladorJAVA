package ArbolSintactico;
import java.util.HashMap;
import CompiladoresMain.TablaDeAmbitos;
import CompiladoresMain.GeneradorAssembler; // <--- NUEVO

public class NodoPrint extends Nodo {
    private Nodo expresion;
    
    public NodoPrint(Nodo expresion) {
        this.expresion = expresion;
    }
    
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        // ... (código de chequeo existente sin cambios) ...
        String tipoExpr = expresion.chequear(TdA);
        if (tipoExpr.equals("error")) {
            return "error";
        }
        return "void"; 
    }
    
    @Override
    public void imprimir(String prefijo) {
        // ... (código de imprimir existente sin cambios) ...
        System.out.println(prefijo + "Sentencia PRINT");
        expresion.imprimir(prefijo + "  " + "Argumento: ");
    }

    // --- NUEVO METODO ---
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        
        String resExpr = expresion.generarCodigo(G, TdA);
        String tipoExpr = expresion.chequear(TdA);

        if ("error".equals(tipoExpr)) return null;

        switch (tipoExpr) {
            case "long":
                // [cite: 2362]
                G.agregarCodigo("invoke printf, ADDR _format_long, " + resExpr);
                break;
            case "dfloat":
                // [cite: 2360]
                G.agregarCodigo("invoke printf, ADDR _format_dfloat, " + resExpr);
                break;
            case "string":
                // [cite: 2358]
                G.agregarCodigo("invoke printf, ADDR _format_string, ADDR " + resExpr);
                break;
        }
        
        return null;
    }
}