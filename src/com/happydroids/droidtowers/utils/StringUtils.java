/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.droidtowers.utils;

import com.happydroids.droidtowers.TowerConsts;

import java.text.NumberFormat;

public class StringUtils {
	public static CharSequence formatNumber(long i) {
		float value = i;
		String suffix;
		if (value >= 1000000000) {
			value = value / 1000000000f;
			suffix = "b";
		} else if (value >= 1000000) {
			value = value / 1000000f;
			suffix = "m";
		} else if (value > 1000) {
			value = i / 1000f;
			suffix = "k";
		} else {
			return String.valueOf(i);
		}

		return String.format("%.1f%s", value, suffix);
	}

	public static CharSequence formatNumber(double i) {
		return NumberFormat.getInstance().format(i);
	}

	public static String currencyFormat(int value) {
		return TowerConsts.CURRENCY_SYMBOL
				+ NumberFormat.getInstance().format(value);
	}

	public static String wrap(String text, int wrapAt) {
		if (text.length() <= wrapAt) {
			return text;
		}

		StringBuilder output = new StringBuilder();
		int lineBegin = 0;
		int lineEnd = text.indexOf(" ", Math.min(wrapAt, text.length()));
		if (lineEnd != -1) {
			do {
				output.append(text.substring(lineBegin, lineEnd));
				output.append("\n");
				lineBegin = lineEnd + 1;
				lineEnd = text.indexOf(' ',
						Math.min(lineBegin + wrapAt, text.length()));
			} while (lineBegin < text.length() && lineEnd != -1);
		}

		output.append(text.substring(lineBegin));
		output.append("\n");

		return output.toString().trim();
	}

	public static String truncate(String title, int finalSize) {
		if (title.length() <= finalSize) {
			return title;
		}

		return title.substring(0, finalSize - 3) + "...";
	}
	
	public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
	
	
	/*
	 * Licensed to the Apache Software Foundation (ASF) under one or more
	 * contributor license agreements.  See the NOTICE file distributed with
	 * this work for additional information regarding copyright ownership.
	 * The ASF licenses this file to You under the Apache License, Version 2.0
	 * (the "License"); you may not use this file except in compliance with
	 * the License.  You may obtain a copy of the License at
	 *
	 *      http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 */
    /**
     * This is from the org.apache.commons.lang3 package class StringUtils.java
     * 
     * <p>Joins the elements of the provided array into a single String
     * containing the provided list of elements.</p>
     *
     * <p>No delimiter is added before or after the list.
     * A {@code null} separator is the same as an empty String ("").
     * Null objects or empty strings within the array are represented by
     * empty strings.</p>
     *
     * <pre>
     * StringUtils.join(null, *)                = null
     * StringUtils.join([], *)                  = ""
     * StringUtils.join([null], *)              = ""
     * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * StringUtils.join(["a", "b", "c"], null)  = "abc"
     * StringUtils.join(["a", "b", "c"], "")    = "abc"
     * StringUtils.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     *
     * @param array  the array of values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @param startIndex the first index to start joining from.  It is
     * an error to pass in an end index past the end of the array
     * @param endIndex the index to stop joining from (exclusive). It is
     * an error to pass in an end index past the end of the array
     * @return the joined String, {@code null} if null array input
     */
    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }

        // endIndex - startIndex > 0:   Len = NofStrings *(len(firstString) + len(separator))
        //           (Assuming that all Strings are roughly equally long)
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }

        StringBuilder buf = new StringBuilder(noOfItems * 16);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
    
    /**
     * The empty String {@code ""}.
     * @since 2.0
     */
    public static final String EMPTY = "";
}
