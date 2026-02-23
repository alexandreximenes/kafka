package com.learnavro;

public class SchemaRegistryChoice {

    public static boolean SCHEMA_REGISTRY_OFF = Boolean.FALSE;

    public static boolean SCHEMA_REGISTRY_CONFLUENT = Boolean.TRUE;

    public static boolean SCHEMA_REGISTRY_APICURIO = !SCHEMA_REGISTRY_CONFLUENT;


}
