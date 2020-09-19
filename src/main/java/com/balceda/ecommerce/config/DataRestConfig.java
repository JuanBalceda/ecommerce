package com.balceda.ecommerce.config;

import com.balceda.ecommerce.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public DataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        HttpMethod[] unsupportedActions = {HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};

        disableHttpMethods(config, unsupportedActions, Product.class);
        disableHttpMethods(config, unsupportedActions, ProductCategory.class);
        disableHttpMethods(config, unsupportedActions, Country.class);
        disableHttpMethods(config, unsupportedActions, State.class);

        exposeIds(config);
    }

    private void disableHttpMethods(RepositoryRestConfiguration config, HttpMethod[] unsupportedActions, Class<? extends JpaEntity> clazz) {

        config.getExposureConfiguration()
                .forDomainType(clazz)
                .withItemExposure(((metadata, httpMethods) -> httpMethods.disable(unsupportedActions)))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        Class[] domainTypes = entityManager.getMetamodel().getEntities().stream()
                .map(EntityType::getJavaType)
                .toArray(Class[]::new);

        config.exposeIdsFor(domainTypes);
    }
}
