package codemetropolis.toolchain.converter.gitlab;

public class GitLabException extends Exception{
    public GitLabException() {
        super();
    }

    public GitLabException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GitLabException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitLabException(String message) {
        super(message);
    }

    public GitLabException(Throwable cause) {
        super(cause);
    }
}
