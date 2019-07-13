package com.sigma.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.sigma.domain.Servicio.class.getName());
            createCache(cm, com.sigma.domain.Servicio.class.getName() + ".monitoreoServicios");
            createCache(cm, com.sigma.domain.MonitoreoServicio.class.getName());
            createCache(cm, com.sigma.domain.ComponenteMonitoreo.class.getName());
            createCache(cm, com.sigma.domain.ComponenteMonitoreo.class.getName() + ".paramentroMonitoreos");
            createCache(cm, com.sigma.domain.ParamentroMonitoreo.class.getName());
            createCache(cm, com.sigma.domain.ParamentroMonitoreo.class.getName() + ".monitoreoServicios");
            createCache(cm, com.sigma.domain.TipoServicios.class.getName());
            createCache(cm, com.sigma.domain.TipoServicios.class.getName() + ".sevicios");
            createCache(cm, com.sigma.domain.TipoSolicitud.class.getName());
            createCache(cm, com.sigma.domain.TipoSolicitud.class.getName() + ".sevicios");
            createCache(cm, com.sigma.domain.TipoInduccion.class.getName());
            createCache(cm, com.sigma.domain.TipoInduccion.class.getName() + ".sevicios");
            createCache(cm, com.sigma.domain.RequisitosSeguridad.class.getName());
            createCache(cm, com.sigma.domain.RequisitosSeguridad.class.getName() + ".sevicios");
            createCache(cm, com.sigma.domain.Cliente.class.getName());
            createCache(cm, com.sigma.domain.Cliente.class.getName() + ".sedes");
            createCache(cm, com.sigma.domain.Sede.class.getName());
            createCache(cm, com.sigma.domain.Sede.class.getName() + ".contactoSedes");
            createCache(cm, com.sigma.domain.Sede.class.getName() + ".servicios");
            createCache(cm, com.sigma.domain.ContactoSede.class.getName());
            createCache(cm, com.sigma.domain.Distrito.class.getName());
            createCache(cm, com.sigma.domain.Distrito.class.getName() + ".sedes");
            createCache(cm, com.sigma.domain.Provincia.class.getName());
            createCache(cm, com.sigma.domain.Provincia.class.getName() + ".distritos");
            createCache(cm, com.sigma.domain.Departamento.class.getName());
            createCache(cm, com.sigma.domain.Departamento.class.getName() + ".provincias");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
