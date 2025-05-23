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

import org.finos.legend.engine.persistence.components.ingestmode.audit.Auditing;
import org.finos.legend.engine.persistence.components.ingestmode.merge.DeleteIndicatorMergeStrategyAbstract;
import org.finos.legend.engine.persistence.components.ingestmode.merge.MergeStrategy;
import org.finos.legend.engine.persistence.components.ingestmode.merge.MergeStrategyVisitor;
import org.finos.legend.engine.persistence.components.ingestmode.merge.NoDeletesMergeStrategy;
import org.finos.legend.engine.persistence.components.ingestmode.merge.NoDeletesMergeStrategyAbstract;
import org.finos.legend.engine.persistence.components.ingestmode.merge.TerminateLatestActiveMergeStrategyAbstract;
import org.finos.legend.engine.persistence.components.ingestmode.versioning.AllVersionsStrategyAbstract;
import org.finos.legend.engine.persistence.components.ingestmode.versioning.MaxVersionStrategyAbstract;
import org.finos.legend.engine.persistence.components.ingestmode.versioning.NoVersioningStrategyAbstract;
import org.finos.legend.engine.persistence.components.ingestmode.versioning.VersioningStrategyVisitor;
import org.finos.legend.engine.persistence.components.ingestmode.versioning.VersioningVisitors;
import org.immutables.value.Value;

import java.util.Optional;

import static org.immutables.value.Value.Default;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@Style(
    typeAbstract = "*Abstract",
    typeImmutable = "*",
    jdkOnly = true,
    optionalAcceptNullable = true,
    strictBuilder = true
)
public interface NontemporalDeltaAbstract extends IngestMode
{
    @Value.Default
    default String batchIdField()
    {
        return "batch_id";
    }

    Optional<String> digestField();

    Auditing auditing();

    @Default
    default MergeStrategy mergeStrategy()
    {
        return NoDeletesMergeStrategy.builder().build();
    }

    @Override
    default <T> T accept(IngestModeVisitor<T> visitor)
    {
        return visitor.visitNontemporalDelta(this);
    }

    @Value.Check
    default void validate()
    {
        mergeStrategy().accept(new MergeStrategyVisitor<Object>()
        {
            @Override
            public Object visitNoDeletesMergeStrategy(NoDeletesMergeStrategyAbstract noDeletesMergeStrategy)
            {
                return null;
            }

            @Override
            public Object visitDeleteIndicatorMergeStrategy(DeleteIndicatorMergeStrategyAbstract deleteIndicatorMergeStrategy)
            {
                return null;
            }

            @Override
            public Object visitTerminateLatestActiveMergeStrategy(TerminateLatestActiveMergeStrategyAbstract terminateLatestActiveMergeStrategy)
            {
                throw new IllegalStateException("Cannot build NontemporalDelta, TerminateLatestActiveMergeStrategy not supported");
            }
        });

        versioningStrategy().accept(new VersioningStrategyVisitor<Void>()
        {

            @Override
            public Void visitNoVersioningStrategy(NoVersioningStrategyAbstract noVersioningStrategy)
            {
                if (!digestField().isPresent())
                {
                    throw new IllegalStateException("Cannot build NontemporalDelta, digestField is mandatory for NoVersioningStrategy");
                }
                return null;
            }

            @Override
            public Void visitMaxVersionStrategy(MaxVersionStrategyAbstract maxVersionStrategy)
            {
                if (!maxVersionStrategy.mergeDataVersionResolver().isPresent())
                {
                    throw new IllegalStateException("Cannot build NontemporalDelta, VersioningResolver is mandatory for MaxVersionStrategy");
                }
                maxVersionStrategy.mergeDataVersionResolver().get().accept(new VersioningVisitors.ValidateDigest(digestField()));
                return null;
            }

            @Override
            public Void visitAllVersionsStrategy(AllVersionsStrategyAbstract allVersionsStrategyAbstract)
            {
                if (!allVersionsStrategyAbstract.mergeDataVersionResolver().isPresent())
                {
                    throw new IllegalStateException("Cannot build NontemporalDelta, VersioningResolver is mandatory for AllVersionsStrategy");
                }
                allVersionsStrategyAbstract.mergeDataVersionResolver().get().accept(new VersioningVisitors.ValidateDigest(digestField()));
                return null;
            }
        });
    }
}
