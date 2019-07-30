/**
 * Copyright 2013-2019 the original author or authors from the Jeddict project (https://jeddict.github.io/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.jeddict.orm.generator.compiler;

import static io.github.jeddict.jcode.JPAConstants.ID;
import static io.github.jeddict.jcode.JPAConstants.ID_FQN;
import static io.github.jeddict.jcode.JPAConstants.ID_NOSQL_FQN;
import java.util.Collection;
import static java.util.Collections.singleton;



public class IdSnippet extends ORMSnippet {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSnippet() throws InvalidDataException {
        if (isNoSQL()) {
            return annotate(
                    ID,
                    attribute(name)
            );
        } else {
            return annotate(ID);
        }
        
    }

    @Override
    public Collection<String> getImportSnippets() throws InvalidDataException {
        if (isNoSQL()) {
            return singleton(ID_NOSQL_FQN);
        } else {
            return singleton(ID_FQN);
        }
    }

}