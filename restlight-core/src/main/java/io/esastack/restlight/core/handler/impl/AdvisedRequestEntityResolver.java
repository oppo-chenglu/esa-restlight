/*
 * Copyright 2021 OPPO ESA Stack Project
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
package io.esastack.restlight.core.handler.impl;

import esa.commons.Checks;
import io.esastack.restlight.core.DeployContext;
import io.esastack.restlight.core.config.RestlightOptions;
import io.esastack.restlight.core.method.HandlerMethod;
import io.esastack.restlight.core.method.Param;
import io.esastack.restlight.core.resolver.HandledValue;
import io.esastack.restlight.core.resolver.RequestEntity;
import io.esastack.restlight.core.resolver.RequestEntityImpl;
import io.esastack.restlight.core.resolver.RequestEntityResolver;
import io.esastack.restlight.core.resolver.RequestEntityResolverAdvice;
import io.esastack.restlight.core.resolver.RequestEntityResolverContextImpl;
import io.esastack.restlight.server.bootstrap.WebServerException;
import io.esastack.restlight.server.context.RequestContext;

import java.util.List;

class AdvisedRequestEntityResolver implements ResolverWrap {

    private final Param param;
    private final HandlerMethod handlerMethod;
    private final RequestEntityResolver[] resolvers;
    private final RequestEntityResolverAdvice[] advices;
    private final boolean absentAdvices;

    AdvisedRequestEntityResolver(HandlerMethod handlerMethod,
                                 Param param,
                                 List<RequestEntityResolver> resolvers,
                                 List<RequestEntityResolverAdvice> advices) {
        Checks.checkNotEmptyArg(resolvers, "resolver");
        this.handlerMethod = handlerMethod;
        this.param = param;
        this.resolvers = resolvers.toArray(new RequestEntityResolver[0]);
        this.absentAdvices = (advices == null || advices.isEmpty());
        this.advices = (advices == null ? null : advices.toArray(new RequestEntityResolverAdvice[0]));
    }

    @Override
    public Object resolve(DeployContext<? extends RestlightOptions> deployContext,
                          Param param, RequestContext context) throws Exception {
        RequestEntity entity = new RequestEntityImpl(handlerMethod, param, context);
        if (absentAdvices) {
            HandledValue<Object> handled;
            for (RequestEntityResolver resolver : resolvers) {
                handled = resolver.readFrom(param, entity, context);
                if (handled.isSuccess()) {
                    return handled.value();
                }
            }
            throw WebServerException.notSupported("There is no suitable resolver to resolve param: " + param
                    + ", content-type: " + context.request().contentType());
        }
        return new RequestEntityResolverContextImpl(this.param, context, entity, resolvers, advices).proceed();
    }
}
