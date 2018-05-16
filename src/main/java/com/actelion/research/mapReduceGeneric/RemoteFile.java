/*
 * Copyright 1997-2018 Idorsia Ltd.
 * Hegenheimermattweg 89
 * CH-4123 Allschwil, Switzerland
 *
 * All Rights Reserved.
 * This software is the proprietary information of Idorsia Pharmaceuticals, Ltd.
 * Use is subject to license terms.
 *
 * Author: Manuel Stritt
 * Date: 4/19/18 4:43 PM
 */

package com.actelion.research.mapReduceGeneric;

import java.io.Serializable;
import java.util.Objects;

public class RemoteFile implements Serializable {
    private String name;
    private long date;
    private long length;
    private String user;

    public RemoteFile(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RemoteFile{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", length=" + length +
                ", user='" + user + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteFile that = (RemoteFile) o;
        return date == that.date &&
                length == that.length &&
                Objects.equals(name, that.name) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, date, length, user);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
