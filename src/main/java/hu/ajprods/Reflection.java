package hu.ajprods;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

class Reflection {

    static Set<ParameterizedType> getInterfaces(Class<?> inputClass, Type interfaceType) {
        return Arrays.stream(inputClass.getGenericInterfaces())
                     .filter(type -> ((ParameterizedType) type).getRawType()
                                                               .equals(interfaceType))
                     .map(ParameterizedType.class::cast)
                     .collect(Collectors.toSet());
    }
}
