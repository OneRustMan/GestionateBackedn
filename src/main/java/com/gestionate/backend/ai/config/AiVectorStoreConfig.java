package com.gestionate.backend.ai.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class AiVectorStoreConfig {

    @Value("${spring.datasource.url}")
    private String mainDatabaseUrl;

    @Value("${spring.datasource.username}")
    private String mainDatabaseUsername;

    @Value("${spring.datasource.password}")
    private String mainDatabasePassword;

    @Value("${spring.datasource.driver-class-name}")
    private String mainDatabaseDriverClassName;

    @Value("${ai.vector.datasource.url}")
    private String aiDatabaseUrl;

    @Value("${ai.vector.datasource.username}")
    private String aiDatabaseUsername;

    @Value("${ai.vector.datasource.password}")
    private String aiDatabasePassword;

    @Value("${ai.vector.datasource.driver-class-name}")
    private String aiDatabaseDriverClassName;

    @Value("${ai.gemini.embedding-dimensions}")
    private Integer embeddingDimensions;

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(mainDatabaseUrl)
                .username(mainDatabaseUsername)
                .password(mainDatabasePassword)
                .driverClassName(mainDatabaseDriverClassName)
                .build();
    }

    @Bean(name = "aiDataSource")
    public DataSource aiDataSource() {
        return DataSourceBuilder.create()
                .url(aiDatabaseUrl)
                .username(aiDatabaseUsername)
                .password(aiDatabasePassword)
                .driverClassName(aiDatabaseDriverClassName)
                .build();
    }

    @Bean(name = "aiJdbcTemplate")
    public JdbcTemplate aiJdbcTemplate(@Qualifier("aiDataSource") DataSource aiDataSource) {
        return new JdbcTemplate(aiDataSource);
    }

    @Bean(name = "vectorStore")
    public VectorStore vectorStore(
            @Qualifier("aiJdbcTemplate") JdbcTemplate aiJdbcTemplate,
            EmbeddingModel embeddingModel) {

        return PgVectorStore.builder(aiJdbcTemplate, embeddingModel)
                .initializeSchema(true)
                .dimensions(embeddingDimensions)
                .build();
    }
}
