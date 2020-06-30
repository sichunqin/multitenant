package com.example.config.multitenant.database;

import com.example.config.TenantContext;
import com.example.repository.CityRepository;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
public class TenantDataSource implements Serializable {

    public HashMap<String, DataSource> getDataSources() {
        return dataSources;
    }

    private HashMap<String, DataSource> dataSources = new HashMap<>();

    @Autowired
    private DataSourceConfigRepository configRepo;

    @Autowired
    private CityRepository cityRepository;

    public DataSource getDataSource(String name) {
        if (dataSources.get(name) != null) {
            return dataSources.get(name);
        }
        DataSource dataSource = createDataSource(name);
        if (dataSource != null) {
            dataSources.put(name, dataSource);
        }
        return dataSource;
    }

    @PostConstruct
    public Map<String, DataSource> getAll() {
        List<DataSourceConfig> configList = configRepo.findAll();
        Map<String, DataSource> result = new HashMap<>();
        for (DataSourceConfig config : configList) {
            DataSource dataSource = getDataSource(config.getName());
            result.put(config.getName(), dataSource);
        }

        return result;
    }

  //  @Scheduled(fixedRate = 2000)
    public void scheduleTask() {

        dataSources.forEach((k, v) -> {
            if(!k.equals("Public")){
                TenantContext.setCurrentTenant(k);
                System.out.print(cityRepository.findAll().toString());
            }

        });
    }

    @Async
    public CompletableFuture<DataSource> loadDataSourceFromDB(String name) {
        DataSourceConfig config = configRepo.findByName(name);
        if (config != null) {
            DataSourceBuilder factory = DataSourceBuilder
                    .create().driverClassName(config.getDriverClassName())
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .url(config.getUrl());
            DataSource ds = factory.build();
            if (config.getInitialize()) {
                initialize(ds);
            }
            return CompletableFuture.completedFuture(ds);
        }
        return CompletableFuture.completedFuture(null);
    }


    public DataSource loaddatasourcefromdb(String name) {
        DataSourceConfig config = configRepo.findByName(name);
        if (config != null) {
            DataSourceBuilder factory = DataSourceBuilder
                    .create().driverClassName(config.getDriverClassName())
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .url(config.getUrl());
            DataSource ds = factory.build();
            if (config.getInitialize()) {
                initialize(ds);
            }
            return ds;
        }
        return null;
    }

    private DataSource createDataSource(String name) {
        DataSourceConfig config = configRepo.findByName(name);
        if (config != null) {
            DataSourceBuilder factory = DataSourceBuilder
                    .create().driverClassName(config.getDriverClassName())
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .url(config.getUrl());
            DataSource ds = factory.build();
            if (config.getInitialize()) {
                initialize(ds);
            }
            return ds;
        }
        return null;
    }

    private void initialize(DataSource dataSource) {
        //ClassPathResource schemaResource = new ClassPathResource("schema.sql");
        //ClassPathResource dataResource = new ClassPathResource("data.sql");
        //ResourceDatabasePopulator populator = new ResourceDatabasePopulator(schemaResource, dataResource);
        //populator.execute(dataSource);
    }


}
