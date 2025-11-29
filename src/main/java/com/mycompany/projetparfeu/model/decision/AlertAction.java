package com.mycompany.projetparfeu.model.decision;

public final class AlertAction implements Action {
    
    private static final AlertAction INSTANCE = new AlertAction();
    
    private AlertAction() {}
    
    public static AlertAction getInstance() {
        return INSTANCE;
    }
    
    @Override
    public String getDescription() {
        return "Alerte générée";
    }
    
    @Override
    public String toString() {
        return "ALERT";
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof AlertAction;
    }
    
    @Override
    public int hashCode() {
        return AlertAction.class.hashCode();
    }
}