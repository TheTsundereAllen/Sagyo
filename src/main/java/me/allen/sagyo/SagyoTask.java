package me.allen.sagyo;

public interface SagyoTask {

    void compute();

    default boolean shouldExecute() {
        return true;
    }

    default boolean shouldReschedule() {
        return false;
    }

}
