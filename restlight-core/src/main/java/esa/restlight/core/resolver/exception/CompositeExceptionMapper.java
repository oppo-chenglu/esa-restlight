/*
 * Copyright 2020 OPPO ESA Stack Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package esa.restlight.core.resolver.exception;

import esa.commons.Checks;
import esa.restlight.core.handler.Handler;
import esa.restlight.core.resolver.ExceptionResolver;
import esa.restlight.core.util.OrderedComparator;

import java.util.List;

/**
 * Shows the multiple {@link ExceptionMapper}s as a single {@link ExceptionMapper}.
 */
public class CompositeExceptionMapper implements ExceptionMapper {

    private final List<ExceptionMapper> mappers;

    public CompositeExceptionMapper(List<ExceptionMapper> mappers) {
        Checks.checkNotNull(mappers, "mappers");
        OrderedComparator.sort(mappers);
        this.mappers = mappers;
    }

    public static ExceptionMapper wrapIfNecessary(List<ExceptionMapper> mappers) {
        Checks.checkNotEmptyArg(mappers, "mappers");
        if (mappers.size() == 1) {
            return mappers.get(0);
        } else {
            return new CompositeExceptionMapper(mappers);
        }
    }

    @Override
    public boolean isApplicable(Handler handler) {
        return mappers.stream().anyMatch(exMapper -> exMapper.isApplicable(handler));
    }

    @Override
    public ExceptionResolver<Throwable> mapTo(Class<? extends Throwable> ex) {
        ExceptionResolver<Throwable> resolver = null;
        for (ExceptionMapper exceptionMapper : mappers) {
            resolver = exceptionMapper.mapTo(ex);
            if (resolver != null) {
                break;
            }
        }
        return resolver;
    }
}
