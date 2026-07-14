package com.gettraining.util;

public final class DbConfig {

    public static final String DB_URL = getEnvOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/sdpfrequencia");
    public static final String DB_USER = getEnvOrDefault("DB_USER", "postgres");
    public static final String DB_PASSWORD = getEnvOrDefault("DB_PASSWORD", "atac");
    public static final String DB_SUPERUSER_URL = getEnvOrDefault("DB_SUPERUSER_URL", "jdbc:postgresql://localhost:5432/postgres");
    public static final String DB_SUPERUSER_USER = getEnvOrDefault("DB_SUPERUSER_USER", "postgres");
    public static final String DB_SUPERUSER_PASSWORD = getEnvOrDefault("DB_SUPERUSER_PASSWORD", "atac");
    public static final String ADMIN_USERNAME = getEnvOrDefault("DB_ADMIN_USERNAME", "admin");
    public static final String ADMIN_PASSWORD = getEnvOrDefault("DB_ADMIN_PASSWORD", "admin");

    private DbConfig() {
        // utilitário
    }

    private static String getEnvOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }

    private static String getRequiredEnv(String name, String errorMessage) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(errorMessage);
        }
        return value;
    }
}
