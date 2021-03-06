package pl.tscript3r.photogram.infrastructure.mapper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.tscript3r.photogram.infrastructure.exception.InternalErrorPhotogramException;

import javax.validation.constraints.NotNull;
import java.util.*;

@Lazy
@Service
public class MapperServiceBean implements MapperService, ApplicationContextAware {

    private final Set<Mapper> mappers = new HashSet<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        mappers.addAll(applicationContext.getBeansOfType(Mapper.class)
                .values());
    }

    @Override
    public <E extends DataStructure, T extends DataStructure> E map(@NotNull final T source,
                                                                    @NotNull final Class<E> convertTo) {
        try {
            Mapper mapper = getMapper(source);
            if (mapper.compatible(convertTo))
                return mapper.map(source, convertTo);
            throw new InternalErrorPhotogramException("Type [" + source.getClass().getName()
                    + "] cannot be converted to [" + convertTo.getName() + "]");
        } catch (ClassCastException e) {
            throw getCustomException(e, source.getClass(), convertTo);
        }
    }

    private Mapper getMapper(final DataStructure source) {
        for (Mapper mapper : mappers)
            if (mapper.compatible(source.getClass()))
                return mapper;
        throw new InternalErrorPhotogramException("Mapper for [" +
                source.getClass().getName() + "] not found");
    }

    private InternalErrorPhotogramException getCustomException(ClassCastException e, Class source, Class target) {
        String message = "Class cast exception by %s of [%s] -> [%s]";
        if (isCollection(source))
            message = String.format(message, "collection mapping", source.getName(), target.getName());
        else
            message = String.format(message, "mapping", source.getName(), target.getName());
        return new InternalErrorPhotogramException(message, e);
    }

    private boolean isCollection(final Object o) {
        return o instanceof Collection || o instanceof Map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends DataStructure, T extends DataStructure, F extends Collection<E>>
    F map(@NotNull final Collection<T> source, @NotNull final Class<E> convertTo) {
        try {
            if (source.isEmpty())
                return (F) new ArrayList<E>();
            return getCollectionMapper(source).map(source, convertTo);
        } catch (ClassCastException e) {
            throw getCustomException(e, source.getClass(), convertTo);
        }
    }

    private <T extends DataStructure> CollectionMapper getCollectionMapper(final Collection<T> source) {
        Mapper mapper = getMapper(source.iterator().next());
        if (mapper instanceof CollectionMapper)
            return (CollectionMapper) mapper;
        throw new InternalErrorPhotogramException("Mapper for [" +
                source.getClass().getName() + "] is not an CollectionMapper, consider refactor");
    }

}
