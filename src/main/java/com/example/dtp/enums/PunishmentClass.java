package com.example.dtp.enums;

public enum PunishmentClass {
    WAITING,
    INNOCENT,
    PENALTY,
    LICENSE_DEPRIVATION,
    ARRESTING;
    
    public static punishmentClass convert (String punishmentRequest) {
        String punishment = punishmentRequest.toUpperCase();
        for (punishmentClass p : punishmentClass.values()) {
            if (p.toString().equals(punishment)) {
                return p;
            }
        }
        return null;
    }
}
