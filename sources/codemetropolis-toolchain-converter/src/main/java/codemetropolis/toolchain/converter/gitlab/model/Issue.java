package codemetropolis.toolchain.converter.gitlab.model;

import codemetropolis.toolchain.commons.cdf.CdfProperty;
import codemetropolis.toolchain.converter.gitlab.GitLabElement;
import codemetropolis.toolchain.converter.gitlab.Pair;
import org.gitlab4j.api.IssuesApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Issue extends GitLabElement {

    private String state;

    private int userNotesCount;
    private int weight;
    private int timeEstimate;
    private int totalTime;

    private float timeRatio;

    public void setState(String state) {
        this.state = state;
    }

    public void setTimeRatio(float timeRatio) {
        this.timeRatio = timeRatio;
    }

    public void setTimeEstimate(int timeEstimate) {
        this.timeEstimate = timeEstimate;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public void setUserNotesCount(int userNotesCount) {
        this.userNotesCount = userNotesCount;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public List<CdfProperty> createProperties() {

        List<CdfProperty> properties=new ArrayList<>();

        if (ID==null) return properties;

        IssuesApi iapi=null;

        if (gitLabApi!=null)
            iapi=new IssuesApi(gitLabApi);

        org.gitlab4j.api.models.Issue issue=null;

        if (iapi!=null) {
            Optional<org.gitlab4j.api.models.Issue> opt = iapi.getOptionalIssue(projectId, Integer.parseInt(ID));
             issue = opt.orElse(null);
        }

        if (issue!=null && issue.getId()!=null) {
            if (issue.getState()!=null)
                state = issue.getState().toString();

            if (issue.getUserNotesCount()!=null)
             userNotesCount = issue.getUserNotesCount();

            if (issue.getWeight()!=null)
             weight = issue.getWeight();

            if (issue.getTimeStats()!=null) {
                timeEstimate = issue.getTimeStats().getTimeEstimate();
                totalTime = issue.getTimeStats().getTimeEstimate();
            }
        }

        if (totalTime==0)
            timeRatio=0;
        else
            timeRatio = (float) (timeEstimate / (float) totalTime);

        if (state!=null)
            properties.add(new CdfProperty("state", state, CdfProperty.Type.STRING));

        properties.add(new CdfProperty("userNoteCount", Integer.toString(userNotesCount), CdfProperty.Type.INT));
        properties.add(new CdfProperty("weight", Integer.toString(weight), CdfProperty.Type.INT));
        properties.add(new CdfProperty("timeEstimate", Integer.toString(timeEstimate), CdfProperty.Type.INT));
        properties.add(new CdfProperty("totalTime", Integer.toString(totalTime), CdfProperty.Type.INT));
        properties.add(new CdfProperty("timeRatio", Float.toString(timeRatio), CdfProperty.Type.FLOAT));

        return properties;
    }

    @Override
    public List<Pair> createChildren() {
        List<Pair> children=new ArrayList<>();

        return children;
    }
}
