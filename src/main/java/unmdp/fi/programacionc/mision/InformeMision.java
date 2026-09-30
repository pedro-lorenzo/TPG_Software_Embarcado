package unmdp.fi.programacionc.mision;

public final InformeMision ejecutarCiclo(Nave nave) {
    preparar(nave);
    ejecutar(nave);
    evaluar(nave);
    return cerrar(nave);          // antes: no devolvía nada
}

public InformeMision cerrar(Nave nave) {     // antes: void
    // TODO: registrar en bitacora y armar el informe real
    return new InformeMision();
}