/*
 * Project: HPK
 *
 * Copyright (c) 2008-2013,  Prof. Dr. Nikolaus Wulff
 * University of Applied Sciences, Muenster, Germany
 * Lab for Computer Sciences (Lab4Inf).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.lab4inf.wrb;

import java.util.logging.Logger;

/**
 * Native WRB Differentiator.
 * 
 * @author  nwulff
 * @since   26.11.2013
 * @version $Id: Differentiator.java,v 1.8 2017/12/21 15:54:22 nwulff Exp $
 */
public class Differentiator {
    private double error = 1.E-8;
    static final Logger logger = java.util.logging.Logger.getLogger("de.lab4inf");
    static {
        try {
            // String libName = "HPK-CPP";
            String libName = "WRB4";
            System.loadLibrary(libName);
        } catch (Throwable error) {
            logger.severe("LibPath: "+System.getProperty("java.library.path"));
            logger.severe("native lib loading failed "+error);
            System.exit( -1);
        }
    }

    /**
     * Differentiate the function f at point x. 
     * @param f function to differentiate.
     * @param x the argument
     * @return f'(x)
     */
    public double differentiate(Function f, double x) {
        return differentiate(f, x, error);
    }

    /**
     * Differentiate the function f at point x. 
     * @param f function to differentiate.
     * @param x the argument
     * @param err the relative error to reach
     * @return f'(x)
     */
    public native double differentiate(Function f, double x, double err);

    /**
     * Get the value of error.
     * @return the error
     */
    public double getError() {
        return error;
    }

    /**
     * Set the error attribute.
     * @param error the error to set
     */
    public void setError(double error) {
        this.error = error;
    }

}
