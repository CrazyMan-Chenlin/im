package com.roy.im.connection.factory;

import com.roy.im.connection.service.TypeProcessor;
import com.roy.im.connection.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author chenlin
 */
@Component
@Slf4j
public class TypeProcessorFactory {

    @Autowired
    private SpringUtils springUtils;

    private  Map<Integer, TypeProcessor> typeProcessorMap = new HashMap<>();

    @PostConstruct
    public void setAllTypeProcessorFactory()  {
        //通过反射获取
        Reflections reflections=new Reflections("com.roy.im.connection.service");
        Set<Class<? extends TypeProcessor>> typeProcessorSet =
                reflections.getSubTypesOf(TypeProcessor.class);
        typeProcessorSet.forEach(typeProcessor -> {
            try {
                TypeProcessor beanTypeProcessor = springUtils.getBean(typeProcessor);
                typeProcessorMap.put(beanTypeProcessor.getType(),beanTypeProcessor);
            } catch (Exception e) {
                log.error("typeProcessor reflect create newInstance fail.");
            }
        });
    }

    /**
     * 返回getTypeProcessor对象
     * @param type
     * @return
     */
    public TypeProcessor getTypeProcessor(int type){
        return this.typeProcessorMap.get(type);
    }
}
