package me.allen.sagyo;

import me.allen.sagyo.distributor.SagyoFactoryDistributor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sagyo {
    private final boolean async;
    private final ExecutorService asyncThread;

    private boolean started;

    private SagyoFactoryDistributor distributor；
        
    public Sagyo() {
        this(false);
    }

    public Sagyo(boolean async) {
        this.async = async;
        this.asyncThread = async ? Executors.newCachedThreadPool() : null;
        this.started = false;
        this.distributor = new SagyoFactoryDistributor();
    }

    public void start() {
        if (!this.started) {

            if (this.async && this.asyncThread != null) {
                this.asyncThread.execute(this.distributor);
            } else {
                this.distributor.run();
            }
            this.started = true;
        }
    }
}
