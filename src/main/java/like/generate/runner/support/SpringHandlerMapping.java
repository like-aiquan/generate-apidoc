package like.generate.runner.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import like.generate.runner.model.ApiInfo;
import like.generate.runner.util.JsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.AbstractHandlerMethodMapping;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.util.pattern.PathPattern;

/**
 * @author chenaiquan
 */
@Configuration
public class SpringHandlerMapping implements ApplicationContextAware {

    private Map<String, ApiInfo> apis = new HashMap<>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AbstractHandlerMethodMapping<?> handlerMethodMapping = applicationContext.getBean(AbstractHandlerMethodMapping.class);
        this.parseHandlerDoc(handlerMethodMapping);
    }

    public Map<String, ApiInfo> getApis() {
        return apis;
    }

    private void parseHandlerDoc(AbstractHandlerMethodMapping<?> handlerMethodMapping) {
        Map<?, HandlerMethod> handlerMethods = handlerMethodMapping.getHandlerMethods();
        List<ApiInfo> apiList = new ArrayList<>();
        handlerMethods.forEach((k, v) -> {
            RequestMappingInfo info = (RequestMappingInfo) k;

            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            Set<PathPattern> patterns = info.getPatternsCondition().getPatterns();
            apiList.addAll(patterns.stream()
                .map(p -> methods.stream().map(m -> new ApiInfo().setPath(p.getPatternString()).setMethod(m)).collect(Collectors.toList()))
                .flatMap(Collection::stream).collect(Collectors.toList()));

            for (MethodParameter parameter : v.getMethodParameters()) {
                Class<?> parameterType = parameter.getParameterType();
                if (parameter.hasParameterAnnotation(RequestBody.class)) {
                    Object param = BeanUtils.instantiateClass(parameterType);
                    apiList.forEach(api -> api.setBody(JsonUtil.writeValueAsString(param)));
                    continue;
                }

                if (parameter.hasParameterAnnotation(RequestParam.class)) {
                    RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
                    if (requestParam != null) {
                        apiList.forEach(api -> api.setParam(Optional.ofNullable(api.getParam()).orElse("&").concat(requestParam.name())));
                    }
                    continue;
                }

                String parameterName = Optional.ofNullable(parameter.getParameterName()).orElse("");
                apiList.forEach(api -> api.setParam(Optional.ofNullable(api.getParam()).orElse("&").concat(parameterName)));
            }

            apiList.forEach(api -> {
                if (Objects.nonNull(api.getParam())) {
                    api.setParam(api.getParam().replaceFirst("&", ""));
                }
            });
            System.out.println(apiList);
        });

        apis = apiList.stream().collect(Collectors.toMap(api -> api.getMethod() + " " + api.getPath(), Function.identity()));
    }

}
