package codemetropolis.toolchain.converter.gitlab.model;

import codemetropolis.toolchain.commons.cdf.CdfElement;
import codemetropolis.toolchain.commons.cdf.CdfProperty;
import codemetropolis.toolchain.converter.gitlab.*;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.MilestonesApi;
import org.gitlab4j.api.models.Issue;

import java.util.ArrayList;
import java.util.List;


public class Milestone extends GitLabElement {

    private String title;
    private String state;

    private double closedRatio;
    private double openedRatio;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public List<CdfProperty> createProperties() {
        MilestonesApi mapi=null;

        if (gitLabApi!=null)
            mapi=new MilestonesApi(gitLabApi);

        List<CdfProperty> properties=new ArrayList<>();

        double all=children.size();

        try {
            org.gitlab4j.api.models.Milestone milestone=null;
            if (mapi!=null)
                milestone=mapi.getMilestone(projectId, Integer.parseInt(ID));

            if (milestone!=null) {
                title = milestone.getTitle();
                state = milestone.getState();
            }

            if (ID!=null) {
                properties.add(new CdfProperty("title", title, CdfProperty.Type.STRING));
                properties.add(new CdfProperty("state", state, CdfProperty.Type.STRING));

                for (Pair child : children) {
                    CdfElement element = GitLabResource.getElement(child).getElement();
                    if (element.getPropertyValue("state").equals("closed")) closedRatio++;
                    else openedRatio++;
                }
            }

        } catch (GitLabApiException e) {
            e.printStackTrace();
        } catch (GitLabException e) {
            e.printStackTrace();
        }

        if (all!=0.0) {
            closedRatio/=all;
            openedRatio/=all;
        }

        if (ID!=null) {
            properties.add(new CdfProperty("closedRatio", closedRatio + "", CdfProperty.Type.FLOAT));
            properties.add(new CdfProperty("openedRatio", openedRatio + "", CdfProperty.Type.FLOAT));
        }

        return properties;
    }

    @Override
    public List<Pair> createChildren() {
        if (ID==null) return new ArrayList<>();

        MilestonesApi mapi=new MilestonesApi(gitLabApi);
        List<Pair> children=new ArrayList<>();

        try {
            List<org.gitlab4j.api.models.Issue> issues=mapi.getIssues(projectId, Integer.parseInt(ID));
            for(Issue issue: issues) {
                children.add(new Pair(issue.getIid().toString(), Type.ISSUE));
            }

        } catch (GitLabApiException e) {
            e.printStackTrace();
        }

        return children;
    }
}
