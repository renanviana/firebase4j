package com.renz.firebase4j;

import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Renan Viana
 */
public class F4JRegisterComponents implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents("com.renz.firebase4j");

        System.out.println("##### candidateComponents size: " + candidateComponents.size());
        for (BeanDefinition candidateComponent : candidateComponents) {
            System.out.println("##### beanName: " + candidateComponent.getBeanClassName());
            registry.registerBeanDefinition(candidateComponent.getBeanClassName(), candidateComponent);
        }
    }

}
