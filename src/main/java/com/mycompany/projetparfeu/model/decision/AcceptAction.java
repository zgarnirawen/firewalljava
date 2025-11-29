package com.mycompany.projetparfeu.model.decision;

/**
 * AcceptAction - Accepter le paquet.
 * Implémente Action (interface fonctionnelle).
 */
final class AcceptAction implements Action {
    
    private static final AcceptAction INSTANCE = new AcceptAction();
    
    private AcceptAction() {}
    
    public static AcceptAction getInstance() {
        return INSTANCE;
    }
    
    @Override
    public String getDescription() {
        return "Accepté";
    }
    
    // Les méthodes default de l'interface sont utilisées automatiquement
    // Pas besoin d'override getSeverity() ni getSymbol()
    
    @Override
    public String toString() {
        return "ACCEPT";
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof AcceptAction;
    }
    
    @Override
    public int hashCode() {
        return AcceptAction.class.hashCode();
    }
}