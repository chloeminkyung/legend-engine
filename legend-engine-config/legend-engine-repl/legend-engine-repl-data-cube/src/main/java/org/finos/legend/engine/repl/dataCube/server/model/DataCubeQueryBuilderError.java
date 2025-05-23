// Copyright 2024 Goldman Sachs
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

package org.finos.legend.engine.repl.dataCube.server.model;

import org.finos.legend.engine.protocol.pure.m3.SourceInformation;
import org.finos.legend.engine.shared.core.operational.errorManagement.EngineException;

public class DataCubeQueryBuilderError
{
    public String type;
    public String message;
    public SourceInformation sourceInformation;

    public DataCubeQueryBuilderError(EngineException engineException)
    {
        this.type = engineException.getErrorType() != null ? engineException.getErrorType().name() : null;
        this.message = engineException.getMessage();
        this.sourceInformation = engineException.getSourceInformation();
    }
}
