package org.lop.modules.repository.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import org.lop.modules.lang.money.Money;

/**
 * Money mybatis handler.
 * 
 * @author 潘瑞峥
 * @date 2014年8月11日
 */
@MappedTypes( Money.class )
@MappedJdbcTypes( value = { JdbcType.BIGINT, JdbcType.NUMERIC, JdbcType.DECIMAL } )
public class MoneyTypeHandler extends BaseTypeHandler<Money> {

	/**
	 * @see org.apache.ibatis.type.BaseTypeHandler#setNonNullParameter(java.sql.PreparedStatement,
	 *      int, java.lang.Object, org.apache.ibatis.type.JdbcType)
	 */
	@Override
	public void setNonNullParameter( PreparedStatement ps, int i, Money parameter, JdbcType jdbcType ) throws SQLException {
		ps.setLong( i, parameter.getCent() );
	}

	/**
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet,
	 *      java.lang.String)
	 */
	@Override
	public Money getNullableResult( ResultSet rs, String columnName ) throws SQLException {
		long result = rs.getLong( columnName );

		Money money = new Money();
		money.setCent( result );

		return money;
	}

	/**
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet, int)
	 */
	@Override
	public Money getNullableResult( ResultSet rs, int columnIndex ) throws SQLException {
		long result = rs.getLong( columnIndex );

		Money money = new Money();
		money.setCent( result );

		return money;
	}

	/**
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.CallableStatement,
	 *      int)
	 */
	@Override
	public Money getNullableResult( CallableStatement cs, int columnIndex ) throws SQLException {
		long result = cs.getLong( columnIndex );

		Money money = new Money();
		money.setCent( result );

		return money;
	}

}