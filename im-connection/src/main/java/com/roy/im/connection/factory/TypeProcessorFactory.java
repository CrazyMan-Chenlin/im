package com.roy.im.connection.factory;

import com.roy.im.connection.service.TypeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
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

    private  Map<Integer, TypeProcessor> typeProcessorMap = new HashMap<>();

    @PostConstruct
    public void setAllTypeProcessorFactory() throws ClassNotFoundException {
        //通过反射获取
        Reflections reflections=new Reflections();
        Set<Class<? extends TypeProcessor>> typeProcessorSet =
                reflections.getSubTypesOf(TypeProcessor.class);
        typeProcessorSet.forEach(typeProcessor -> {
            try {
                TypeProcessor typeProcessor1 = typeProcessor.newInstance();
                typeProcessorMap.put(typeProcessor1.getType(),typeProcessor1);
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
        return typeProcessorMap.get(type);
    }
}
