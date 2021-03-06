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

package me.snowdrop.data.core.repository.query;

import me.snowdrop.data.core.ops.SnowdropOperations;
import me.snowdrop.data.core.mapping.SnowdropPersistentProperty;
import me.snowdrop.data.core.query.BaseQuery;
import me.snowdrop.data.core.query.CriteriaQuery;
import me.snowdrop.data.core.repository.query.parser.SnowdropQueryCreator;
import me.snowdrop.data.core.spi.Query;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.parser.PartTree;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SnowdropPartQuery extends AbstractSnowdropRepositoryQuery {
    private final PartTree tree;
    private final MappingContext<?, SnowdropPersistentProperty> mappingContext;

    public SnowdropPartQuery(SnowdropQueryMethod queryMethod, SnowdropOperations snowdropOperations) {
        super(queryMethod, snowdropOperations);
        this.tree = new PartTree(queryMethod.getName(), queryMethod.getEntityInformation().getJavaType());
        this.mappingContext = snowdropOperations.getMappingContext();
    }

    protected boolean isModify(Query<?> query) {
        return tree.isDelete();
    }

    protected boolean isExistsProjection(Query<?> query) {
        return tree.isExistsProjection();
    }

    protected boolean isCountProjection(Query<?> query) {
        return tree.isCountProjection();
    }

    protected BaseQuery<?> createQuery(ParametersParameterAccessor accessor) {
        Class<?> entityClass = getQueryMethod().getEntityInformation().getJavaType();
        CriteriaQuery<?> query = new SnowdropQueryCreator(entityClass, tree, accessor, mappingContext).createQuery();

        query.setTargetFields(TargetFieldsUtils.getTargetFieldsMap(getQueryMethod()));

        query.setMaxResults(tree.getMaxResults());
        query.setDistinct(tree.isDistinct());

        return query;
    }
}
