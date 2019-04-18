package codemetropolis.toolchain.converter.gitlab;

import codemetropolis.toolchain.commons.exceptions.CodeMetropolisException;

public class GitLabConnectException extends CodeMetropolisException {
    public GitLabConnectException() { super(); }
    public GitLabConnectException(String message) { super(message); }
}
