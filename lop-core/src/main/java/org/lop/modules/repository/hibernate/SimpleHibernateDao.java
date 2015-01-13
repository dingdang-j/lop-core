package org.lop.modules.repository.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.lop.modules.repository.mapper.QueryFilter;
import org.lop.modules.repository.mapper.QueryFilter.RestrictionType;

/**
 * SimpleHibernateDao.
 * 
 * 主要包含分页, 属性过滤查询等.
 * 
 * @author 潘瑞峥
 * @date 2013-5-31
 */
public class SimpleHibernateDao<T, PK extends Serializable> extends CrudHibernateDao<T, PK> {

	/**
	 * 用于Repository层子类使用的构造函数. 通过子类的泛型定义取得对象类型Class.
	 * 
	 * eg. <code>public class UserRepository extends SimpleHibernateDao<User, String></code>
	 */
	public SimpleHibernateDao() {
		super();
	}

	/**
	 * 用于省略Repository层, 在Service层直接使用通用SimpleHibernateDao的构造函数. 在构造函数中定义对象类型Class.
	 * 
	 * eg.
	 * <code>SimpleHibernateDao<User, String> userRepository = new SimpleHibernateDao<User, String>(User.class);</code>
	 */
	public SimpleHibernateDao( final Class<T> entityClass ) {
		super( entityClass );
	}

	/**
	 * 用于省略Repository层, 在Service层直接使用通用SimpleHibernateDao的构造函数. 在构造函数中定义对象类型Class.
	 * 
	 * eg.
	 * <code>SimpleHibernateDao<User, String> userRepository = new SimpleHibernateDao<User, String>(sessionFactory, User.class);</code>
	 */
	public SimpleHibernateDao( final SessionFactory sessionFactory, final Class<T> entityClass ) {
		super( sessionFactory, entityClass );
	}

	/**
	 * 分页获取全部对象.
	 */
	public Page<T> getAll( final Pageable pageable ) {
		return this.findPage( pageable );
	}

	/**
	 * 根据HQL分页查询.
	 * 
	 * @param values
	 *            数量可变的参数, 按顺序绑定.
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings( "unchecked" )
	public Page<T> findPage( final Pageable pageable, final String hql, final Object... values ) {
		Validate.notNull( pageable, "pageable不能为空" );

		long total = this.count( hql, values );

		Query query = super.createQuery( hql, values );
		this.setPageParameter( query, pageable );

		List<T> content = ( total > pageable.getOffset() ) ? query.list() : Collections.<T> emptyList();
		return new PageImpl<T>( content, pageable, total );
	}

	/**
	 * 根据HQL分页查询.
	 * 
	 * @param values
	 *            命名参数, 按名称绑定.
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings( "unchecked" )
	public Page<T> findPage( final Pageable pageable, final String hql, final Map<String, ?> values ) {
		Validate.notNull( pageable, "pageable不能为空" );

		long total = this.count( hql, values );

		Query query = super.createQuery( hql, values );
		this.setPageParameter( query, pageable );

		List<T> content = ( total > pageable.getOffset() ) ? query.list() : Collections.<T> emptyList();
		return new PageImpl<T>( content, pageable, total );
	}

	/**
	 * 按Criteria分页查询.
	 * 
	 * @param criterions
	 *            数量可变的Criterion.
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings( "unchecked" )
	public Page<T> findPage( final Pageable pageable, final Criterion... criterions ) {
		Validate.notNull( pageable, "pageable不能为空" );

		Criteria criteria = super.createCriteria( criterions );

		long total = this.count( criteria );

		this.setPageParameter( criteria, pageable );

		List<T> content = ( total > pageable.getOffset() ) ? criteria.list() : Collections.<T> emptyList();
		return new PageImpl<T>( content, pageable, total );
	}

	/**
	 * 按Criteria分页查询.
	 * 
	 * @param criterions
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings( "unchecked" )
	public Page<T> findPage( final Pageable pageable, final Collection<Criterion> criterions ) {
		Validate.notNull( pageable, "pageable不能为空" );

		Criteria criteria = super.createCriteria( criterions );

		long total = this.count( criteria );

		this.setPageParameter( criteria, pageable );

		List<T> content = ( total > pageable.getOffset() ) ? criteria.list() : Collections.<T> emptyList();
		return new PageImpl<T>( content, pageable, total );
	}

	/**
	 * 获取Hql的总记录数.
	 */
	protected long count( final String hql, final Object... values ) {
		String countHql = prepareCountHql( hql );

		try {
			Long count = this.findUnique( countHql, values );
			return count;
		} catch ( Exception e ) {
			throw new RuntimeException( "hql can't be auto count, hql is: " + countHql, e );
		}
	}

	/**
	 * 获取Hql的总记录数.
	 */
	protected long count( final String hql, final Map<String, ?> values ) {
		String countHql = prepareCountHql( hql );

		try {
			Long count = this.findUnique( countHql, values );
			return count;
		} catch ( Exception e ) {
			throw new RuntimeException( "hql can't be auto count, hql is: " + countHql, e );
		}
	}

	/**
	 * 执行count查询获得本次Criteria查询所能获得的对象总数.
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" } )
	protected long count( final Criteria criteria ) {
		CriteriaImpl criteriaImpl = ( CriteriaImpl ) criteria;

		/* 先把Projection, ResultTransformer, OrderBy取出, 清空三者后再count. */
		Projection projection = criteriaImpl.getProjection();
		ResultTransformer transformer = criteriaImpl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = ( List ) FieldUtils.readField( criteriaImpl, "orderEntries", true );
			FieldUtils.writeField( criteriaImpl, "orderEntries", new ArrayList(), true );
		} catch ( Exception e ) {
			logger.error( "不可能抛出的异常: {}", e.getMessage() );
		}

		// count.
		Long totalCountObject = ( Long ) criteria.setProjection( Projections.rowCount() ).uniqueResult();
		long totalCount = ( null == totalCountObject ) ? 0 : totalCountObject;

		/* 重新设置之前的Projection, ResultTransformer, OrderBy. */
		criteria.setProjection( projection );
		if ( null == projection ) {
			criteria.setResultTransformer( CriteriaSpecification.ROOT_ENTITY );
		}
		if ( null != transformer ) {
			criteria.setResultTransformer( transformer );
		}
		try {
			FieldUtils.writeField( criteriaImpl, "orderEntries", orderEntries, true );
		} catch ( Exception e ) {
			logger.error( "不可能抛出的异常: {}", e.getMessage() );
		}

		return totalCount;
	}

	/**
	 * 设置Query的分页参数.
	 */
	protected Query setPageParameter( final Query query, final Pageable pageable ) {
		Validate.isTrue( pageable.getPageSize() > 0, "Page Size must larger than zero" );

		// hibernate的firstResult的序号从0开始.
		query.setFirstResult( pageable.getOffset() );
		query.setMaxResults( pageable.getPageSize() );
		return query;
	}

	/**
	 * 设置Criteria的分页, 排序参数.
	 */
	protected Criteria setPageParameter( final Criteria criteria, final Pageable pageable ) {
		Validate.isTrue( pageable.getPageSize() > 0, "Page Size must larger than zero" );

		// hibernate的firstResult的序号从0开始.
		criteria.setFirstResult( pageable.getOffset() );
		criteria.setMaxResults( pageable.getPageSize() );

		/* 排序. */
		Sort sort = pageable.getSort();
		if ( null != sort ) {
			for ( org.springframework.data.domain.Sort.Order order : sort ) {
				if ( order.isAscending() ) {
					criteria.addOrder( Order.asc( order.getProperty() ) );
				} else {
					criteria.addOrder( Order.desc( order.getProperty() ) );
				}
			}
		}

		return criteria;
	}

	/**
	 * 根据属性查询, 支持多种匹配方式.
	 */
	public List<T> findBy( final RestrictionType restrictionType, final String propertyName, final Object value ) {
		Criterion criterion = this.createCriterion( restrictionType, propertyName, value );
		return this.find( criterion );
	}

	/**
	 * 根据属性过滤条件查询.
	 */
	public List<T> find( final List<QueryFilter> filters ) {
		Criterion[] criterions = this.createCriterionByFilter( filters );
		return this.find( criterions );
	}

	/**
	 * 根据属性过滤条件分页查询.
	 */
	public Page<T> findPage( final Pageable pageable, final List<QueryFilter> filters ) {
		Criterion[] criterions = this.createCriterionByFilter( filters );
		return this.findPage( pageable, criterions );
	}

	/**
	 * 根据属性条件参数创建Criterion.
	 */
	protected Criterion createCriterion( final RestrictionType restrictionType, final String propertyName, final Object value ) {
		Validate.notNull( restrictionType, "restrictionType不能为空" );
		Validate.notBlank( propertyName, "propertyName不能为空" );
		Validate.notNull( value, "value不能为空" );

		logger.debug( "QueryFilter: {} {} {}", new Object[] { propertyName, restrictionType, value } );

		Criterion criterion = null;
		/* 根据RestrictionType构造criterion. */
		switch ( restrictionType ) {
			case LT:
				criterion = Restrictions.lt( propertyName, value );
				break;
			case LE:
				criterion = Restrictions.le( propertyName, value );
				break;
			case GT:
				criterion = Restrictions.gt( propertyName, value );
				break;
			case GE:
				criterion = Restrictions.ge( propertyName, value );
				break;
			case IN:
				if ( value instanceof Collection ) {
					criterion = Restrictions.in( propertyName, ( Collection<?> ) value );
				} else if ( value instanceof Object[] ) {
					criterion = Restrictions.in( propertyName, ( Object[] ) value );
				}
				break;

			case EQ:
				criterion = Restrictions.eq( propertyName, value );
				break;
			case NE:
				criterion = Restrictions.ne( propertyName, value );
				break;
			case LIKE:
				criterion = Restrictions.like( propertyName, ( String ) value, MatchMode.ANYWHERE );
				break;
			case LIKESTART:
				criterion = Restrictions.like( propertyName, ( String ) value, MatchMode.START );
				break;
			case LIKEEND:
				criterion = Restrictions.like( propertyName, ( String ) value, MatchMode.END );
				break;
		}
		return criterion;
	}

	/**
	 * 根据属性过滤条件创建Criterion数组.
	 */
	protected Criterion[] createCriterionByFilter( final Collection<QueryFilter> filters ) {
		List<Criterion> criterionList = new ArrayList<Criterion>();

		for ( QueryFilter filter : filters ) {
			// 只有一个属性需要比较的情况.
			if ( !filter.hasMultiProperties() ) {
				Criterion criterion = createCriterion( filter.getRestrictionType(), filter.getPropertyName(), filter.getValue() );
				criterionList.add( criterion );
			}
			// 包含多个属性需要比较的情况, 进行or处理.
			else {
				Disjunction disjunction = Restrictions.disjunction();
				for ( String propertyName : filter.getPropertyNames() ) {
					Criterion criterion = createCriterion( filter.getRestrictionType(), propertyName, filter.getValue() );
					disjunction.add( criterion );
				}
				criterionList.add( disjunction );
			}
		}
		return criterionList.toArray( new Criterion[criterionList.size()] );
	}

	/**
	 * SELECT和ORDER BY会影响COUNT查询, 进行简单的排除.
	 */
	public static String prepareCountHql( String orgHql ) {
		String fromHql = orgHql;

		int start = StringUtils.indexOfIgnoreCase( fromHql, "FROM" );
		int end = StringUtils.indexOfIgnoreCase( fromHql, "ORDER BY" );
		if ( -1 == end ) {
			end = fromHql.length();
		}
		fromHql = StringUtils.substring( fromHql, start, end );

		String countHql = "SELECT COUNT(*) " + fromHql;
		return countHql;
	}

}