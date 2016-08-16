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

package com.actelion.research;

import com.actelion.research.mapReduceGeneric.examples.ComputeSum;
import com.actelion.research.mapReduceGeneric.executors.IMapReduceExecutor;
import com.actelion.research.mapReduceGeneric.executors.MapReduceExecutorLocal;
import com.actelion.research.mapReduceGeneric.executors.MapReduceExecutorLocalMultiCore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class testComputeSum {

    private ComputeSum mapRed = new ComputeSum();

    @Test
    public void testComputesumSingleThread() throws Exception {
        IMapReduceExecutor<Integer, String, Long> executorGrid = new MapReduceExecutorLocal<Integer, String, Long>();
        Map<String, Long> reduced = executorGrid.execute(Arrays.asList(1, 2, 3, 4, 5), mapRed);
        for (String key : reduced.keySet()) {
            System.out.println("res " + key + ": " + reduced.get(key));
        }
        assertEquals(120L, (long) reduced.get("mult"));
        assertEquals(15L, (long) reduced.get("sum"));
    }

    @Test
    public void testComputesumMultiThread() throws Exception {
        IMapReduceExecutor<Integer, String, Long> executorGrid = new MapReduceExecutorLocalMultiCore<Integer, String, Long>();
        Map<String, Long> reduced = executorGrid.execute(Arrays.asList(1, 2, 3, 4, 5), mapRed);
        for (String key : reduced.keySet()) {
            System.out.println("res " + key + ": " + reduced.get(key));
        }
        assertEquals(120L, (long) reduced.get("mult"));
        assertEquals(15L, (long) reduced.get("sum"));
    }


}
