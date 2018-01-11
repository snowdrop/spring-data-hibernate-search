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

import me.snowdrop.data.core.query.QueryHelper;
import org.springframework.data.domain.Sort;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class GcdQueryHelper implements QueryHelper<QueryHandle, Void> {

    private final QueryHandle handle;

    public GcdQueryHelper(QueryHandle handle) {
        this.handle = handle;
    }

    @Override
    public Void convert(Sort sort) {
        handle.sort(sort);
        return null;
    }

    public QueryHandle matchAll() {
        return handle;
    }
}
