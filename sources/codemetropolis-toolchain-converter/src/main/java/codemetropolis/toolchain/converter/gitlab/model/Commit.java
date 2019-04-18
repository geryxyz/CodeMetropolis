package codemetropolis.toolchain.converter.gitlab.model;

import codemetropolis.toolchain.commons.cdf.CdfProperty;
import codemetropolis.toolchain.converter.gitlab.GitLabElement;
import codemetropolis.toolchain.converter.gitlab.Pair;
import org.gitlab4j.api.CommitsApi;
import org.gitlab4j.api.GitLabApiException;

import java.util.ArrayList;
import java.util.List;

public class Commit extends GitLabElement {
    private int messageLength;
    private int addition;
    private int deletions;
    private int total;
    private int numberOfComments;

    private String status;
    private String message;

    public void setTotal(int total) {
        this.total = total;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDeletions(int deletions) {
        this.deletions = deletions;
    }

    public void setAddition(int addition) {
        this.addition = addition;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    @Override
    public List<CdfProperty> createProperties() {
        CommitsApi capi = new CommitsApi(gitLabApi);
        List<CdfProperty> list = new ArrayList<>();
        org.gitlab4j.api.models.Commit c=null;

        try {
             c= capi.getCommit(projectId, ID);
            if (c!=null) {
                message=c.getMessage();
                addition = c.getStats().getAdditions();
                deletions = c.getStats().getDeletions();
                total = c.getStats().getTotal();
                status = c.getStatus();
                numberOfComments = capi.getComments(projectId, ID).size();
            }
        } catch (GitLabApiException e) {
            //e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (message!=null)
            messageLength=message.length();
        if (ID!=null) {
            list.add(new CdfProperty("messageLength", Integer.toString(messageLength), CdfProperty.Type.INT));
            list.add(new CdfProperty("addition", Integer.toString(addition), CdfProperty.Type.INT));
            list.add(new CdfProperty("deletion", Integer.toString(deletions), CdfProperty.Type.INT));
            list.add(new CdfProperty("total", Integer.toString(total), CdfProperty.Type.INT));
            list.add(new CdfProperty("status", status, CdfProperty.Type.STRING));
            list.add(new CdfProperty("numberOfComments", numberOfComments + "", CdfProperty.Type.INT));
            list.add(new CdfProperty("message", message, CdfProperty.Type.STRING));
        }

        return list;
    }

    @Override
    public List<Pair> createChildren() {
        List<Pair> list=new ArrayList<>();

        return list;
    }
}
