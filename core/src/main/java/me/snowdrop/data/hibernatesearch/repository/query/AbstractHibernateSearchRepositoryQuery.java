/*
 * Copyright 2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.snowdrop.data.hibernatesearch.repository.query;

import java.util.Optional;

import me.snowdrop.data.hibernatesearch.core.HibernateSearchOperations;
import me.snowdrop.data.hibernatesearch.core.query.BaseQuery;
import me.snowdrop.data.hibernatesearch.spi.Query;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractHibernateSearchRepositoryQuery implements RepositoryQuery {
  private final QueryMethod queryMethod;
  protected final HibernateSearchOperations hibernateSearchOperations;

  public AbstractHibernateSearchRepositoryQuery(QueryMethod queryMethod, HibernateSearchOperations hibernateSearchOperations) {
    this.queryMethod = queryMethod;
    this.hibernateSearchOperations = hibernateSearchOperations;
  }

  @Override
  public QueryMethod getQueryMethod() {
    return queryMethod;
  }

  protected abstract boolean isCountProjection(Query<?> query);

  protected abstract BaseQuery<?> createQuery(ParametersParameterAccessor accessor);

  @Override
  public Object execute(Object[] parameters) {
    ParametersParameterAccessor accessor = new ParametersParameterAccessor(getQueryMethod().getParameters(), parameters);

    BaseQuery<?> query = createQuery(accessor);
    if (query.getSort() == null) {
      query.setSort(accessor.getSort());
    }
    if (query.getPageable() == null) {
      query.setPageable(accessor.getPageable());
    }

    if (getQueryMethod().isSliceQuery()) {
      return hibernateSearchOperations.findSlice(query);
    } else if (getQueryMethod().isPageQuery()) {
      return hibernateSearchOperations.findPageable(query);
    } else if (getQueryMethod().isStreamQuery()) {
      return hibernateSearchOperations.stream(query);
    } else if (getQueryMethod().isCollectionQuery()) {
      return hibernateSearchOperations.findAll(query);
    } else if (isCountProjection(query)) {
      return hibernateSearchOperations.count(query);
    } else {
      Optional<?> optional = hibernateSearchOperations.findSingle(query);
      if (Optional.class.equals(getQueryMethod().getReturnedObjectType())) {
        return optional;
      } else {
        return optional.get();
      }
    }
  }
}
