package app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum Role {
    ADMIN,
    USER;

    /** Torna o parse case-insensitive e gera mensagem amigável. */
    @JsonCreator
    public static Role from(String value) {
        return Arrays.stream(values())
                     .filter(r -> r.name().equalsIgnoreCase(value))
                     .findFirst()
                     .orElseThrow(() ->
                         new IllegalArgumentException(
                             "Valor inválido para 'role'. Use ADMIN ou USER."
                         ));
    }

    @JsonValue          // faz o enum sair como string no JSON
    public String toValue() {
        return name();
    }
}
