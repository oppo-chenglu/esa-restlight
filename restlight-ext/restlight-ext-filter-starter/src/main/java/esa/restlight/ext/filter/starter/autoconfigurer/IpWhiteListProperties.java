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
package esa.restlight.ext.filter.starter.autoconfigurer;

import esa.restlight.ext.filter.ipwhitelist.IpWhiteListOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static esa.restlight.ext.filter.starter.autoconfigurer.RestlightExtFilterAutoConfiguration.EXT;

@ConfigurationProperties(IpWhiteListProperties.PREFIX)
public class IpWhiteListProperties extends IpWhiteListOptions {

    static final String PREFIX = EXT + "whitelist";

    private static final long serialVersionUID = 1312780714181225812L;
}
