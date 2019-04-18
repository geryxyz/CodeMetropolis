package codemetropolis.toolchain.converter.gitlab.model;

import codemetropolis.toolchain.commons.cdf.CdfProperty;
import codemetropolis.toolchain.converter.gitlab.GitLabElement;
import codemetropolis.toolchain.converter.gitlab.GitLabResource;
import codemetropolis.toolchain.converter.gitlab.Pair;
import codemetropolis.toolchain.converter.gitlab.Type;
import org.gitlab4j.api.*;
import org.gitlab4j.api.models.Assignee;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.CustomAttribute;
import org.gitlab4j.api.models.Issue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User extends GitLabElement {
    private String username;

    private int isAdmin;
    private int commitNumber;
    private int deletions;
    private int additions;
    private int total;

    private float deletionRatio;
    private float addingRatio;

    public User() {}
    
    public float getDeletionRatio() {
    	return deletionRatio;
    }
    public float getAddingRatio() { return addingRatio; }

    public void setDeletionRatio(int deletion, int total) {
        if (total == 0)
            deletionRatio = 0.0f;
        else
            deletionRatio = ((float)deletion / (float)total);
    }

    public void setAddingRatio(int additions, int total) {
        if(total == 0)
            addingRatio = 0.0f;
        else
            addingRatio = ((float)additions/ (float)total);
    }
    
    @Override
    public List<CdfProperty> createProperties() {
        List<CdfProperty> list = new ArrayList<>();
        UserApi uapi = gitLabApi.getUserApi();
        RepositoryApi rapi=gitLabApi.getRepositoryApi();

        commitNumber=0;
        additions=0;
        deletions=0;

        try {
            List<org.gitlab4j.api.models.User> currentUsers = uapi.findUsers(ID);

            Map<String, GitLabElement> typeCommit=GitLabResource.getAllByType(Type.COMMIT);
            CommitsApi commitsApi=gitLabApi.getCommitsApi();

            for(GitLabElement gle: typeCommit.values()) {
                Commit commit=commitsApi.getCommit(projectId, gle.getID());

                if ( commit.getCommitterEmail().equals(ID)) {
                    commitNumber++;
                    additions+=commit.getStats().getAdditions();
                    deletions+=commit.getStats().getDeletions();                    
                }
                
            }
            
            total = deletions + additions;

            if (currentUsers.size()>0) {
                org.gitlab4j.api.models.User currentUser = currentUsers.get(0);
                List<CustomAttribute> attributeLIst = currentUser.getCustomAttributes();

                username = currentUser.getUsername();
                Boolean admin = currentUser.getIsAdmin();

                if (admin != null && currentUser.getIsAdmin()) {
                    isAdmin = 1;
                } else {
                    isAdmin = 0;
                }

                list.add(new CdfProperty("username", username, CdfProperty.Type.STRING));
                list.add(new CdfProperty("admin", isAdmin + "", CdfProperty.Type.INT));
            }
            
            setDeletionRatio(deletions, total);
            setAddingRatio(additions, total);

            list.add(new CdfProperty("numberOfCommits", commitNumber + "", CdfProperty.Type.INT));
            list.add(new CdfProperty("deletions", deletions + "", CdfProperty.Type.INT));
            list.add(new CdfProperty("additions", additions + "", CdfProperty.Type.INT));
            list.add(new CdfProperty("deletionRatio", deletionRatio + "", CdfProperty.Type.FLOAT));
            list.add(new CdfProperty("additionRatio", addingRatio + "", CdfProperty.Type.FLOAT));

        } catch (GitLabApiException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Pair> createChildren() {
        List<Pair> list = new ArrayList<>();

        return list;
    }
}
