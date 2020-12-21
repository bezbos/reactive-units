/**
 * Temporary logging solution. Don't rely on it, as it's very likely going to be replaced in the future
 *
 * @author Boško Bezik
 */
@Deprecated
public interface DomainLogger {

    @Deprecated
    default void log(boolean doLogging, Class clazz, String methodName, Object... args) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i] + ((i + 1) == args.length ? "" : ", "));
        }
        sb.append("}");

        if (doLogging) {
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
