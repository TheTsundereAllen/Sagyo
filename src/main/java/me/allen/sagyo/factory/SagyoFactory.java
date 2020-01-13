package me.allen.sagyo.factory;

import me.allen.sagyo.SagyoTask;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

public class SagyoFactory {

    private Runnable taskExecutor;

    private final long timePeriod;

    private final Deque<SagyoTask> sagyoTasks;

    public SagyoFactory(long timePeriod, TimeUnit timeUnit) {
        this(timeUnit.toMillis(timePeriod));
    }

    public SagyoFactory(long timePeriod) {
        this.timePeriod = timePeriod;
        this.sagyoTasks = new ArrayDeque<>();
        this.taskExecutor = new SagyoRunnable();
    }

    private SagyoTask firstRescheduledWorkload;

    private boolean computeWorkload(SagyoTask task) {
        if (task != null) {
            if (task.shouldExecute()) {
                task.compute();
            }

            if (task.shouldReschedule()) {
                postWorkload(task);
                if (firstRescheduledWorkload == null) {
                    firstRescheduledWorkload = task;
                } else {
                    return firstRescheduledWorkload != task;
                }
            }
        }

        return true;
    }

    public Runnable getTask() {
        return this.taskExecutor;
    }

    public void postWorkload(SagyoTask task) {
        sagyoTasks.add(task);
    }

    class SagyoRunnable implements Runnable {

        @Override
        public void run() {
            long stopTime = System.currentTimeMillis() + timePeriod;

            while (!sagyoTasks.isEmpty() && System.currentTimeMillis() <= stopTime) {
                if (!computeWorkload(sagyoTasks.poll())) break;
            }
        }

    }

    public static SagyoFactoryBuilder builder() {
        return new SagyoFactoryBuilder();
    }

    static class SagyoFactoryBuilder {
        private long timePeriod;
        private TimeUnit timeUnit;

        public SagyoFactoryBuilder timePeriod(long period) {
            this.timePeriod = period;
            return this;
        }

        public SagyoFactoryBuilder timeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }

        public SagyoFactory build() {
            return this.timeUnit == null ? new SagyoFactory(this.timePeriod) : new SagyoFactory(this.timePeriod, this.timeUnit);
        }
    }
}
