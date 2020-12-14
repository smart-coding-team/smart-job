package cn.smartcoding.generator.util;

import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;

public class test {

    public static void main(String[] args) {
//        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.STRING));
//
////假设我们引入的是Beetl引擎，则：
//        Template template = engine.getTemplate("Hello ${name}");
////Dict本质上为Map，此处可用Map
//        String result = template.render(Dict.create().set("name", "Hutool"));
//        System.out.println("result = " + result);

        Map<String, String> valuesMap = new HashMap<String, String>();
        valuesMap.put("animal", "quick brown fox");
        valuesMap.put("target", "lazy dog");
        String templateString = "The ${animal:kkkkk} jumped over the ${target}.";
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        sub.setValueDelimiter(":");
        String resolvedString = sub.replace(templateString);
        System.out.println("result = " + resolvedString);
    }
}
