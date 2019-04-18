package codemetropolis.toolchain.converter.gitlab.model;

import codemetropolis.toolchain.commons.cdf.CdfProperty;
import codemetropolis.toolchain.converter.gitlab.GitLabElement;
import codemetropolis.toolchain.converter.gitlab.Pair;
import codemetropolis.toolchain.converter.gitlab.Type;
import org.gitlab4j.api.CommitsApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.RepositoryApi;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.TreeItem;

import java.util.ArrayList;
import java.util.List;

public class Tree extends GitLabElement {

    private int numberOfChildren;

    private String branchName;
    private String name;

    public String getBranchName() {
        return branchName;
    }
    public String getName() {
        return name;
    }

    @Override
    public List<CdfProperty> createProperties() {
        List<CdfProperty> properties=new ArrayList<>();

        if (ID==null) return properties;

        RepositoryApi repository=null;

        if (gitLabApi!=null) {
            repository=new RepositoryApi(gitLabApi);
        }

        String names[] = ID.split("---");
        branchName = names[names.length-1];
        List<Pair> out=new ArrayList<>();
        name=names[0];

        if (repository!=null) {
            try {
                List<TreeItem> list = repository.getTree(projectId, name, branchName, true);
                numberOfChildren = list.size();
            } catch (GitLabApiException e) {
                e.printStackTrace();
            }
        }

        properties.add(new CdfProperty("numberOfChildren", numberOfChildren + "", CdfProperty.Type.INT));

        return properties;
    }

    @Override
    public List<Pair> createChildren() {
        if (ID==null) return new ArrayList<>();
        RepositoryApi repository=new RepositoryApi(gitLabApi);
        String names[] = ID.split("---");
        String branchName = names[names.length-1];
        List<Pair> out=new ArrayList<>();
        String name=names[0];

        try {
            List<TreeItem> list=repository.getTree(projectId, name, branchName);

            for(TreeItem treeItem: list) {
                if (treeItem.getType()==TreeItem.Type.TREE) {
                    out.add(new Pair(treeItem.getPath()+"---"+branchName, Type.TREE));
                }

            }
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }


        return out;
    }
}
