/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.netbeans.lib.profiler.charts;

/**
 *
 * @author Jiri Sedlacek
 */
public interface ChartConfigurationListener {

    // Called immediately after the change
    public void offsetChanged(long oldOffsetX, long oldOffsetY,
                              long newOffsetX, long newOffsetY);

    // Called immediately after the change
    public void scaleChanged(double oldScaleX, double oldScaleY,
                             double newScaleX, double newScaleY);

    // Called immediately after the change
    public void dataBoundsChanged(long dataOffsetX, long dataOffsetY,
                                  long dataWidth, long dataHeight,
                                  long oldDataOffsetX, long oldDataOffsetY,
                                  long oldDataWidth, long oldDataHeight);

    // Called before paintContents(Graphics, Rectangle)
    public void contentsWillBeUpdated(long offsetX, long offsetY,
                            double scaleX, double scaleY,
                            long lastOffsetX, long lastOffsetY,
                            double lastScaleX, double lastScaleY);

    // Called after paintContents(Graphics, Rectangle)
    // The actual change may/not be already displayed depending on buffer type
    public void contentsUpdated(long offsetX, long offsetY,
                            double scaleX, double scaleY,
                            long lastOffsetX, long lastOffsetY,
                            double lastScaleX, double lastScaleY,
                            int shiftX, int shiftY);


    public abstract class Adapter implements ChartConfigurationListener {
        
        public void offsetChanged(long oldOffsetX, long oldOffsetY,
                                  long newOffsetX, long newOffsetY) {}

        public void scaleChanged(double oldScaleX, double oldScaleY,
                                 double newScaleX, double newScaleY) {}

        public void dataBoundsChanged(long dataOffsetX, long dataOffsetY,
                                      long dataWidth, long dataHeight,
                                      long oldDataOffsetX, long oldDataOffsetY,
                                      long oldDataWidth, long oldDataHeight) {}

        public void contentsWillBeUpdated(long offsetX, long offsetY,
                                double scaleX, double scaleY,
                                long lastOffsetX, long lastOffsetY,
                                double lastScaleX, double lastScaleY) {}

        public void contentsUpdated(long offsetX, long offsetY,
                                double scaleX, double scaleY,
                                long lastOffsetX, long lastOffsetY,
                                double lastScaleX, double lastScaleY,
                                int shiftX, int shiftY) {}

    }

}
