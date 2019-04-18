package codemetropolis.toolchain.converter.gitlab.model;

import codemetropolis.toolchain.commons.cdf.CdfProperty;
import codemetropolis.toolchain.converter.gitlab.GitLabElement;
import codemetropolis.toolchain.converter.gitlab.GitLabResource;
import codemetropolis.toolchain.converter.gitlab.Pair;
import codemetropolis.toolchain.converter.gitlab.Type;
import org.gitlab4j.api.*;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.TreeItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Branch extends GitLabElement {
    private String name;

    private int isMerged;
    private int developersCanPush;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsMerged() {
        return isMerged;
    }

    public void setIsMerged(int isMerged) {
        this.isMerged = isMerged;
    }

    public int getDevelopersCanPush() {
        return developersCanPush;
    }

    public void setDevelopersCanPush(int developersCanPush) {
        this.developersCanPush = developersCanPush;
    }

    public static int convertBooleanToInteger(boolean value) {
    	if (value == true)
    		return 1;
    	else
    		return 0;
    }

    public void addProperties(List<CdfProperty> list) {
        list.add(new CdfProperty("isMerged", isMerged + "", CdfProperty.Type.INT));
        list.add(new CdfProperty("developersCanPush", developersCanPush + "", CdfProperty.Type.INT));
    }

    @Override
    public List<CdfProperty> createProperties() {
        RepositoryApi rapi = new RepositoryApi(gitLabApi);
        List<CdfProperty> list = new ArrayList<>();
        
        try {
            org.gitlab4j.api.models.Branch b = rapi.getBranch(projectId, ID);

            name = b.getName();
        
            isMerged = convertBooleanToInteger(b.getMerged());
            developersCanPush = convertBooleanToInteger(b.getDevelopersCanPush());
            addProperties(list);

        } catch (GitLabApiException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Pair> createChildren() {
        RepositoryApi rapi = new RepositoryApi(gitLabApi);
        Pair child;
        List<Pair> list = new ArrayList<>();
        List<TreeItem> items = null;                
        org.gitlab4j.api.models.Branch b=null;
        
        try {
            items = rapi.getTree(projectId);
            b = rapi.getBranch(projectId, ID);
             
            Queue<Commit> commits = new LinkedList<>();
            commits.add(b.getCommit());
            CommitsApi capi=gitLabApi.getCommitsApi();

            while(!commits.isEmpty()) {
                Commit commit=commits.remove();
                list.add(new Pair(commit.getId(), Type.COMMIT));
        
                for(String commit1: commit.getParentIds()) {
                    if (!GitLabResource.consistsOf(Type.COMMIT, commit1))
                        commits.add(capi.getCommit(projectId, commit1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        for(TreeItem item: items) {
        		if(item.getType() == TreeItem.Type.TREE) {
                    child = new Pair(item.getPath()+"---"+ID, Type.TREE);
                    list.add(child);
               }
        }

        return list;
    }
}
