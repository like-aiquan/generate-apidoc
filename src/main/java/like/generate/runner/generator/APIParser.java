package like.generate.runner.generator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import like.generate.runner.model.ApiInfo;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenaiquan
 */
public class APIParser {

    private Map<String, ApiInfo> apis;

    private final PathMatchingResourcePatternResolver resolver;

    public APIParser(Class<?> application) {
        resolver = new PathMatchingResourcePatternResolver(application.getClassLoader());
    }

    public Map<String, ApiInfo> parse(String sources) {
        Assert.hasText(sources, "main module is empty");
        return this.parse(sources, new LinkedList<>());
    }


    public Map<String, ApiInfo> parse(String pkg, List<Class<?>> clas) {
        String path = ClassUtils.convertClassNameToResourcePath(pkg);

        URL url = Objects.requireNonNull(resolver.getClassLoader()).getResource(path);
        if (url == null || !Objects.equals(url.getProtocol(), "file")) {
            throw new RuntimeException("The package path could not be found");
        }
        this.resolverApiClass(url.getFile(), clas, pkg);
        return this.fetch(clas);
    }

    private Map<String, ApiInfo> fetch(List<Class<?>> clas) {
        for (Class<?> clazz : clas) {
            RequestMethod[] requestMethods = null;
            RequestMapping requestMapping = AnnotationUtils.findAnnotation(clazz, RequestMapping.class);
            String[] suffixes = new String[]{};
            if (Objects.nonNull(requestMapping)) {
                suffixes = requestMapping.path();
                requestMethods = requestMapping.method();
            }
            Method[] methods = clazz.getDeclaredMethods();
            String[] path = new String[]{};
            for (Method method : methods) {
                requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
                if (Objects.isNull(requestMapping)) {
                    continue;
                }
                requestMethods = requestMapping.method();
                path = requestMapping.path();
            }

            List<String> suf = Arrays.stream(suffixes).collect(Collectors.toList());
            List<String> p = Arrays.stream(path).collect(Collectors.toList());

            if (!suf.isEmpty()) {
                suf.forEach(s -> p.forEach(pa -> pa = s + pa));
            }

            List<String> requestMet = Arrays.stream(Optional.ofNullable(requestMethods)
                    .orElse(RequestMethod.values()))
                .map(Enum::name)
                .collect(Collectors.toList());
        }
        return new HashMap<>();
    }

    private void resolverApiClass(String path, List<Class<?>> clas, String pkg) {
        File[] subFile = Optional.ofNullable(new File(path).listFiles()).orElse(new File[]{});
        for (File sub : subFile) {
            if (!sub.exists()) {
                continue;
            }
            if (sub.isDirectory()) {
                String subDirect = path + "/" + sub.getName();
                String subPkg = pkg + "." + sub.getName();
                this.resolverApiClass(subDirect, clas, subPkg);
            }
        }

        Resource[] clazzRefs = this.findPkgClass(pkg);

        for (Resource clazzRef : clazzRefs) {
            if (!clazzRef.exists()) {
                continue;
            }
            if (!StringUtils.hasText(clazzRef.getFilename())) {
                continue;
            }
            Class<?> clazz = this.load(pkg + "." + StringUtils.stripFilenameExtension(clazzRef.getFilename()));
            if (isApi(clazz)) {
                clas.add(clazz);
            }
        }
    }

    private Class<?> load(String fullName) {
        try {
            return Class.forName(ClassUtils.convertResourcePathToClassName(fullName));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Resource[] findPkgClass(String sources) {
        try {
            return resolver.getResources(ClassUtils.convertClassNameToResourcePath(sources) + "/*.class");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isApi(Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            return false;
        }
        return clazz.isAnnotationPresent(RestController.class) || clazz.isAnnotationPresent(Controller.class);
    }

}
