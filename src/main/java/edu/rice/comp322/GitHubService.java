package edu.rice.comp322;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import java.util.List;

/**
 * Service used to make requests to GitHub.
 */
interface GitHubService {

    /**
     * Requests the list of repositories under the given organization.
     * @param org organization name
     * @return list of repositories
     */
    @GET("orgs/{org}/repos?per_page=100")
    Call<List<Repo>> getOrgReposCall(@Path("org") String org);

    /**
     * Requests the list of contributors for each repository.
     * @param owner owner of the repo (organization name)
     * @param repo name of the repo
     * @return list of users
     */
    @GET("repos/{owner}/{repo}/contributors?per_page=100")
    Call<List<User>> getRepContributorsCall(@Path("owner") String owner,  @Path("repo") String repo);

}