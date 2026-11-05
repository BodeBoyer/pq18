package comp210.pq;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

/**
 * Fails any test that runs longer than 3 seconds, so an infinite loop shows up as a failed test
 * instead of freezing the whole run. The limit is skipped while a debugger is attached, so you can
 * step through code at your own pace.
 */
class TimeLimitExtension implements InvocationInterceptor {

  private static final long LIMIT_MILLIS = 3000;

  private static final boolean DEBUGGING =
      ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("jdwp");

  @Override
  public void interceptTestMethod(
      Invocation<Void> invocation,
      ReflectiveInvocationContext<Method> invocationContext,
      ExtensionContext extensionContext)
      throws Throwable {
    if (DEBUGGING) {
      invocation.proceed();
      return;
    }
    // Run the test on a daemon thread: if it never finishes, the stuck thread cannot keep the
    // JVM (and with it the whole test run or the autograder) alive.
    Throwable[] thrown = new Throwable[1];
    Thread worker =
        new Thread(
            () -> {
              try {
                invocation.proceed();
              } catch (Throwable t) {
                thrown[0] = t;
              }
            },
            "time-limited-test");
    worker.setDaemon(true);
    worker.start();
    worker.join(LIMIT_MILLIS);
    if (worker.isAlive()) {
      fail("test took longer than " + LIMIT_MILLIS / 1000 + " seconds (infinite loop?)");
    }
    if (thrown[0] != null) {
      throw thrown[0];
    }
  }
}
