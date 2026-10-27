/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2021 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
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
package org.jboss.pnc.repositorydriver.endpoints;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.jboss.pnc.api.dto.ComponentVersion;
import org.jboss.pnc.api.repositorydriver.dto.ArchiveRequest;
import org.jboss.pnc.api.repositorydriver.dto.RepositoryCollectRequest;
import org.jboss.pnc.api.repositorydriver.dto.RepositoryCreateRequest;
import org.jboss.pnc.api.repositorydriver.dto.RepositoryCreateResponse;
import org.jboss.pnc.api.repositorydriver.dto.RepositoryPromoteRequest;
import org.jboss.pnc.api.repositorydriver.dto.RepositoryPromoteResult;
import org.jboss.pnc.api.repositorydriver.dto.RepositoryRollbackRequest;
import org.jboss.pnc.api.repositorydriver.dto.RepositoryRollbackResult;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface RepositoryDriverEndpoint {
    /**
     * Create a new repository for the build. If Indy responds with an error an error response is returned to the
     * invoker.
     */
    @POST
    @Path("/create")
    RepositoryCreateResponse create(RepositoryCreateRequest repositoryCreateRequest);

    /**
     * Seals the tracking report.
     *
     * @param buildContentId
     */
    @PUT
    @Path("/seal")
    void seal(String buildContentId);

    /**
     * Retrieves the tracking report from Indy and promotes the repository. The endpoint returns after tracking report
     * retrieval, if the retrieval fails and error response is returned. The promotion is an async operation, the result
     * is sent via callback defined in the {@link RepositoryPromoteRequest}
     */
    @PUT
    @Path("/promote")
    void promote(RepositoryPromoteRequest promoteRequest);

    /**
     * Rolls back the artifacts promoted by {@link #promote(RepositoryPromoteRequest)}. The operation is synchronous,
     * the outcome is returned in {@link RepositoryRollbackResult#getStatus()}.
     */
    @DELETE
    @Path("/rollback")
    RepositoryRollbackResult rollback(RepositoryRollbackRequest rollbackRequest);

    @POST
    @Path("/archive")
    void archive(ArchiveRequest archiveRequest);

    /**
     * Gets repository manager result for a specific buildContentId. It generates a successful result from tracking
     * report even for builds that failed because of a system error with a sealed tracking record.
     */
    @GET
    @Path("/{id}/repository-manager-result")
    RepositoryPromoteResult collectRepoManagerResult(
            @PathParam("id") String buildContentId,
            RepositoryCollectRequest collectRequest);

    @GET
    @Path("/version")
    ComponentVersion getVersion();
}
