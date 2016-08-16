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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.io.File.separator;

public class Helpers {

    public static Collection<Integer> parseParamsInt(String s) {
        List<Integer> intList = new ArrayList<Integer>();
        if (s != null && s.length() > 0) {
            if (s.contains(";")) {
                String[] split = s.split(separator);
                if (split != null && split.length > 0) {
                    for (String el : split) {
                        intList.add(Integer.parseInt(el));
                    }
                }
            } else {
                intList.add(Integer.parseInt(s));
            }
        }
        return intList;
    }

    public static String serializeParamInt(Integer element) {
        return element.toString();
    }


    public static Collection<Long> parseParamsLong(String s) {
        List<Long> longList = new ArrayList<Long>();
        if (s != null && s.length() > 0) {
            if (s.contains(";")) {
                String[] split = s.split(separator);
                if (split != null && split.length > 0) {
                    for (String el : split) {
                        longList.add(Long.parseLong(el));
                    }
                }
            } else {
                longList.add(Long.parseLong(s));
            }
        }
        return longList;
    }

    public static String serializeParamLong(Long element) {
        return element.toString();
    }

    public static Collection<Double> parseParamsDouble(String s) {
        List<Double> doubleList = new ArrayList<Double>();
        if (s != null && s.length() > 0) {
            if (s.contains(";")) {
                String[] split = s.split(separator);
                if (split != null && split.length > 0) {
                    for (String el : split) {
                        doubleList.add(Double.parseDouble(el));
                    }
                }
            } else {
                doubleList.add(Double.parseDouble(s));
            }
        }
        return doubleList;
    }

    public static String serializeParamDouble(Double element) {
        return element.toString();
    }


    public static Collection<String> parseParamsString(String s) {
        List<String> stringList = new ArrayList<String>();
        if (s != null && s.length() > 0) {
            if (s.contains(";")) {
                String[] split = s.split(separator);
                if (split != null && split.length > 0) {
                    for (String el : split) {
                        stringList.add(el);
                    }
                }
            } else {
                stringList.add(s);
            }
        }
        return stringList;
    }


}
