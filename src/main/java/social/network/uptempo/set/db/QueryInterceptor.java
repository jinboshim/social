/**
 * @author jinbos
 * @version 00.00.01
 * @date 2012.11.12
 * @category kr.co.etribe.framework.db
 * @description Mybatis를 통한 DB 에 Data 조회시 관련 Query 및 Parameter를 Log4j 및 Log 파일로 생성 가능한 로직
 */
package social.network.uptempo.set.db;

import java.sql.Statement;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({@Signature(type=StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
@Signature(type=Executor.class, method="update", args={MappedStatement.class,  Object.class})})
public class QueryInterceptor implements Interceptor {
	private static Logger logger = LoggerFactory.getLogger(QueryInterceptor.class);
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if(logger.isDebugEnabled()){
			logger.debug("@>> start QueryInterceptor.");
			String sql = "";
			String param = "";
			if("query".equals(invocation.getMethod().getName())){
				StatementHandler handler = (StatementHandler)invocation.getTarget();
				// Query
				sql = handler.getBoundSql().getSql();
				param = handler.getParameterHandler().getParameterObject() != null ? handler.getParameterHandler().getParameterObject().toString() : "";
			}else if("update".equals(invocation.getMethod().getName())){
				Object parameterObject = invocation.getArgs()[1];
				sql = ((MappedStatement)invocation.getArgs()[0]).getBoundSql(parameterObject).getSql();
				param = parameterObject.toString();
			}
			logger.debug("@@@@@@@@@ interceptor method is : " + invocation.getMethod().getName());
			logger.debug("@@@@@@@@@ query @@@@@@@@@@\n"+sql);
			logger.debug("@@@@@@@@@ parameter @@@@@@@@@@\n"+param);
			logger.debug("@>> end QueryInterceptor.");
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties arg0) {

	}

}
