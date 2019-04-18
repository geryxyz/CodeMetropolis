package codemetropolis.toolchain.converter.gitlab.model;

import codemetropolis.toolchain.commons.cdf.CdfProperty;
import codemetropolis.toolchain.converter.gitlab.GitLabElement;
import codemetropolis.toolchain.converter.gitlab.GitLabResource;
import codemetropolis.toolchain.converter.gitlab.Pair;
import codemetropolis.toolchain.converter.gitlab.Type;
import org.gitlab4j.api.*;
import org.gitlab4j.api.models.*;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Issue;
import org.gitlab4j.api.models.Milestone;

import java.util.*;

public class Project extends GitLabElement {

    private String name;

    private int commitCount;
    private int jobArtifactsSize;
    private int lfsObjectSize;
    private int storageSize;
    private int approvalsNumber = 0;
    private int forksCount = 0;

    public Project() {}

    public int getCommitCount() {
        return commitCount;
    }

    public void setCommitCount(int commitCount) {
        this.commitCount = commitCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getJobArtifactsSize() {
        return jobArtifactsSize;
    }

    public void setJobArtifactsSize(int jobArtifactsSize) {
        this.jobArtifactsSize = jobArtifactsSize;
    }

    public int getLfsObjectSize() {
        return lfsObjectSize;
    }

    public void setLfsObjectSize(int lfsObjectSize) {
        this.lfsObjectSize = lfsObjectSize;
    }

    public int getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(int storageSize) {
        this.storageSize = storageSize;
    }

    public int getApprovalsNumber() {
        return approvalsNumber;
    }

    public void setApprovalsNumber(int approvalsNumber) {
        this.approvalsNumber = approvalsNumber;
    }

    public int getForksCount() {
        return forksCount;
    }

    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }
    private class BranchHolder implements Comparable {
        String branchName;

        Date commitDate;

        public BranchHolder(String branchName, Date commitDate) {
            this.branchName=branchName;
            this.commitDate=commitDate;
        }
        @Override
        public int compareTo(Object o) {
            return this.commitDate.compareTo(((BranchHolder)o).commitDate);
        }

    }

    public void addProperties(List<CdfProperty> properties) {
        properties.add(new CdfProperty("commitCount", Integer.toString(commitCount), CdfProperty.Type.INT));
        properties.add(new CdfProperty("jobArtifactsSize", Integer.toString(jobArtifactsSize), CdfProperty.Type.INT));
        properties.add(new CdfProperty("lfsObjectSize", Integer.toString(lfsObjectSize), CdfProperty.Type.INT));
        properties.add(new CdfProperty("storageSize", Integer.toString(storageSize), CdfProperty.Type.INT));
        properties.add(new CdfProperty("approvalsNumber", Integer.toString(approvalsNumber), CdfProperty.Type.INT));
        properties.add(new CdfProperty("forksCount", Integer.toString(forksCount), CdfProperty.Type.INT));
        properties.add(new CdfProperty("name", name, CdfProperty.Type.STRING));
    }

    @Override
    public List<CdfProperty> createProperties() {

        List<CdfProperty> properties = new ArrayList<>();        

        if(gitLabApi != null) {
        	ProjectApi papi = new ProjectApi(gitLabApi);
        	
        	try {
                org.gitlab4j.api.models.Project proj = papi.getProject(ID, true);
                ProjectStatistics projectStatistics = proj.getStatistics();

                if (proj.getApprovalsBeforeMerge() != null)
                    approvalsNumber=proj.getApprovalsBeforeMerge();

                name = proj.getName();

                if (proj.getForksCount() != null)
                    forksCount=proj.getForksCount();

                storageSize = (int) projectStatistics.getStorageSize();
                lfsObjectSize = (int) projectStatistics.getLfsObjectSize();
                jobArtifactsSize = (int) projectStatistics.getJobArtifactsSize();
                commitCount = (int) projectStatistics.getCommitCount();

                addProperties(properties);

            } catch (GitLabApiException e) {
                e.printStackTrace();
            }        	
        }        
        
        return properties;
    }

    @Override
    public List<Pair> createChildren() {
        List<Pair> children = new ArrayList<>();
        
        if(gitLabApi != null) {
        	try {
                MilestonesApi mapi = gitLabApi.getMilestonesApi();
                List<org.gitlab4j.api.models.Milestone> milestoneList = mapi.getMilestones(Integer.parseInt(ID));

                for (org.gitlab4j.api.models.Milestone milestone: milestoneList) {
                    children.add(new Pair(milestone.getId().toString(), Type.MILESTONE));
                }

                RepositoryApi rapi=new RepositoryApi(gitLabApi);

                List<org.gitlab4j.api.models.Branch> branches=rapi.getBranches(Integer.parseInt(ID));

                Set<BranchHolder> holders=new TreeSet<>();

                for(org.gitlab4j.api.models.Branch branch: branches) {
                    holders.add(new BranchHolder(branch.getName(), branch.getCommit().getCommittedDate()));
                }

                for(BranchHolder holder: holders) {
                    children.add(new Pair(holder.branchName, Type.BRANCH));
                }

                for(Contributor contributor: rapi.getContributors(Integer.parseInt(ID))) {
                    children.add(new Pair(contributor.getEmail(), Type.USER));             
                }
            } catch (GitLabApiException e) {
                e.printStackTrace();
            }

        }        
        
        return children;
    }
}
