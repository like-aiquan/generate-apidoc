package like.generate.runner.model;

import java.util.Objects;
import java.util.Optional;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author chenaiquan
 */
public class ApiInfo {
    private String path;
    private RequestMethod method;
    private String body;
    private String param;
    private String result;
    private String describe;

    public String getPath() {
        return path;
    }

    public ApiInfo setPath(String path) {
        this.path = path;
        return this;
    }

    public String getMethod() {
        return Optional.ofNullable(method).map(Objects::toString).orElse("");
    }

    public ApiInfo setMethod(RequestMethod method) {
        this.method = method;
        return this;
    }

    public String getBody() {
        return body;
    }

    public ApiInfo setBody(String body) {
        this.body = body;
        return this;
    }

    public String getParam() {
        return param;
    }

    public ApiInfo setParam(String param) {
        this.param = param;
        return this;
    }

    public String getResult() {
        return result;
    }

    public ApiInfo setResult(String result) {
        this.result = result;
        return this;
    }

    public String getDescribe() {
        return describe;
    }

    public ApiInfo setDescribe(String describe) {
        this.describe = describe;
        return this;
    }

    @Override
    public String toString() {
        return "ApiInfo{" +
            "path='" + path + '\'' +
            ", method=" + method +
            ", body='" + body + '\'' +
            ", param='" + param + '\'' +
            ", result='" + result + '\'' +
            ", describe='" + describe + '\'' +
            '}';
    }
}
