package org.lop.modules.repository.mybatis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 标识MyBatis的DAO, 方便{@link org.mybatis.spring.mapper.MapperScannerConfigurer}扫描.
 * 
 * @author 潘瑞峥
 * @date 2014年7月28日
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
@Documented
@Component
public @interface MyBatisDao {

	String value() default "";

}