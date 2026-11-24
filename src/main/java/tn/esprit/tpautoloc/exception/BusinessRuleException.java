package tn.esprit.tpautoloc.exception;

/**
 * Violation d'une règle métier (ex. désaffecter un véhicule qui n'est rattaché
 * à aucune agence). Traduite en HTTP 409 (Conflict) par le GlobalExceptionHandler.
 */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
