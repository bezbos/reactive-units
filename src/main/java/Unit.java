import java.util.function.Function;

public class Unit<T> {

    private boolean doLogging = false;
    private T data;
    private DomainSubscriber<T> subscriber;
    private DomainPublisher<T> publisher;

    public synchronized Unit<T> map(Function<T, T> function){
        log("map", this.getClass(), function);
        data = function.apply(data);

        return this;
    }

    public synchronized Unit<T> log() {
        doLogging = true;
        log("log", this.getClass());

        return this;
    }

    public synchronized Unit<T> just(T data) {
        log("just", this.getClass(), data);
        this.data = data;

        return this;
    }

    public synchronized Unit<T> subscribe() {
        log("subscribe", this.getClass());
        publisher = new DomainPublisher<>(doLogging);
        subscriber = new DomainSubscriber<>(data, doLogging);
        publisher.subscribe(subscriber);

        return this;
    }

    private void log(String methodName, Class clazz, Object... args) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i] + ((i + 1) == args.length ? "" : ", "));
        }
        sb.append("}");

        if (this.doLogging) {
            System.out.println(
                    clazz.getSimpleName()
                            + "<"
                            + clazz.getGenericSuperclass().getTypeName()
                            + ">::"
                            + methodName
                            + " "
                            + sb.toString()
            );
        }
    }
}
