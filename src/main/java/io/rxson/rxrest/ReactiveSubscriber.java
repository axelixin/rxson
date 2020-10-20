package io.rxson.rxrest;

import io.rxson.reactive.RxPathUtils;
import io.rxson.reactive.Streamable;
import org.jsfr.json.JsonSurfer;

import java.util.Collection;

import static io.rxson.rxrest.SubscriberUtils.bind;

/**
 * @param <T> the subscribed item type
 * <p>
 * A receiver of http messages (stream chunks), and Publisher of chunks as POJOs accordingly
 * @author Mohamed Aly Bou Hanane
 * ©2020
 */
final public class ReactiveSubscriber<T> extends FlowSubscriber {

    private final T streamInstance;
    private final Collection<Streamable> streamablePaths;

    public ReactiveSubscriber(final Class<T> clazz, final JsonSurfer surfer) {
        super(surfer);
        streamablePaths = RxPathUtils.mapFields(clazz);
        streamInstance = RxPathUtils.createInstance(clazz);
        streamablePaths.forEach(it -> it.invokeSetter(streamInstance));

        this.nonBlockingParser = surfer.createNonBlockingParser(bind(getRxPaths(), surfer, subscription));
    }

    public T getStreamInstance() {
        return streamInstance;
    }

    public Collection<Streamable> getRxPaths() {
        return streamablePaths;
    }
}
