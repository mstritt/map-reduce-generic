/*
 *     Orbit, a versatile image analysis software for biological image-based quantification.
 *     Copyright (C) 2009 - 2016 Actelion Pharmaceuticals Ltd., Gewerbestrasse 16, CH-4123 Allschwil, Switzerland.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.actelion.research.mapReduceGeneric.executors;

import com.actelion.research.mapReduceGeneric.IMapReduce;
import com.actelion.research.mapReduceGeneric.utils.KeyValue;

import java.util.*;
import java.util.concurrent.*;

/**
 * Executes mapReduce tasks, multi core. This implementation is perfect for executing mapReduce tasks on a single local machine.
 * By default the number of threads is set to the number of core the machine has. It can be set to a different (e.g. smaller) number
 * via setMaxThreads().
 *
 * @param <T> Type Input (e.g. Integer for IDs)
 * @param <K> Output Key (measurement identifier)
 * @param <V> Output Value (e.g. Integer for a count)
 */
public class MapReduceExecutorLocalMultiCore<T, K, V> implements IMapReduceExecutor<T, K, V> {

    private int maxThreads = Runtime.getRuntime().availableProcessors();
    private double progress = 0d;

    public Map<K, V> execute(final Collection<T> elements, final IMapReduce<T, K, V> mapReduce) throws Exception {
        if (elements == null) throw new IllegalArgumentException("elements is null");
        if (elements.size() == 0) throw new IllegalArgumentException("no elements given (elements.size()==0)");
        long startTime = System.currentTimeMillis();
        setProgress(0d);
        // map
        final List<KeyValue<K, V>> keyValueList = Collections.synchronizedList(new ArrayList<KeyValue<K, V>>());
        List<Callable<Void>> mapJobs = new ArrayList<Callable<Void>>(elements.size());
        for (final T element : elements) {
            mapJobs.add(new Callable<Void>() {
                public Void call() throws Exception {
                    List<KeyValue<K, V>> mapList = mapReduce.map(element);
                    if (mapList == null) throw new Exception("map step returned null");
                    //if (mapList.size()==0) throw new InterruptedException("map step returned empty list");
                    synchronized (keyValueList) {
                        keyValueList.addAll(mapList);
                    }
                    return null;
                }
            });
        }
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(maxThreads, mapJobs.size()));
        List<Future<Void>> resList = executorService.invokeAll(mapJobs);
        // runtime exception in map step?
        for (Future<Void> res : resList) {
            res.get();
        }
        executorService.shutdown();

        setProgress(50d);
        // reduce
        final Map<K, List<V>> results = new ConcurrentHashMap<K, List<V>>();
        for (KeyValue<K, V> keyValue : keyValueList) {
            if (!results.containsKey(keyValue.getKey())) {
                results.put(keyValue.getKey(), new ArrayList<V>());
            }
            results.get(keyValue.getKey()).add(keyValue.getValue());
        }


        final Map<K, V> reduced = new ConcurrentHashMap<K, V>();
        // map without results?
        if (results.keySet().size() == 0) {
            System.out.println("Warning: Map step returned no results");
            setProgress(100d);
            return reduced;
        }

        setProgress(60d);
        List<Callable<Void>> reduceJobs = new ArrayList<Callable<Void>>(results.keySet().size());
        for (final K key : results.keySet()) {
            reduceJobs.add(new Callable<Void>() {
                public Void call() throws Exception {
                    reduced.put(key, mapReduce.reduce(key, results.get(key)));
                    return null;
                }
            });
        }
        executorService = Executors.newFixedThreadPool(Math.min(maxThreads, reduceJobs.size()));
        List<Future<Void>> redList = executorService.invokeAll(reduceJobs);
        // runtime exception in reduce step?
        for (Future<Void> res : redList) {
            res.get();
        }
        executorService.shutdown();

        setProgress(100d);
        double usedTime = (System.currentTimeMillis() - startTime) / 1000d;
        System.out.println("Used time: " + usedTime + " s");
        return reduced;
    }

    public double getProgress() {
        return progress;
    }

    protected void setProgress(double progress) {
        this.progress = progress;
    }

    public void cancel() {
        // not implemented
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }


}
