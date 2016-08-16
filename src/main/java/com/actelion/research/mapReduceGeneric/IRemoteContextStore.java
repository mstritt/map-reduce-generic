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

import java.io.IOException;
import java.util.List;

/**
 * To persist and read data in a distributed environment.
 * The idea is to write the data somewhere where all clients can read it. A possible implementation is a samba share or HDFS.
 */
public interface IRemoteContextStore {
    void copyToRemote(byte[] bytes, String remoteFolder, String fileNameNew) throws IOException;

    String generateUniqueFilename(String prefix, String ending);  // e.g. via UUID

    byte[] readFromRemote(String remoteFile) throws IOException;

    List<String> listFilenames(String remoteFolder) throws IOException;
}
