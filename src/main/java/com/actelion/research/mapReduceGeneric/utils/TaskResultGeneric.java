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

package com.actelion.research.mapReduceGeneric.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * Generic result object to store the result map together with the (e.g. map reduce) class name so that the
 * results can be interpreted with an e.g. ResultProducer.
 */
public class TaskResultGeneric implements Serializable {

    private String className;
    private Map results;
    private long numericId;
    private String stringId;
    private int status;

    public TaskResultGeneric() {
    }

    public TaskResultGeneric(String className, Map results, long numericId, String stringId, int status) {
        this.className = className;
        this.results = results;
        this.numericId = numericId;
        this.stringId = stringId;
        this.status = status;
    }

    @Override
    public String toString() {
        return "TaskResultGeneric{" +
                "className='" + className + '\'' +
                ", numericId=" + numericId +
                ", stringId='" + stringId + '\'' +
                ", status=" + status +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskResultGeneric that = (TaskResultGeneric) o;

        if (numericId != that.numericId) return false;
        if (status != that.status) return false;
        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        return stringId != null ? stringId.equals(that.stringId) : that.stringId == null;

    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (int) (numericId ^ (numericId >>> 32));
        result = 31 * result + (stringId != null ? stringId.hashCode() : 0);
        result = 31 * result + status;
        return result;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map getResults() {
        return results;
    }

    public void setResults(Map results) {
        this.results = results;
    }

    public long getNumericId() {
        return numericId;
    }

    public void setNumericId(long numericId) {
        this.numericId = numericId;
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
