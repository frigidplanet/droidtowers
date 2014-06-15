/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apach3.http.impl.client.cache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apach3.http.Header;
import org.apach3.http.HeaderElement;
import org.apach3.http.HttpEntity;
import org.apach3.http.HttpEntityEnclosingRequest;
import org.apach3.http.HttpRequest;
import org.apach3.http.HttpResponse;
import org.apach3.http.HttpStatus;
import org.apach3.http.HttpVersion;
import org.apach3.http.ProtocolVersion;
import org.apach3.http.annotation.Immutable;
import org.apach3.http.client.ClientProtocolException;
import org.apach3.http.client.cache.HeaderConstants;
import org.apach3.http.impl.client.RequestWrapper;
import org.apach3.http.impl.cookie.DateParseException;
import org.apach3.http.impl.cookie.DateUtils;
import org.apach3.http.message.BasicHeader;
import org.apach3.http.protocol.HTTP;
import org.apach3.http.util.EntityUtils;

/**
 * @since 4.1
 */
@Immutable
class ResponseProtocolCompliance {

    private static final String UNEXPECTED_100_CONTINUE = "The incoming request did not contain a "
                    + "100-continue header, but the response was a Status 100, continue.";
    private static final String UNEXPECTED_PARTIAL_CONTENT = "partial content was returned for a request that did not ask for it";

    /**
     * When we get a response from a down stream server (Origin Server)
     * we attempt to see if it is HTTP 1.1 Compliant and if not, attempt to
     * make it so.
     *
     * @param request The {@link HttpRequest} that generated an origin hit and response
     * @param response The {@link HttpResponse} from the origin server
     * @throws IOException Bad things happened
     */
    public void ensureProtocolCompliance(HttpRequest request, HttpResponse response)
            throws IOException {
        if (backendResponseMustNotHaveBody(request, response)) {
            consumeBody(response);
            response.setEntity(null);
        }

        requestDidNotExpect100ContinueButResponseIsOne(request, response);

        transferEncodingIsNotReturnedTo1_0Client(request, response);

        ensurePartialContentIsNotSentToAClientThatDidNotRequestIt(request, response);

        ensure200ForOPTIONSRequestWithNoBodyHasContentLengthZero(request, response);

        ensure206ContainsDateHeader(response);

        ensure304DoesNotContainExtraEntityHeaders(response);

        identityIsNotUsedInContentEncoding(response);

        warningsWithNonMatchingWarnDatesAreRemoved(response);
    }

    private void consumeBody(HttpResponse response) throws IOException {
        HttpEntity body = response.getEntity();
        if (body != null) EntityUtils.consume(body);
    }

    private void warningsWithNonMatchingWarnDatesAreRemoved(
            HttpResponse response) {
        Date responseDate = null;
        try {
            responseDate = DateUtils.parseDate(response.getFirstHeader(HTTP.DATE_HEADER).getValue());
        } catch (DateParseException e) {
            //Empty On Purpose
        }

        if (responseDate == null) return;

        Header[] warningHeaders = response.getHeaders(HeaderConstants.WARNING);

        if (warningHeaders == null || warningHeaders.length == 0) return;

        List<Header> newWarningHeaders = new ArrayList<Header>();
        boolean modified = false;
        for(Header h : warningHeaders) {
            for(WarningValue wv : WarningValue.getWarningValues(h)) {
                Date warnDate = wv.getWarnDate();
                if (warnDate == null || warnDate.equals(responseDate)) {
                    newWarningHeaders.add(new BasicHeader(HeaderConstants.WARNING,wv.toString()));
                } else {
                    modified = true;
                }
            }
        }
        if (modified) {
            response.removeHeaders(HeaderConstants.WARNING);
            for(Header h : newWarningHeaders) {
                response.addHeader(h);
            }
        }
    }

    private void identityIsNotUsedInContentEncoding(HttpResponse response) {
        Header[] hdrs = response.getHeaders(HTTP.CONTENT_ENCODING);
        if (hdrs == null || hdrs.length == 0) return;
        List<Header> newHeaders = new ArrayList<Header>();
        boolean modified = false;
        for (Header h : hdrs) {
            StringBuilder buf = new StringBuilder();
            boolean first = true;
            for (HeaderElement elt : h.getElements()) {
                if ("identity".equalsIgnoreCase(elt.getName())) {
                    modified = true;
                } else {
                    if (!first) buf.append(",");
                    buf.append(elt.toString());
                    first = false;
                }
            }
            String newHeaderValue = buf.toString();
            if (!"".equals(newHeaderValue)) {
                newHeaders.add(new BasicHeader(HTTP.CONTENT_ENCODING, newHeaderValue));
            }
        }
        if (!modified) return;
        response.removeHeaders(HTTP.CONTENT_ENCODING);
        for (Header h : newHeaders) {
            response.addHeader(h);
        }
    }

    private void ensure206ContainsDateHeader(HttpResponse response) {
        if (response.getFirstHeader(HTTP.DATE_HEADER) == null) {
            response.addHeader(HTTP.DATE_HEADER, DateUtils.formatDate(new Date()));
        }

    }

    private void ensurePartialContentIsNotSentToAClientThatDidNotRequestIt(HttpRequest request,
            HttpResponse response) throws IOException {
        if (request.getFirstHeader(HeaderConstants.RANGE) != null
                || response.getStatusLine().getStatusCode() != HttpStatus.SC_PARTIAL_CONTENT)
            return;

        consumeBody(response);
        throw new ClientProtocolException(UNEXPECTED_PARTIAL_CONTENT);
    }

    private void ensure200ForOPTIONSRequestWithNoBodyHasContentLengthZero(HttpRequest request,
            HttpResponse response) {
        if (!request.getRequestLine().getMethod().equalsIgnoreCase(HeaderConstants.OPTIONS_METHOD)) {
            return;
        }

        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            return;
        }

        if (response.getFirstHeader(HTTP.CONTENT_LEN) == null) {
            response.addHeader(HTTP.CONTENT_LEN, "0");
        }
    }

    private void ensure304DoesNotContainExtraEntityHeaders(HttpResponse response) {
        String[] disallowedEntityHeaders = { HeaderConstants.ALLOW, HTTP.CONTENT_ENCODING,
                "Content-Language", HTTP.CONTENT_LEN, "Content-MD5",
                "Content-Range", HTTP.CONTENT_TYPE, HeaderConstants.LAST_MODIFIED
        };
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_MODIFIED) {
            for(String hdr : disallowedEntityHeaders) {
                response.removeHeaders(hdr);
            }
        }
    }

    private boolean backendResponseMustNotHaveBody(HttpRequest request, HttpResponse backendResponse) {
        return HeaderConstants.HEAD_METHOD.equals(request.getRequestLine().getMethod())
                || backendResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT
                || backendResponse.getStatusLine().getStatusCode() == HttpStatus.SC_RESET_CONTENT
                || backendResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_MODIFIED;
    }

    private void requestDidNotExpect100ContinueButResponseIsOne(HttpRequest request,
            HttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_CONTINUE) {
            return;
        }

        HttpRequest originalRequest = requestWasWrapped(request) ?
                ((RequestWrapper)request).getOriginal() : request;
        if (originalRequest instanceof HttpEntityEnclosingRequest) {
            if (((HttpEntityEnclosingRequest)originalRequest).expectContinue()) return;
        }
        consumeBody(response);
        throw new ClientProtocolException(UNEXPECTED_100_CONTINUE);
    }

    private void transferEncodingIsNotReturnedTo1_0Client(HttpRequest request, HttpResponse response) {
        if (!requestWasWrapped(request)) {
            return;
        }

        ProtocolVersion originalProtocol = getOriginalRequestProtocol((RequestWrapper) request);

        if (originalProtocol.compareToVersion(HttpVersion.HTTP_1_1) >= 0) {
            return;
        }

        removeResponseTransferEncoding(response);
    }

    private void removeResponseTransferEncoding(HttpResponse response) {
        response.removeHeaders("TE");
        response.removeHeaders(HTTP.TRANSFER_ENCODING);
    }

    private ProtocolVersion getOriginalRequestProtocol(RequestWrapper request) {
        return request.getOriginal().getProtocolVersion();
    }

    private boolean requestWasWrapped(HttpRequest request) {
        return request instanceof RequestWrapper;
    }

}
