package org.lop.modules.repository.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import org.lop.modules.utils.ReflectionUtils;

/**
 * 封装Hibernate原生API的Repository.
 * 
 * @author 潘瑞峥
 * @date 2013-5-31
 */
public class CrudHibernateDao<T, PK extends Serializable> {

	protected Logger logger = LoggerFactory.getLogger( this.getClass() );

	protected SessionFactory sessionFactory;

	protected Class<T> entityClass;

	/**
	 * 用于Repository层子类使用的构造函数. 通过子类的泛型定义取得对象类型Class.
	 * 
	 * eg. <code>public class UserRepository extends CrudHibernateDao<User, String></code>
	 */
	public CrudHibernateDao() {
		this.entityClass = ReflectionUtils.getSuperClassGenricType( this.getClass() );
	}

	/**
	 * 用于省略Repository层, 在Service层直接使用通用CrudHibernateDao的构造函数. 在构造函数中定义对象类型Class.
	 * 
	 * eg.
	 * <code>CrudHibernateDao<User, String> userRepository = new CrudHibernateDao<User, String>(User.class);</code>
	 */
	public CrudHibernateDao( final Class<T> entityClass ) {
		this.entityClass = entityClass;
	}

	/**
	 * 用于省略Repository层, 在Service层直接使用通用CrudHibernateDao的构造函数. 在构造函数中定义对象类型Class.
	 * 
	 * eg.
	 * <code>CrudHibernateDao<User, String> userRepository = new CrudHibernateDao<User, String>(sessionFactory, User.class);</code>
	 */
	public CrudHibernateDao( final SessionFactory sessionFactory, final Class<T> entityClass ) {
		this.sessionFactory = sessionFactory;
		this.entityClass = entityClass;
	}

	/**
	 * 获取sessionFactory.
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 注入SessionFactory, 当有多个SesionFactory的时候在子类重写该函数.
	 */
	@Autowired
	public void setSessionFactory( final SessionFactory sessionFactory ) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 获取当前Session.
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 保存新增或修改的对象并返回.
	 */
	@Transactional
	public T save( final T entity ) {
		Validate.notNull( entity, "entity不能为空" );

		this.getSession().saveOrUpdate( entity );
		logger.debug( "save entity: {}", entity );

		return entity;
	}

	/**
	 * 批量保存新增或修改的对象并返回.
	 */
	@Transactional
	public List<T> save( final Collection<T> entities ) {
		List<T> result = null;

		if ( CollectionUtils.isEmpty( entities ) ) {
			return result;
		}

		result = Lists.newArrayList();
		for ( T entity : entities ) {
			result.add( this.save( entity ) );
		}

		return result;
	}

	/**
	 * 删除对象.
	 * 
	 * @param entity
	 *            对象必须是session中的对象或含id属性的transient对象.
	 */
	@Transactional
	public void delete( final T entity ) {
		Validate.notNull( entity, "entity不能为空" );

		this.getSession().delete( entity );
		logger.debug( "delete entity: {}", entity );
	}

	/**
	 * 根据id删除对象.
	 */
	@Transactional
	public void delete( final PK id ) {
		Validate.notNull( id, "id不能为空" );

		this.delete( get( id ) );
		logger.debug( "delete entity {}, id is {}", entityClass.getSimpleName(), id );
	}

	/**
	 * 批量根据id删除对象.
	 */
	@Transactional
	public void delete( final Collection<PK> ids ) {
		Validate.notNull( ids, "ids不能为空" );

		for ( PK id : ids ) {
			this.delete( id );
		}
	}

	/**
	 * 根据id获取对象.
	 */
	@SuppressWarnings( "unchecked" )
	public T get( final PK id ) {
		Validate.notNull( id, "id不能为空" );

		return ( T ) this.getSession().load( entityClass, id );
	}

	/**
	 * 根据id列表获取对象列表.
	 */
	public List<T> get( final Collection<PK> ids ) {
		if ( CollectionUtils.isNotEmpty( ids ) ) {
			return this.find( Restrictions.in( this.getIdName(), ids ) );
		}
		return null;
	}

	/**
	 * 获取全部对象.
	 */
	public List<T> getAll() {
		return this.find();
	}

	/**
	 * 获取全部对象, 支持按属性排序.
	 */
	@SuppressWarnings( "unchecked" )
	public List<T> getAll( final Sort sort ) {
		Criteria criteria = createCriteria();

		if ( null != sort ) {
			for ( org.springframework.data.domain.Sort.Order order : sort ) {
				if ( order.isAscending() ) {
					criteria.addOrder( Order.asc( order.getProperty() ) );
				} else {
					criteria.addOrder( Order.desc( order.getProperty() ) );
				}
			}
		}

		return criteria.list();
	}

	/**
	 * 根据属性查找对象列表, 匹配方式为相等.
	 */
	public List<T> findBy( final String propertyName, final Object value ) {
		Validate.notBlank( propertyName, "propertyName不能为空" );

		Criterion criterion = Restrictions.eq( propertyName, value );
		return this.find( criterion );
	}

	/**
	 * 根据属性查找唯一对象, 匹配方式为相等.
	 */
	@SuppressWarnings( "unchecked" )
	public T findUniqueBy( final String propertyName, final Object value ) {
		Validate.notBlank( propertyName, "propertyName不能为空" );

		Criterion criterion = Restrictions.eq( propertyName, value );
		return ( T ) this.createCriteria( criterion ).uniqueResult();
	}

	/**
	 * 根据HQL查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数, 按顺序绑定.
	 */
	@SuppressWarnings( "unchecked" )
	public <X> List<X> find( final String hql, final Object... values ) {
		return this.createQuery( hql, values ).list();
	}

	/**
	 * 根据HQL查询对象列表.
	 * 
	 * @param values
	 *            命名参数, 按名称绑定.
	 */
	@SuppressWarnings( "unchecked" )
	public <X> List<X> find( final String hql, final Map<String, ?> values ) {
		return this.createQuery( hql, values ).list();
	}

	/**
	 * 根据HQL查询唯一对象.
	 * 
	 * @param values
	 *            数量可变的参数, 按顺序绑定.
	 */
	@SuppressWarnings( "unchecked" )
	public <X> X findUnique( final String hql, final Object... values ) {
		return ( X ) this.createQuery( hql, values ).uniqueResult();
	}

	/**
	 * 根据HQL查询唯一对象.
	 * 
	 * @param values
	 *            命名参数, 按名称绑定.
	 */
	@SuppressWarnings( "unchecked" )
	public <X> X findUnique( final String hql, final Map<String, ?> values ) {
		return ( X ) this.createQuery( hql, values ).uniqueResult();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values
	 *            数量可变的参数, 按顺序绑定.
	 * @return 更新记录数.
	 */
	public int batchExecute( final String hql, final Object... values ) {
		return this.createQuery( hql, values ).executeUpdate();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values
	 *            命名参数, 按名称绑定.
	 * @return 更新记录数.
	 */
	public int batchExecute( final String hql, final Map<String, ?> values ) {
		return this.createQuery( hql, values ).executeUpdate();
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 
	 * @param values
	 *            数量可变的参数, 按顺序绑定.
	 */
	public final Query createQuery( final String hql, final Object... values ) {
		Validate.notBlank( hql, "hql不能为空" );

		Query query = this.getSession().createQuery( hql );
		if ( ArrayUtils.isNotEmpty( values ) ) {
			for ( int i = 0; i < values.length; i++ ) {
				query.setParameter( i, values[ i ] );
			}
		}
		return query;
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 
	 * @param values
	 *            命名参数, 按名称绑定.
	 */
	public final Query createQuery( final String hql, final Map<String, ?> values ) {
		Validate.notBlank( hql, "hql不能为空" );

		Query query = this.getSession().createQuery( hql );
		if ( MapUtils.isNotEmpty( values ) ) {
			query.setProperties( values );
		}
		return query;
	}

	/**
	 * 根据Criterion查询对象列表.
	 */
	@SuppressWarnings( "unchecked" )
	public List<T> find( final Criterion... criterions ) {
		return this.createCriteria( criterions ).list();
	}

	/**
	 * 根据Criterion查询对象列表.
	 */
	@SuppressWarnings( "unchecked" )
	public List<T> find( final Collection<Criterion> criterions ) {
		return this.createCriteria( criterions ).list();
	}

	/**
	 * 根据Criterion查询唯一对象.
	 */
	@SuppressWarnings( "unchecked" )
	public T findUnique( final Criterion... criterions ) {
		return ( T ) this.createCriteria( criterions ).uniqueResult();
	}

	/**
	 * 根据Criterion查询唯一对象.
	 */
	@SuppressWarnings( "unchecked" )
	public T findUnique( final Collection<Criterion> criterions ) {
		return ( T ) this.createCriteria( criterions ).uniqueResult();
	}

	/**
	 * 根据Criterion条件创建Criteria.
	 */
	public final Criteria createCriteria( final Criterion... criterions ) {
		Criteria criteria = this.getSession().createCriteria( entityClass );

		if ( ArrayUtils.isNotEmpty( criterions ) ) {
			for ( Criterion criterion : criterions ) {
				criteria.add( criterion );
			}
		}
		return criteria;
	}

	/**
	 * 根据Criterion条件创建Criteria.
	 */
	public final Criteria createCriteria( final Collection<Criterion> criterions ) {
		Criteria criteria = this.getSession().createCriteria( entityClass );

		if ( CollectionUtils.isNotEmpty( criterions ) ) {
			for ( Criterion criterion : criterions ) {
				criteria.add( criterion );
			}
		}
		return criteria;
	}

	/**
	 * 初始化对象. 使用load()方法得到的仅是Proxy代理对象, 在传到View层前需要进行初始化.
	 * 
	 * 如果传入entity, 则只初始化entity的直接属性, 但不会初始化延迟加载的关联集合和属性. 如需初始化关联属性, 需执行:
	 * Hibernate.initialize(user.getRoles()), 初始化User的直接属性和关联集合.
	 * Hibernate.initialize(user.getDescription()), 初始化User的直接属性和延迟加载的Description属性.
	 */
	public void initProxyObject( Object proxy ) {
		Hibernate.initialize( proxy );
	}

	/**
	 * Flush当前Session.
	 */
	public void flush() {
		this.getSession().flush();
	}

	/**
	 * 为Query添加distinct transformer. 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
	 */
	public Query distinct( Query query ) {
		query.setResultTransformer( CriteriaSpecification.DISTINCT_ROOT_ENTITY );
		return query;
	}

	/**
	 * 为Criteria添加distinct transformer. 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
	 */
	public Criteria distinct( Criteria criteria ) {
		criteria.setResultTransformer( CriteriaSpecification.DISTINCT_ROOT_ENTITY );
		return criteria;
	}

	/**
	 * 判断对象的属性值在数据库内是否唯一.
	 * 
	 * 在修改对象的情景下, 如果属性新修改的值(newValue)eq属性原来的值(orgValue)则不作比较.
	 */
	public boolean isPropertyUnique( final String propertyName, final Object newValue, final Object oldValue ) {
		if ( null == newValue || newValue.equals( oldValue ) ) {
			return true;
		}
		Object object = this.findUniqueBy( propertyName, newValue );
		return ( null == object );
	}

	/**
	 * 获取对象的主键名.
	 */
	public String getIdName() {
		ClassMetadata meta = this.getSessionFactory().getClassMetadata( entityClass );
		return meta.getIdentifierPropertyName();
	}

}