import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.concurrent.Future;

public class DomainSubscription<T> implements Flow.Subscription {
    private final Flow.Subscriber<? super T> subscriber;
    private final ExecutorService executor;
    private final boolean doLogging;
    private Future<?> future; // to allow cancellation
    private boolean completed;
    private T payload;

    DomainSubscription(Flow.Subscriber<? super T> subscriber,
                       ExecutorService executor,
                       boolean doLogging) {
        this.subscriber = subscriber;
        this.executor = executor;
        this.doLogging = doLogging;
    }

    public synchronized void setPayloadAndDoRequest(T payload, long n) {
        this.payload = payload;

        this.request(n);
    }

    @Override
    public synchronized void request(long n) {
        log("request", this.getClass(), n);

        if (n != 0 && !completed) {
            completed = true;
            if (n < 0) {
                IllegalArgumentException ex = new IllegalArgumentException("DomainSubscription::request, parameter [n] cannot be less than 0.");
                executor.execute(() -> subscriber.onError(ex));
            } else {
                future = executor.submit(() -> {
                    subscriber.onNext(payload);
                    subscriber.onComplete();
                });
            }
        }
    }

    @Override
    public synchronized void cancel() {
        log("cancel", this.getClass());

        completed = true;
        if (future != null) future.cancel(false);
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
