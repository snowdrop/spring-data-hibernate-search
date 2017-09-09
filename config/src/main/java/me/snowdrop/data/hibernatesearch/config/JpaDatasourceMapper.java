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

package me.snowdrop.data.hibernatesearch.config;

import java.util.List;

import javax.persistence.EntityManager;

import me.snowdrop.data.hibernatesearch.spi.DatasourceMapper;
import me.snowdrop.data.hibernatesearch.spi.QueryAdapter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.spi.SearchIntegrator;
import org.springframework.util.Assert;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class JpaDatasourceMapper implements DatasourceMapper {

  private EntityManager em;

  public JpaDatasourceMapper(EntityManager em) {
    Assert.notNull(em, "Null EntityManager!");
    this.em = em;
  }

  @Override
  public <T> QueryAdapter createQueryAdapter(Class<T> entityClass) {
    return new OrmQueryAdapter<>(entityClass);
  }

  private class OrmQueryAdapter<T> implements QueryAdapter<T> {
    private final Class<T> entityClass;
    private FullTextQuery fullTextQuery;

    public OrmQueryAdapter(Class<T> entityClass) {
      this.entityClass = entityClass;
    }

    @Override
    public void applyLuceneQuery(SearchIntegrator searchIntegrator, Query query) {
      FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
      fullTextQuery = fullTextEntityManager.createFullTextQuery(query, entityClass);
    }

    @Override
    public long size() {
      return fullTextQuery.getResultSize();
    }

    @Override
    public List<T> list() {
      //noinspection unchecked
      return fullTextQuery.getResultList();
    }

    @Override
    public void setSort(Sort sort) {
      fullTextQuery.setSort(sort);
    }

    @Override
    public void setFirstResult(int firstResult) {
      fullTextQuery.setFirstResult(firstResult);
    }

    @Override
    public void setMaxResults(int maxResults) {
      fullTextQuery.setMaxResults(maxResults);
    }
  }
}