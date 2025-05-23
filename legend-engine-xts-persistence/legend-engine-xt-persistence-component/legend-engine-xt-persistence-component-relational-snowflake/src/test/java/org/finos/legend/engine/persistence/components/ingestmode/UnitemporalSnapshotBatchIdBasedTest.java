// Copyright 2022 Goldman Sachs
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.finos.legend.engine.persistence.components.ingestmode;

import org.finos.legend.engine.persistence.components.SnowflakeTestArtifacts;
import org.finos.legend.engine.persistence.components.relational.RelationalSink;
import org.finos.legend.engine.persistence.components.relational.snowflake.SnowflakeSink;

public class UnitemporalSnapshotBatchIdBasedTest extends org.finos.legend.engine.persistence.components.ingestmode.unitemporal.UnitemporalSnapshotBatchIdBasedTest
{
    @Override
    public RelationalSink getRelationalSink()
    {
        return SnowflakeSink.get();
    }

    @Override
    protected String getExpectedMetadataTableIngestQuery()
    {
        return SnowflakeTestArtifacts.expectedMetadataTableIngestQuery;
    }

    @Override
    protected String getExpectedMetadataTableIngestQueryWithUpperCase()
    {
        return SnowflakeTestArtifacts.expectedMetadataTableIngestQueryWithUpperCase;
    }

    protected String getExpectedMetadataTableCreateQuery()
    {
        return SnowflakeTestArtifacts.expectedMetadataTableCreateQuery;
    }

    protected String getExpectedMetadataTableCreateQueryWithUpperCase()
    {
        return SnowflakeTestArtifacts.expectedMetadataTableCreateQueryWithUpperCase;
    }

    protected String getExpectedMaxDataErrorQueryWithDistinctAmount()
    {
        return SnowflakeTestArtifacts.dataErrorCheckSqlWithAmount;
    }

    protected String getExpectedDataErrorQueryWithDistinctAmount()
    {
        return SnowflakeTestArtifacts.dataErrorSqlWithAmount;
    }
}
