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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class ConditionalFileFilterAbstractTestCase extends IOFileFilterAbstractTestCase {

    private static final String TEST_FILE_NAME_PREFIX = "TestFile";
    private static final String TEST_FILE_TYPE = ".tst";

    protected TesterTrueFileFilter[] trueFilters;
    protected TesterFalseFileFilter[] falseFilters;

    private File file;
    private File workingPath;


    private List<List<IOFileFilter>> testFilters;
    private List<boolean[]> testTrueResults;
    private List<boolean[]> testFalseResults;
    private List<Boolean> testFileResults;
    private List<Boolean> testFilenameResults;

    public ConditionalFileFilterAbstractTestCase(final String name) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        this.workingPath = determineWorkingDirectoryPath(this.getWorkingPathNamePropertyKey(), this.getDefaultWorkingPath());
        this.file = new File(this.workingPath, TEST_FILE_NAME_PREFIX + 1 + TEST_FILE_TYPE);
        this.trueFilters = new TesterTrueFileFilter[4];
        this.falseFilters = new TesterFalseFileFilter[4];
        this.trueFilters[1] = new TesterTrueFileFilter();
        this.trueFilters[2] = new TesterTrueFileFilter();
        this.trueFilters[3] = new TesterTrueFileFilter();
        this.falseFilters[1] = new TesterFalseFileFilter();
        this.falseFilters[2] = new TesterFalseFileFilter();
        this.falseFilters[3] = new TesterFalseFileFilter();
    }

    public void testAdd() {
        final List<TesterTrueFileFilter> filters = new ArrayList<TesterTrueFileFilter>();
        final ConditionalFileFilter fileFilter = this.getConditionalFileFilter();
        filters.add(new TesterTrueFileFilter());
        filters.add(new TesterTrueFileFilter());
        filters.add(new TesterTrueFileFilter());
        filters.add(new TesterTrueFileFilter());
        for (int i = 0; i < filters.size(); i++) {
            assertEquals("file filters count: ", i, fileFilter.getFileFilters().size());
            fileFilter.addFileFilter(filters.get(i));
            assertEquals("file filters count: ", i + 1, fileFilter.getFileFilters().size());
        }
        for (final IOFileFilter filter : fileFilter.getFileFilters()) {
            assertTrue("found file filter", filters.contains(filter));
        }
        assertEquals("file filters count", filters.size(), fileFilter.getFileFilters().size());
    }

    public void testRemove() {
        final List<TesterTrueFileFilter> filters = new ArrayList<TesterTrueFileFilter>();
        final ConditionalFileFilter fileFilter = this.getConditionalFileFilter();
        filters.add(new TesterTrueFileFilter());
        filters.add(new TesterTrueFileFilter());
        filters.add(new TesterTrueFileFilter());
        filters.add(new TesterTrueFileFilter());
        for (TesterTrueFileFilter filter : filters) {
            fileFilter.removeFileFilter(filter);
            assertTrue("file filter removed", !fileFilter.getFileFilters().contains(filter));
        }
        assertEquals("file filters count", 0, fileFilter.getFileFilters().size());
    }

    public void testNoFilters() throws Exception {
        final ConditionalFileFilter fileFilter = this.getConditionalFileFilter();
        final File file = new File(this.workingPath, TEST_FILE_NAME_PREFIX + 1 + TEST_FILE_TYPE);
        assertFileFiltering(1, (IOFileFilter) fileFilter, file, false);
        assertFilenameFiltering(1, (IOFileFilter) fileFilter, file, false);
    }

    public void testFilterBuiltUsingConstructor() throws Exception {
        final List<List<IOFileFilter>> testFilters = this.getTestFilters();
        final List<boolean[]> testTrueResults = this.getTrueResults();
        final List<boolean[]> testFalseResults = this.getFalseResults();
        final List<Boolean> testFileResults = this.getFileResults();
        final List<Boolean> testFilenameResults = this.getFilenameResults();

        for (int i = 1; i < testFilters.size(); i++) {
            final List<IOFileFilter> filters = testFilters.get(i);
            final boolean[] trueResults = testTrueResults.get(i);
            final boolean[] falseResults = testFalseResults.get(i);
            final boolean fileResults = testFileResults.get(i);
            final boolean filenameResults = testFilenameResults.get(i);

            // Test conditional AND filter created by passing filters to the constructor
            final IOFileFilter filter = this.buildFilterUsingConstructor(filters);

            // Test as a file filter
            resetTrueFilters(this.trueFilters);
            resetFalseFilters(this.falseFilters);
            assertFileFiltering(i, filter, this.file, fileResults);
            assertTrueFiltersInvoked(i, trueFilters, trueResults);
            assertFalseFiltersInvoked(i, falseFilters, falseResults);

            // Test as a filename filter
            resetTrueFilters(this.trueFilters);
            resetFalseFilters(this.falseFilters);
            assertFilenameFiltering(i, filter, this.file, filenameResults);
            assertTrueFiltersInvoked(i, trueFilters, trueResults);
            assertFalseFiltersInvoked(i, falseFilters, falseResults);
        }
    }

    public void testFilterBuiltUsingAdd() throws Exception {
        final List<List<IOFileFilter>> testFilters = this.getTestFilters();
        final List<boolean[]> testTrueResults = this.getTrueResults();
        final List<boolean[]> testFalseResults = this.getFalseResults();
        final List<Boolean> testFileResults = this.getFileResults();
        final List<Boolean> testFilenameResults = this.getFilenameResults();

        for (int i = 1; i < testFilters.size(); i++) {
            final List<IOFileFilter> filters = testFilters.get(i);
            final boolean[] trueResults = testTrueResults.get(i);
            final boolean[] falseResults = testFalseResults.get(i);
            final boolean fileResults = testFileResults.get(i);
            final boolean filenameResults = testFilenameResults.get(i);

            // Test conditional AND filter created by passing filters to the constructor
            final IOFileFilter filter = this.buildFilterUsingAdd(filters);

            // Test as a file filter
            resetTrueFilters(this.trueFilters);
            resetFalseFilters(this.falseFilters);
            assertFileFiltering(i, filter, this.file, fileResults);
            assertTrueFiltersInvoked(i, trueFilters, trueResults);
            assertFalseFiltersInvoked(i, falseFilters, falseResults);

            // Test as a filename filter
            resetTrueFilters(this.trueFilters);
            resetFalseFilters(this.falseFilters);
            assertFilenameFiltering(i, filter, this.file, filenameResults);
            assertTrueFiltersInvoked(i, trueFilters, trueResults);
            assertFalseFiltersInvoked(i, falseFilters, falseResults);
        }
    }

    public void setUpTestFilters() {
        // filters
        //tests
        this.testFilters = new ArrayList<List<IOFileFilter>>();
        this.testTrueResults = new ArrayList<boolean[]>();
        this.testFalseResults = new ArrayList<boolean[]>();
        this.testFileResults = new ArrayList<Boolean>();
        this.testFilenameResults = new ArrayList<Boolean>();

        // test 0 - add empty elements
        {
            testFilters.add(0, null);
            testTrueResults.add(0, null);
            testFalseResults.add(0, null);
            testFileResults.add(0, null);
            testFilenameResults.add(0, null);
        }

        // test 1 - Test conditional or with all filters returning true
        {
            // test 1 filters
            final List<IOFileFilter> filters = new ArrayList<IOFileFilter>();
            filters.add(trueFilters[1]);
            filters.add(trueFilters[2]);
            filters.add(trueFilters[3]);
            // test 1 true results
            final boolean[] trueResults = new boolean[] {true, false, false};
            // test 1 false results
            final boolean[] falseResults = new boolean[] {false, false, false};

            testFilters.add(1, filters);
            testTrueResults.add(1, trueResults);
            testFalseResults.add(1, falseResults);
            testFileResults.add(1, Boolean.TRUE);
            testFilenameResults.add(1, Boolean.TRUE);
        }

        // test 2 - Test conditional or with first filter returning false
        {
            // test 2 filters
            final List<IOFileFilter> filters = new ArrayList<IOFileFilter>();
            filters.add(falseFilters[1]);
            filters.add(trueFilters[1]);
            filters.add(trueFilters[2]);
            filters.add(trueFilters[3]);
            filters.add(falseFilters[2]);
            filters.add(falseFilters[3]);
            // test 2 true results
            final boolean[] trueResults = new boolean[] {true, false, false};
            // test 2 false results
            final boolean[] falseResults = new boolean[] {true, false, false};

            testFilters.add(2, filters);
            testTrueResults.add(2, trueResults);
            testFalseResults.add(2, falseResults);
            testFileResults.add(2, Boolean.TRUE);
            testFilenameResults.add(2, Boolean.TRUE);
        }

        // test 3 - Test conditional or with second filter returning false
        {
            // test 3 filters
            final List<IOFileFilter> filters = new ArrayList<IOFileFilter>();
            filters.add(trueFilters[1]);
            filters.add(falseFilters[1]);
            filters.add(trueFilters[2]);
            filters.add(trueFilters[3]);
            filters.add(falseFilters[2]);
            filters.add(falseFilters[3]);
            // test 3 true results
            final boolean[] trueResults = new boolean[] {true, false, false};
            // test 3 false results
            final boolean[] falseResults = new boolean[] {false, false, false};

            testFilters.add(3, filters);
            testTrueResults.add(3, trueResults);
            testFalseResults.add(3, falseResults);
            testFileResults.add(3, Boolean.TRUE);
            testFilenameResults.add(3, Boolean.TRUE);
        }

        // test 4 - Test conditional or with third filter returning false
        {
            // test 4 filters
            final List<IOFileFilter> filters = new ArrayList<IOFileFilter>();
            filters.add(trueFilters[1]);
            filters.add(trueFilters[2]);
            filters.add(falseFilters[1]);
            filters.add(trueFilters[3]);
            filters.add(falseFilters[2]);
            filters.add(falseFilters[3]);
            // test 4 true results
            final boolean[] trueResults = new boolean[] {true, false, false};
            // test 4 false results
            final boolean[] falseResults = new boolean[] {false, false, false};

            testFilters.add(4, filters);
            testTrueResults.add(4, trueResults);
            testFalseResults.add(4, falseResults);
            testFileResults.add(4, Boolean.TRUE);
            testFilenameResults.add(4, Boolean.TRUE);
        }

        // test 5 - Test conditional or with first and third filters returning false
        {
            // test 5 filters
            final List<IOFileFilter> filters = new ArrayList<IOFileFilter>();
            filters.add(falseFilters[1]);
            filters.add(trueFilters[1]);
            filters.add(falseFilters[2]);
            filters.add(falseFilters[3]);
            filters.add(trueFilters[2]);
            filters.add(trueFilters[3]);
            // test 5 true results
            final boolean[] trueResults = new boolean[] {true, false, false};
            // test 5 false results
            final boolean[] falseResults = new boolean[] {true, false, false};

            testFilters.add(5, filters);
            testTrueResults.add(5, trueResults);
            testFalseResults.add(5, falseResults);
            testFileResults.add(5, Boolean.TRUE);
            testFilenameResults.add(5, Boolean.TRUE);
        }

        // test 6 - Test conditional or with second and third filters returning false
        {
            final List<IOFileFilter> filters = new ArrayList<IOFileFilter>();
            filters.add(trueFilters[1]);
            filters.add(falseFilters[1]);
            filters.add(falseFilters[2]);
            filters.add(trueFilters[2]);
            filters.add(trueFilters[3]);
            filters.add(falseFilters[3]);
            // test 6 true results
            final boolean[] trueResults = new boolean[] {true, false, false};
            // test 6 false results
            final boolean[] falseResults = new boolean[] {false, false, false};

            testFilters.add(6, filters);
            testTrueResults.add(6, trueResults);
            testFalseResults.add(6, falseResults);
            testFileResults.add(6, Boolean.TRUE);
            testFilenameResults.add(6, Boolean.TRUE);
        }

        // test 7 - Test conditional or with first and second filters returning false
        {
            final List<IOFileFilter> filters = new ArrayList<IOFileFilter>();
            filters.add(falseFilters[1]);
            filters.add(falseFilters[2]);
            filters.add(trueFilters[1]);
            filters.add(falseFilters[3]);
            filters.add(trueFilters[2]);
            filters.add(trueFilters[3]);
            // test 7 true results
            final boolean[] trueResults = new boolean[] {true, false, false};
            // test 7 false results
            final boolean[] falseResults = new boolean[] {true, true, false};

            testFilters.add(7, filters);
            testTrueResults.add(7, trueResults);
            testFalseResults.add(7, falseResults);
            testFileResults.add(7, Boolean.TRUE);
            testFilenameResults.add(7, Boolean.TRUE);
        }

        // test 8 - Test conditional or with fourth filter returning false
        {
            final List<IOFileFilter> filters = new ArrayList<IOFileFilter>();
            filters.add(trueFilters[1]);
            filters.add(trueFilters[2]);
            filters.add(trueFilters[3]);
            filters.add(falseFilters[1]);
            // test 8 true results
            final boolean[] trueResults = new boolean[] {true, false, false};
            // test 8 false results
            final boolean[] falseResults = new boolean[] {false, false, false};

            testFilters.add(8, filters);
            testTrueResults.add(8, trueResults);
            testFalseResults.add(8, falseResults);
            testFileResults.add(8, Boolean.TRUE);
            testFilenameResults.add(8, Boolean.TRUE);
        }

        // test 9 - Test conditional or with all filters returning false
        {
            final List<IOFileFilter> filters = new ArrayList<IOFileFilter>();
            filters.add(falseFilters[1]);
            filters.add(falseFilters[2]);
            filters.add(falseFilters[3]);
            // test 9 true results
            final boolean[] trueResults = new boolean[] {false, false, false};
            // test 9 false results
            final boolean[] falseResults = new boolean[] {true, true, true};

            testFilters.add(9, filters);
            testTrueResults.add(9, trueResults);
            testFalseResults.add(9, falseResults);
            testFileResults.add(9, Boolean.FALSE);
            testFilenameResults.add(9, Boolean.FALSE);
        }
    }

    protected abstract ConditionalFileFilter getConditionalFileFilter();

    protected abstract IOFileFilter buildFilterUsingAdd(List<IOFileFilter> filters);

    protected abstract IOFileFilter buildFilterUsingConstructor(List<IOFileFilter> filters);

    protected abstract List<List<IOFileFilter>> getTestFilters();

    protected abstract List<boolean[]> getTrueResults();

    protected abstract List<boolean[]> getFalseResults();

    protected abstract List<Boolean> getFileResults();

    protected abstract List<Boolean> getFilenameResults();

    protected abstract String getWorkingPathNamePropertyKey();

    protected abstract String getDefaultWorkingPath();
}
