package me.allen.sagyo.distributor;

import me.allen.sagyo.factory.SagyoFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SagyoFactoryDistributor implements Runnable {

    private final Map<Integer, SagyoFactory> factoryMap;

    private int factoryIndex = 0;

    public SagyoFactoryDistributor() {
        this.factoryMap = new ConcurrentHashMap<>();
        this.factoryIndex = 0;
    }

    public SagyoFactory addFactory(SagyoFactory sagyoFactory) {
        this.factoryMap.put(++this.factoryIndex, sagyoFactory);
        return sagyoFactory;
    }

    public boolean containsFactory(int factoryIndex) {
        return this.factoryMap.containsKey(factoryIndex);
    }

    public SagyoFactory removeFactory(int factoryIndex) {
        return this.factoryMap.remove(factoryIndex);
    }

    @Override
    public void run() {
        this.factoryMap.values().forEach(factory -> factory.getTask().run());
    }

}
