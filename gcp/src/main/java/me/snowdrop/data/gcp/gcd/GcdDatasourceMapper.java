/*
 * Copyright 2018 Red Hat, Inc, and individual contributors.
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

package me.snowdrop.data.gcp.gcd;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import me.snowdrop.data.core.spi.CrudAdapter;
import me.snowdrop.data.core.spi.DatasourceMapper;
import me.snowdrop.data.core.spi.QueryAdapter;
import me.snowdrop.data.gcp.EntityToModelMapper;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.util.Assert;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class GcdDatasourceMapper implements DatasourceMapper {

    private final EntityToModelMapper<Entity, FullEntity<IncompleteKey>> entityToModelMapper;

    public GcdDatasourceMapper() {
        this(new GcdEntityToModelMapper());
    }

    public GcdDatasourceMapper(EntityToModelMapper<Entity, FullEntity<IncompleteKey>> entityToModelMapper) {
        Assert.notNull(entityToModelMapper, "Null EntityToModelMapper!");
        this.entityToModelMapper = entityToModelMapper;
    }

    public <T> QueryAdapter<T> createQueryAdapter(Class<T> entityClass) {
        return new GcdQueryAdapter<>(entityClass, entityToModelMapper);
    }

    public <T, ID> CrudAdapter<T, ID> createCrudAdapter(EntityInformation<T, ID> ei) {
        return new GcdCrudAdapter<>(ei, entityToModelMapper);
    }
}
