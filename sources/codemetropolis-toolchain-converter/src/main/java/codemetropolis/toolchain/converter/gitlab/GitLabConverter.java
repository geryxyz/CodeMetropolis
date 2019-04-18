package codemetropolis.toolchain.converter.gitlab;

import codemetropolis.toolchain.commons.cdf.CdfElement;
import codemetropolis.toolchain.commons.cdf.CdfProperty;
import codemetropolis.toolchain.commons.cdf.CdfTree;
import codemetropolis.toolchain.commons.cdf.converter.CdfConverter;
import codemetropolis.toolchain.commons.exceptions.CodeMetropolisException;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;

import java.util.List;
import java.util.Map;

//to run: -t gitlab -i hostUrl (e.g.: http://gitlab-okt.sed.hu) -o output_dir -p username=<your_username> password=<your_password> group=<group_name> project=<project_name>

public class GitLabConverter extends CdfConverter {

    public final String USER_NAME_PARAM = "username";
    public final String PASSWORD_PARAM = "password";
    public final String GROUP_PARAM = "group";
    public final String PROJECT_PARAM = "project";
    
    private String hostUrl;
    private String projectName;
    private String groupName;

    private Project p;

    public String getHostUrl() {
		return hostUrl;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Project getP() {
		return p;
	}

	public void setP(Project p) {
		this.p = p;
	}

    public String createFullUrl() {
    	return hostUrl + "/" + groupName + "/" + projectName;
    }
    
    public GitLabConverter() {super(null);}
    
    public GitLabConverter(Map<String, String> params) {
        super(params);
    }

    public void authentication() throws GitLabConnectException
    {
        projectName = getParameter(PROJECT_PARAM);
        groupName = getParameter(GROUP_PARAM);

        GitLabClient glc = new GitLabClient(hostUrl, getParameter(USER_NAME_PARAM), getParameter(PASSWORD_PARAM));
        glc.authentication();
    }

    @Override
    public CdfTree createElements(String source) throws CodeMetropolisException {
        hostUrl = source;

        try {
            authentication();

            List<Project> pList;
            pList = GitLabElement.gitLabApi.getProjectApi().getProjects();

            for(Project pr : pList)
            {
                if(pr.getName().equalsIgnoreCase(projectName)) {
                    p = pr;
                    GitLabElement.setProjectID(p.getId());
                }
            }
        } catch (GitLabConnectException e) {
            throw new CodeMetropolisException(e.getMessage());
        } catch (GitLabApiException e) {
            throw new CodeMetropolisException(e.getMessage());
        }

        CdfTree tree = new CdfTree();
        CdfElement root = null;

        try {
            root = GitLabResource.getElement(Type.PROJECT, Integer.toString(GitLabElement.projectId), Integer.toString(GitLabElement.projectId));
        } catch (GitLabException e) {
            throw new CodeMetropolisException(e.getMessage());
        }

        tree.setRoot(root);

        return tree;
    }
}
