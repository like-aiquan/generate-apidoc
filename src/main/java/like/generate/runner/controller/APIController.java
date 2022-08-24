package like.generate.runner.controller;

import like.generate.runner.form.LoginForm;
import like.generate.runner.support.SpringHandlerMapping;
import like.generate.runner.util.JsonUtil;
import like.generate.runner.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenaiquan
 */
@RestController
@RequestMapping("/like")
public class APIController {
    @Autowired
    private SpringHandlerMapping springHandlerMapping;

    @PostMapping("/mapping/{value}")
    public R post(@RequestBody LoginForm form, @RequestParam("key") String key, @PathVariable String value) {
        System.out.println(JsonUtil.writeValueAsString(form));
        System.out.println(value);
        System.out.println(key);
        return R.success(springHandlerMapping.getApis());
    }
}
