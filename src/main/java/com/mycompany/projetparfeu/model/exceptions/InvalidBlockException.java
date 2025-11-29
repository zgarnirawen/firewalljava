/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projetparfeu.model.exceptions;

/**
 *
 * @author ZGARNI
 */

/**
 * Exception levée lorsqu’un bloc de la blockchain est invalide
 * (hash incorrect, précédent hash non cohérent, etc.).
 */
public final class InvalidBlockException extends Exception {

    public InvalidBlockException() {
        super("Bloc invalide dans la blockchain.");
    }

    public InvalidBlockException(String message) {
        super(message);
    }

    public InvalidBlockException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidBlockException(Throwable cause) {
        super(cause);
    }
}
