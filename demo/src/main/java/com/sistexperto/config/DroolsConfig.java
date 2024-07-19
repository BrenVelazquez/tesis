package com.sistexperto.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.domain.EntityScan;


import org.springframework.context.annotation.Configuration;


@Configuration

// Especifica el paquete donde están las clases de entidad
@EntityScan(basePackages = "com.sistexperto.model") 
public class DroolsConfig {
    //Almacena la ruta a su archivo de reglas de Drools
    private final static String RULES_DRL = "role/paciente.drl";
    //Esta línea crea una instancia de KieServices, que es el punto de entrada principal para interactuar con la API de Drools. Es un objeto singleton, lo que significa que solo existe una instancia en todo el ciclo de vida de la aplicación.
    private final KieServices kieServices = KieServices.Factory.get();

    //Esto es todo como viene de drools

    @Bean
    public KieContainer kieContainer(){
        // Crea un nuevo objeto KieFileSystem para gestionar los recursos utilizados para construir la base de conocimientos de Drools. 
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        //Lee el archivo de reglas especificado por RULES_DRL del classpath y lo añade al KieFileSystem.
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_DRL));
        //Crea un objeto KieBuilder utilizando el KieFileSystem que contiene su archivo de reglas. El KieBuilder es responsable de construir la base de conocimientos basándose en las reglas.
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        //Construye la base de conocimientos utilizando el KieBuilder y recupera el objeto KieModule resultante.
        KieModule kieModule = kieBuilder.getKieModule();
        //Crea un nuevo objeto KieContainer utilizando el ID de versión del KieModule. El KieContainer contendrá las reglas de Drools compiladas para su uso posterior
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
}
