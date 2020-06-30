package com.example.config.multitenant.database;

import com.example.repository.CityRepository;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


import static com.example.constant.MultiTenantConstants.DEFAULT_TENANT_ID;

@Component

public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    @Autowired
    private DataSource defaultDS;

    @Autowired
    private ApplicationContext context;

    private Map<String, DataSource> map = new HashMap<>();

    private boolean init = false;
    private boolean needReload = false;



    @PostConstruct
    public void load() {
        map.put(DEFAULT_TENANT_ID, defaultDS);
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return map.get(DEFAULT_TENANT_ID);
    }

    @Scheduled(fixedRate = 10000)
    public void scheduleTaskWithReloadDataSource() {
        TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
        if(needReload){
            map.putAll(tenantDataSource.getAll());
        }
        needReload = false;
    }


    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
        if (!init) {
            init = true;
            map.putAll(tenantDataSource.getAll());
        }

        if(map.get(tenantIdentifier) == null) {
            needReload = true;
        }
        return map.get(tenantIdentifier) != null ? map.get(tenantIdentifier) : map.get(DEFAULT_TENANT_ID);
    }
}

