import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import static java.util.concurrent.Flow.Publisher;
import static java.util.concurrent.Flow.Subscriber;

public class DomainPublisher<T> implements Publisher<T> {

    private boolean doLogging = false;
    private final List<Subscriber<? super T>> subscribers = new ArrayList<>();
    private final ExecutorService executor = ForkJoinPool.commonPool(); // daemon-based

    public DomainPublisher(boolean doLogging) {
        this.doLogging = doLogging;
    }

    @Override
    public synchronized void subscribe(Subscriber<? super T> subscriber) {
        log("subscribe", this.getClass(), subscriber);

        if (subscribers.contains(subscriber)) {
            throw new IllegalArgumentException("The given subscriber " + subscriber.hashCode() + " is already subscribed.");
        }

        subscribers.add(subscriber);
        subscriber.onSubscribe(
                new DomainSubscription<T>(subscriber, executor, doLogging)
        );
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

