/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idfor.kat.tools.UIBackend.service.utils;

import org.apache.cxf.jaxrs.security.JAASAuthenticationFilter;
import org.apache.cxf.message.Message;
import java.net.URI;
import java.util.List;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.jaxrs.impl.HttpHeadersImpl;
import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;

public class JaasExtender extends JAASAuthenticationFilter {

    private URI redirectURI;
    private String realmName;
    private boolean ignoreBasePath = true;

    @Override
    protected Response handleAuthenticationException(SecurityException ex, Message m) {
        HttpHeaders headers = new HttpHeadersImpl(m);
        ResponseBuilder builder;
        if (this.redirectURI != null && this.isRedirectPossible(headers)) {
            builder = null;
            URI finalRedirectURI;
            if (!this.redirectURI.isAbsolute()) {
                String endpointAddress = HttpUtils.getEndpointAddress(m);
                Object basePathProperty = m.get(Message.BASE_PATH);
                if (this.ignoreBasePath && basePathProperty != null && !"/".equals(basePathProperty)) {
                    int index = endpointAddress.lastIndexOf(basePathProperty.toString());
                    if (index != -1) {
                        endpointAddress = endpointAddress.substring(0, index);
                    }
                }

                finalRedirectURI = UriBuilder.fromUri(endpointAddress).path(this.redirectURI.toString()).build(new Object[0]);
            } else {
                finalRedirectURI = this.redirectURI;
            }

            return Response.status(this.getRedirectStatus()).header("Location", finalRedirectURI).build();
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
            StringBuilder sb = new StringBuilder();
            List<String> authHeader = headers.getRequestHeader("Authorization");
            if (authHeader != null && authHeader.size() > 0) {
                String[] authValues = StringUtils.split((String)authHeader.get(0), " ");
                if (authValues.length > 0) {
                    sb.append(authValues[0]);
                }
            } else {
                sb.append("Basic");
            }

            if (this.realmName != null) {
                sb.append(" realm=\"").append(this.realmName).append('"');
            }
            return builder.build();
        }
    }
}
