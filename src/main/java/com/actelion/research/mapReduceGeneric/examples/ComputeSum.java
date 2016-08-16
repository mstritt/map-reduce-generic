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

package com.actelion.research.mapReduceGeneric.examples;

import com.actelion.research.mapReduceGeneric.IMapReduce;
import com.actelion.research.mapReduceGeneric.executors.IMapReduceExecutor;
import com.actelion.research.mapReduceGeneric.executors.MapReduceExecutorLocalMultiCore;
import com.actelion.research.mapReduceGeneric.utils.Helpers;
import com.actelion.research.mapReduceGeneric.utils.KeyValue;

import java.util.*;

/**
 * Computes the sum and the product of a set of numbers. Demonstrates how to compute several key/value pairs.
 */
public class ComputeSum implements IMapReduce<Integer, String, Long> {
    public List<KeyValue<String, Long>> map(Integer element) {
        List<KeyValue<String, Long>> list = new ArrayList<KeyValue<String, Long>>();
        list.add(new KeyValue<String, Long>("sum", (long) element));
        list.add(new KeyValue<String, Long>("mult", (long) element));
        return list;
    }

    public Long reduce(String key, List<Long> valueList) {
        Long res = 0L;
        if (key.equals("mult")) res = 1L;
        for (Long v : valueList) {
            if (key.equals("sum")) {
                res += v;
            } else if (key.equals("mult")) {
                res *= v;
            }
        }
        return res;
    }

    public Collection<Integer> parseParams(String s) {
        return Helpers.parseParamsInt(s);
    }

    public String serializeParam(Integer element) {
        return Helpers.serializeParamInt(element);
    }

    public static void main(String[] args) throws Exception {
        IMapReduce<Integer, String, Long> mapReduce = new ComputeSum();
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        //IMapReduceExecutor<Integer,String,Long> executor = new MapReduceExecutorLocal<Integer,String,Long>();
        IMapReduceExecutor<Integer, String, Long> executor = new MapReduceExecutorLocalMultiCore<Integer, String, Long>();

        Map<String, Long> resultMap = executor.execute(list, mapReduce);
        for (String key : resultMap.keySet()) {
            System.out.println(key + ": " + resultMap.get(key));
        }
    }

}
