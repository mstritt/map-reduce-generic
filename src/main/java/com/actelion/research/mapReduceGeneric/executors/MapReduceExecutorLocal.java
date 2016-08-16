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

/**
 * Executes mapReduce tasks, singe core. This is the simplest implementation of a mapReduce executor and perfect for testing.
 *
 * @param <T> Type Input (e.g. Integer for IDs)
 * @param <K> Output Key (measurement identifier)
 * @param <V> Output Value (e.g. Integer for a count)
 */
public class MapReduceExecutorLocal<T, K, V> implements IMapReduceExecutor<T, K, V> {

    private double progress = 0d;

    public Map<K, V> execute(final Collection<T> elements, final IMapReduce<T, K, V> mapReduce) {
        long startTime = System.currentTimeMillis();
        setProgress(0d);
        // map
        List<KeyValue<K, V>> keyValueList = new ArrayList<KeyValue<K, V>>();
        for (T element : elements) {
            keyValueList.addAll(mapReduce.map(element));
        }

        setProgress(50d);
        // reduce
        Map<K, List<V>> results = new HashMap<K, List<V>>();
        for (KeyValue<K, V> keyValue : keyValueList) {
            if (!results.containsKey(keyValue.getKey())) {
                results.put(keyValue.getKey(), new ArrayList<V>());
            }
            results.get(keyValue.getKey()).add(keyValue.getValue());
        }

        setProgress(60d);
        final Map<K, V> reduced = new HashMap<K, V>();

        // map without results?
        if (results.keySet().size() == 0) {
            System.out.println("Warning: Map step returned no results");
            setProgress(100d);
            return reduced;
        }

        for (K key : results.keySet()) {
            reduced.put(key, mapReduce.reduce(key, results.get(key)));
        }

        setProgress(100d);
        double usedTime = (System.currentTimeMillis() - startTime) / 1000d;
        System.out.println("Used time: " + usedTime + " s");
        return reduced;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public void cancel() {
        // not implemented
    }

}
