/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.protocol.oidc;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.common.ClientConnection;
import org.keycloak.crypto.KeyType;
import org.keycloak.crypto.KeyUse;
import org.keycloak.crypto.KeyWrapper;
import org.keycloak.events.EventBuilder;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.jose.jwk.JSONWebKeySet;
import org.keycloak.jose.jwk.JWK;
import org.keycloak.jose.jwk.JWKBuilder;
import org.keycloak.models.Constants;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.protocol.oidc.endpoints.AuthorizationEndpoint;
import org.keycloak.protocol.oidc.endpoints.LoginStatusIframeEndpoint;
import org.keycloak.protocol.oidc.endpoints.LogoutEndpoint;
import org.keycloak.protocol.oidc.endpoints.TokenEndpoint;
import org.keycloak.protocol.oidc.endpoints.UserInfoEndpoint;
import org.keycloak.protocol.oidc.ext.OIDCExtProvider;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.messages.Messages;
import org.keycloak.services.resources.Cors;
import org.keycloak.services.resources.RealmsResource;
import org.keycloak.services.util.CacheControlUtil;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.LinkedList;
import java.util.List;

/**
 * Resource class for the oauth/openid connect token service
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class OIDCLoginProtocolService {

    private RealmModel realm;
    private TokenManager tokenManager;
    private EventBuilder event;

    @Context
    private KeycloakSession session;

    @Context
    private HttpHeaders headers;

    @Context
    private HttpRequest request;

    @Context
    private ClientConnection clientConnection;

    public OIDCLoginProtocolService(RealmModel realm, EventBuilder event) {
        this.realm = realm;
        this.tokenManager = new TokenManager();
        this.event = event;
    }

    public static UriBuilder tokenServiceBaseUrl(UriInfo uriInfo) {
        UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder();
        return tokenServiceBaseUrl(baseUriBuilder);
    }

    public static UriBuilder tokenServiceBaseUrl(UriBuilder baseUriBuilder) {
        return baseUriBuilder.path(RealmsResource.class).path("{realm}/protocol/" + OIDCLoginProtocol.LOGIN_PROTOCOL);
    }

    public static UriBuilder authUrl(UriInfo uriInfo) {
        UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder();
        return authUrl(baseUriBuilder);
    }

    public static UriBuilder authUrl(UriBuilder baseUriBuilder) {
        UriBuilder uriBuilder = tokenServiceBaseUrl(baseUriBuilder);
        return uriBuilder.path(OIDCLoginProtocolService.class, "auth");
    }

    public static UriBuilder tokenUrl(UriBuilder baseUriBuilder) {
        UriBuilder uriBuilder = tokenServiceBaseUrl(baseUriBuilder);
        return uriBuilder.path(OIDCLoginProtocolService.class, "token");
    }

    public static UriBuilder certsUrl(UriBuilder baseUriBuilder) {
        UriBuilder uriBuilder = tokenServiceBaseUrl(baseUriBuilder);
        return uriBuilder.path(OIDCLoginProtocolService.class, "certs");
    }

    public static UriBuilder userInfoUrl(UriBuilder baseUriBuilder) {
        UriBuilder uriBuilder = tokenServiceBaseUrl(baseUriBuilder);
        return uriBuilder.path(OIDCLoginProtocolService.class, "issueUserInfo");
    }

    public static UriBuilder tokenIntrospectionUrl(UriBuilder baseUriBuilder) {
        return tokenUrl(baseUriBuilder).path(TokenEndpoint.class, "introspect");
    }

    public static UriBuilder logoutUrl(UriInfo uriInfo) {
        UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder();
        return logoutUrl(baseUriBuilder);
    }

    public static UriBuilder logoutUrl(UriBuilder baseUriBuilder) {
        UriBuilder uriBuilder = tokenServiceBaseUrl(baseUriBuilder);
        return uriBuilder.path(OIDCLoginProtocolService.class, "logout");
    }

    /**
     * Authorization endpoint
     */
    @Path("auth")
    public Object auth() {
        AuthorizationEndpoint endpoint = new AuthorizationEndpoint(realm, event);
        ResteasyProviderFactory.getInstance().injectProperties(endpoint);
        return endpoint;
    }

    /**
     * Registration endpoint
     */
    @Path("registrations")
    public Object registerPage() {
        AuthorizationEndpoint endpoint = new AuthorizationEndpoint(realm, event);
        ResteasyProviderFactory.getInstance().injectProperties(endpoint);
        return endpoint.register();
    }

    /**
     * Forgot-Credentials endpoint
     */
    @Path("forgot-credentials")
    public Object forgotCredentialsPage() {
        AuthorizationEndpoint endpoint = new AuthorizationEndpoint(realm, event);
        ResteasyProviderFactory.getInstance().injectProperties(endpoint);
        return endpoint.forgotCredentials();
    }

    /**
     * Token endpoint
     */
    @Path("token")
    public Object token() {
        TokenEndpoint endpoint = new TokenEndpoint(tokenManager, realm, event);
        ResteasyProviderFactory.getInstance().injectProperties(endpoint);
        return endpoint;
    }

    @Path("login-status-iframe.html")
    public Object getLoginStatusIframe() {
        LoginStatusIframeEndpoint endpoint = new LoginStatusIframeEndpoint();
        ResteasyProviderFactory.getInstance().injectProperties(endpoint);
        return endpoint;
    }

    @OPTIONS
    @Path("certs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVersionPreflight() {
        return Cors.add(request, Response.ok()).allowedMethods("GET").preflight().auth().build();
    }

    @GET
    @Path("certs")
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    public Response certs() {
        List<JWK> keys = new LinkedList<>();
        for (KeyWrapper k : session.keys().getKeys(realm)) {
            if (k.getStatus().isEnabled() && k.getUse().equals(KeyUse.SIG) && k.getVerifyKey() != null) {
                JWKBuilder b = JWKBuilder.create().kid(k.getKid()).algorithm(k.getAlgorithm());
                switch (k.getType()) {
                    case KeyType.RSA:
                        keys.add(b.rsa(k.getVerifyKey()));
                        break;
                    case KeyType.EC:
                        keys.add(b.ec(k.getVerifyKey()));
                        break;
                    case "GOST3411_2012_256":
                    case "GOST3411_2012_512":
                    case KeyType.GOST3411_2012_256withGOST3410_2012_256:
                    case KeyType.GOST3411_2012_512withGOST3410_2012_512:
                        keys.add(b.gost(k.getVerifyKey()));
                        break;
                }
            }
        }

        JSONWebKeySet keySet = new JSONWebKeySet();

        JWK[] k = new JWK[keys.size()];
        k = keys.toArray(k);
        keySet.setKeys(k);

        Response.ResponseBuilder responseBuilder = Response.ok(keySet).cacheControl(CacheControlUtil.getDefaultCacheControl());
        return Cors.add(request, responseBuilder).allowedOrigins("*").auth().build();
    }

    @Path("userinfo")
    public Object issueUserInfo() {
        UserInfoEndpoint endpoint = new UserInfoEndpoint(tokenManager, realm);
        ResteasyProviderFactory.getInstance().injectProperties(endpoint);
        return endpoint;
    }

    @Path("logout")
    public Object logout() {
        LogoutEndpoint endpoint = new LogoutEndpoint(tokenManager, realm, event);
        ResteasyProviderFactory.getInstance().injectProperties(endpoint);
        return endpoint;
    }

    @Path("oauth/oob")
    @GET
    public Response installedAppUrnCallback(final @QueryParam("code") String code, final @QueryParam("error") String error, final @QueryParam("error_description") String errorDescription) {
        LoginFormsProvider forms = session.getProvider(LoginFormsProvider.class);
        if (code != null) {
            return forms.setClientSessionCode(code).createCode();
        } else {
            return forms.setError(error).createCode();
        }
    }

    /**
     * For KeycloakInstalled and kcinit login where command line login is delegated to a browser.
     * This clears login cookies and outputs login success or failure messages.
     *
     * @param error
     * @return
     */
    @GET
    @Path("delegated")
    public Response kcinitBrowserLoginComplete(@QueryParam("error") boolean error) {
        AuthenticationManager.expireIdentityCookie(realm, session.getContext().getUri(), clientConnection);
        AuthenticationManager.expireRememberMeCookie(realm, session.getContext().getUri(), clientConnection);
        if (error) {
            LoginFormsProvider forms = session.getProvider(LoginFormsProvider.class);
            return forms
                    .setAttribute("messageHeader", forms.getMessage(Messages.DELEGATION_FAILED_HEADER))
                    .setAttribute(Constants.SKIP_LINK, true).setError(Messages.DELEGATION_FAILED).createInfoPage();

        } else {
            LoginFormsProvider forms = session.getProvider(LoginFormsProvider.class);
            return forms
                    .setAttribute("messageHeader", forms.getMessage(Messages.DELEGATION_COMPLETE_HEADER))
                    .setAttribute(Constants.SKIP_LINK, true)
                    .setSuccess(Messages.DELEGATION_COMPLETE).createInfoPage();
        }
    }

    @Path("ext/{extension}")
    public Object resolveExtension(@PathParam("extension") String extension) {
        OIDCExtProvider provider = session.getProvider(OIDCExtProvider.class, extension);
        if (provider != null) {
            provider.setEvent(event);
            return provider;
        }
        throw new NotFoundException();
    }

}
