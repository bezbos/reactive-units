import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ReactiveUnit<T> implements DomainLogger {

    private boolean doLogging = false;
    private T payload;
    private DomainSubscriber<T> subscriber;
    private DomainPublisher<T> publisher;

    /**
     * Creates a new <code>ReactiveUnit</code> instance and assigns it the provided payload.
     *
     * @param payload The payload that will be assigned to the newly created <code>ReactiveUnit</code>.
     * @return New <code>ReactiveUnit</code> instance
     */
    public static <T> ReactiveUnit<T> build(T payload) {
        return new ReactiveUnit<T>()
                .payload(payload);
    }

    /**
     * Execute a function with the <code>ReactiveUnit</code> payload, without modifying it.
     *
     * @param action Function with a single parameter, which is the current <code>ReactiveUnit</code> payload .
     * @return This <code>ReactiveUnit</code> instance
     */
    public synchronized ReactiveUnit<T> forOne(Consumer<? super T> action) {
        log(this.doLogging, this.getClass(), "forOne");
        action.accept(payload);

        return this;
    }

    /**
     * Execute an arbitrary function without affecting the <code>ReactiveUnit</code> state.
     *
     * @param action Function without parameters
     * @return This <code>ReactiveUnit</code> instance
     */
    public synchronized ReactiveUnit<T> forNone(Supplier<? super T> action) {
        log(this.doLogging, this.getClass(), "forNone");
        action.get();

        return this;
    }

    /**
     * Applies the given function to the payload of this <code>ReactiveUnit</code> and replaces it with the result.
     *
     * @param function Function with a single parameter, which is the current <code>ReactiveUnit</code> payload.
     * @return This <code>ReactiveUnit</code> instance
     */
    public synchronized ReactiveUnit<T> map(Function<? super T, ? extends T> function) {
        log(this.doLogging, this.getClass(), "map", function);
        payload = function.apply(payload);

        return this;
    }

    /**
     * Enables logging for this <code>ReactiveUnit</code>.
     *
     * @return This <code>ReactiveUnit</code> instance
     */
    public synchronized ReactiveUnit<T> log() {
        doLogging = true;
        log(this.doLogging, this.getClass(), "log");

        return this;
    }

    /**
     * Sets the payload of this <code>ReactiveUnit</code>.
     *
     * @param payload Payload that will be assigned to this <code>ReactiveUnit</code>
     * @return This <code>ReactiveUnit</code> instance
     */
    public synchronized ReactiveUnit<T> payload(T payload) {
        log(this.doLogging, this.getClass(), "just", payload);
        this.payload = payload;

        return this;
    }

    /**
     * Initializes this <code>ReactiveUnit</code>.
     *
     * @return This <code>ReactiveUnit</code> instance
     */
    public synchronized ReactiveUnit<T> subscribe() {
        log(this.doLogging, this.getClass(), "subscribe");
        publisher = new DomainPublisher<>(doLogging);
        subscriber = new DomainSubscriber<>(payload, doLogging);
        publisher.subscribe(subscriber);

        return this;
    }

}
