import java.util.concurrent.Flow;
import java.util.function.Function;

public class DomainSubscriber<T> implements Flow.Subscriber<T> {

    private T payload;
    private boolean doLogging = false;

    public DomainSubscriber(T payload) {
        this.payload = payload;
    }

    public DomainSubscriber(T payload, boolean doLogging) {
        this.payload = payload;
        this.doLogging = true;
    }

    @Override
    public synchronized void onSubscribe(Flow.Subscription subscription) {
        log("onSubscribe", this.getClass(), subscription);
        DomainSubscription<T> domainSubscription = (DomainSubscription<T>) subscription;
        domainSubscription.setPayloadAndDoRequest(this.payload, 1);
    }

    @Override
    public synchronized void onNext(T item) {
        log("onNext", this.getClass(), item);
    }

    @Override
    public synchronized void onError(Throwable throwable) {
        log("onError", this.getClass(), throwable);
    }

    @Override
    public synchronized void onComplete() {
        log("onComplete", this.getClass());
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
