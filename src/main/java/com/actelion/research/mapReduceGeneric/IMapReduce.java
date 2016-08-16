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

package com.actelion.research.mapReduceGeneric;

import com.actelion.research.mapReduceGeneric.utils.KeyValue;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Interface for MapReduce tasks. Use a MapReduceExecutor* implementation to execute such a task.
 *
 * @param <T> Type Input (e.g. Integer for IDs)
 * @param <K> Output Key (measurement identifier)
 * @param <V> Output Value (e.g. Integer for a count)
 */
public interface IMapReduce<T, K, V> extends Serializable {
    List<KeyValue<K, V>> map(T element);

    V reduce(K key, List<V> valueList);

    Collection<T> parseParams(String s);

    String serializeParam(T element);
}
