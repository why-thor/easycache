package org.galileo.easycache.easycachetest.comp;

import com.google.common.collect.Lists;
import org.galileo.easycache.easycachetest.utils.ThreadUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 击穿测试
 */
@SpringBootTest
public class BreakdownTest extends TestCommon {

    int id1 = 1;
    int id2 = 2;

    @Test
    public void start() {

    }

    @Test
    public void breakdownTest() {
        add1(id1).run();
        add1(id2).run();

        long start = System.currentTimeMillis();
        int[] wight = runnableWight();
        for (int i = 0; i < 100; i++) {
            try {
                for (int j = 0; j < 10000; j++) {
                    ThreadUtils.poolExecutor.execute(() -> lb(wight).run());
                }
            } catch (Exception e) {
                System.out.println("reject");
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException interruptedException) {
                }
            }
        }
        if (ThreadUtils.poolExecutor.getActiveCount() > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000 * 5);
            } catch (InterruptedException interruptedException) {
            }
        }
        ThreadUtils.poolExecutor.shutdown();
        long end = System.currentTimeMillis();
        System.out.println((end - start) / (10 * 1000));
    }

    public int[] runnableWight() {
        return new int[]{100, 100};
    }

    public List<Runnable> runnableList() {
        return Lists.newArrayList(cached1(), cached2());
    }

    public Runnable cached1() {
        return () -> {
            try {
                userService.getBreakdown1(id1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Runnable cached2() {
        return () -> {
            try {
                userService.getBreakdown2(id2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
