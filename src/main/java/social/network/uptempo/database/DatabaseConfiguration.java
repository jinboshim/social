package social.network.uptempo.database;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@MapperScan(value="social.network.uptempo.batch.mapper", sqlSessionFactoryRef="batchSqlSessionFactory")
@PropertySource("classpath:/application.properties")
@EnableTransactionManagement
public class DatabaseConfiguration{
	
	@Bean(name="batchDataSource")
	@ConfigurationProperties(prefix="mysql")
	public DataSource batchDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name="batchSqlSessionFactory")
	public SqlSessionFactory batchSqlSessionFactory(@Qualifier("batchDataSource") DataSource batchDataSource
			, ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(batchDataSource);
//        sqlSessionFactoryBean.setTypeAliasesPackage("com.test.api.entity, com.test.api.vo");
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis/sqlmaps/**.xml"));
        
        Resource configResource = new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/sql-map-config.xml");
        sqlSessionFactoryBean.setConfigLocation(configResource);
        
        return sqlSessionFactoryBean.getObject();
	}
		
	
	@Bean(name = "batchSessionTemplate")
    public SqlSessionTemplate batchSqlSessionTemplate(@Qualifier("batchSqlSessionFactory") SqlSessionFactory batchSqlSessionFactory) {
        return new SqlSessionTemplate(batchSqlSessionFactory);
    }
	
	@Bean(name="transactionManager")
	public PlatformTransactionManager transactionManager() throws Exception {
		return new DataSourceTransactionManager(batchDataSource());
	}
	
	
	 
	
	
//	@Autowired
//    private DataSource dataSource;
//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//        
////        String[] mapperLocations = {"classpath:mybatis/sqlmaps/*.xml","classpath:mybatis/sqlmaps/v2/*.xml"};
////        sqlSessionFactoryBean.setMapperLocations(resolveMapperLocations(mapperLocations));
//        
//        Resource[] mapperResource = new PathMatchingResourcePatternResolver()
//                .getResources("classpath:mybatis/sqlmaps/*.xml");
//        sqlSessionFactoryBean.setMapperLocations(mapperResource);
//        
//        Resource configResource = new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/sql-map-config.xml");
//        sqlSessionFactoryBean.setConfigLocation(configResource);
//        
//        sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
////        sqlSessionFactoryBean.setTransactionFactory(new ManagedTransactionFactory());
//        return sqlSessionFactoryBean.getObject();
//    }
//    
//    @Bean(name="transactionManager")
//	public PlatformTransactionManager transactionManager() throws Exception {
//		return new DataSourceTransactionManager(batchDataSource);
//	}
//    
//    private Resource[] resolveMapperLocations(String[] mapperLocations) {
//        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
//        List<Resource> resources = new ArrayList<Resource>();
//        if (mapperLocations != null) {
//            for (String mapperLocation : mapperLocations) {
//                try {
//                    Resource[] mappers = resourceResolver.getResources(mapperLocation);
//                    resources.addAll(Arrays.asList(mappers));
//                } catch (IOException e) {
//                    // ignore
//                }
//            }
//        }
//        return resources.toArray(new Resource[resources.size()]);
//    }
}
