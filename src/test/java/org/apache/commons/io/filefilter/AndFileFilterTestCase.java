/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.filefilter;

import java.util.ArrayList;
import java.util.List;

public abstract class AndFileFilterTestCase extends ConditionalFileFilterAbstractTestCase {

  private static final String DEFAULT_WORKING_PATH = "./AndFileFilterTestCase/";
  private static final String WORKING_PATH_NAME_PROPERTY_KEY = AndFileFilterTestCase.class.getName() + ".workingDirectory";

  public AndFileFilterTestCase(final String name) {
    super(name);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.setUpTestFilters();
  }

  @Override
  protected IOFileFilter buildFilterUsingAdd(final List<IOFileFilter> filters) {
    final AndFileFilter filter = new AndFileFilter();
    for (IOFileFilter filter1 : filters) {
      filter.addFileFilter(filter1);
    }
    return filter;
  }

  @Override
  protected IOFileFilter buildFilterUsingConstructor(final List<IOFileFilter> filters) {
    return new AndFileFilter(filters);
  }

  @Override
  protected ConditionalFileFilter getConditionalFileFilter() {
    return new AndFileFilter();
  }

  @Override
  protected String getDefaultWorkingPath() {
    return DEFAULT_WORKING_PATH;
  }


  @Override
  protected String getWorkingPathNamePropertyKey() {
    return WORKING_PATH_NAME_PROPERTY_KEY;
  }


}
