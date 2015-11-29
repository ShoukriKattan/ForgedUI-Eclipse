package com.forgedui.model.property;

/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * Validates the number to verify if it is within the bounds specified for this
 * validator. You can specify a min only, a max only, or a min and max. the min
 * and max will be included in the range. I.e. if equal to the min or max, then
 * the answer will be true.
 * 
 * Null is considered valid, if null is not wanted, then another validator needs
 * to check for this.
 * 
 * Note: This won't appropriately handle Big... numbers. They can be bigger than
 * a double value so the test would be inaccurate.
 */
public class MinmaxValidator implements ICellEditorValidator, IExecutableExtension {
    public static final String MIN_ONLY = "minonly", // $NON-NLS-1$ //$NON-NLS-1$
            MAX_ONLY = "maxonly"; // $NON-NLS-1$ //$NON-NLS-1$

    public static final Long LONG_UNDERFLOW = new Long(-42L);
    public static final Long LONG_OVERFLOW = new Long(42L);

    protected static final MessageFormat sMinMaxError, sMinError, sMaxError;
    protected static final String sNotNumberError;
    static {
        sMinMaxError = new MessageFormat("The value is not between {0} and {1}");
        sMinError = new MessageFormat("The value is not greater than or equal to {0}");
        sMaxError = new MessageFormat("The value is not less than or equal to {0} ");
        sNotNumberError = "The value is not a number.";
    }

    protected static final NumberFormat sNumberFormat;
    static {
        sNumberFormat = NumberFormat.getInstance();
        sNumberFormat.setMaximumFractionDigits(8);
    }

    protected String fType = null; // Min/max/ or both

    protected Number fMinValue, fMaxValue;

    /**
     * Default to min/max of Integer type.
     */
    public MinmaxValidator() {
        this(new Long(Integer.MIN_VALUE), new Long(Integer.MAX_VALUE));
    }

    public MinmaxValidator(int min, int max) {
        this(new Long(min), new Long(max));
    }

    /**
     * The type will be one the two above to indicate setting min only or max
     * only.
     */
    public MinmaxValidator(int in, String type) {
        this(new Long(in), type);
    }

    public MinmaxValidator(Number min, Number max) {
        setMinMax(min, max);
    }

    /**
     * The type will be one the two above to indicate setting min only or max
     * only.
     */
    public MinmaxValidator(Number in, String type) {
        setOnly(in, type);
    }

    public void setMinMax(int min, int max) {
        setMinMax(new Long(min), new Long(max));
    }

    /**
     * This will only expect initData to be a string. The string should be: a)
     * min: minvalue b) max: maxvalue c) minvalue, maxvalue
     * 
     * e.g.: min: 3 3, 10
     */
    @Override
    public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
        if (initData instanceof String) {
            StringTokenizer st = new StringTokenizer((String) initData, ":,", true); //$NON-NLS-1$
            String s = null;
            if (st.hasMoreTokens())
                s = st.nextToken();
            if ("min".equalsIgnoreCase(s)) { //$NON-NLS-1$
                if (!st.hasMoreTokens())
                    return; // Invalid format;
                s = st.nextToken();
                if (!st.hasMoreTokens())
                    return; // Invalid format;
                s = st.nextToken();
                try {
                    Number min = sNumberFormat.parse(s);
                    setOnly(min, MIN_ONLY);
                } catch (ParseException e) {
                }
            } else if ("max".equalsIgnoreCase(s)) { //$NON-NLS-1$
                try {
                    if (!st.hasMoreTokens())
                        return; // Invalid format;
                    s = st.nextToken();
                    if (!st.hasMoreTokens())
                        return; // Invalid format;
                    s = st.nextToken();
                    Number max = sNumberFormat.parse(s);
                    setOnly(max, MAX_ONLY);
                } catch (ParseException e) {
                }
            } else {
                try {
                    // Should be number, number
                    Number min = sNumberFormat.parse(s);
                    if (!st.hasMoreTokens())
                        return; // Invalid format;
                    s = st.nextToken();
                    if (!st.hasMoreTokens())
                        return; // Invalid format;
                    s = st.nextToken();
                    Number max = sNumberFormat.parse(s);
                    setMinMax(min, max);
                } catch (ParseException e) {
                }
            }
        }
    }

    public void setMinMax(Number min, Number max) {
        this.fType = null;
        this.fMinValue = min;
        this.fMaxValue = max;
    }

    /**
     * The type will be one the two above to indicate setting min only or max
     * only.
     */
    public void setOnly(int in, String type) {
        setOnly(new Long(in), type);
    }

    public void setOnly(Number in, String type) {
        if (MIN_ONLY.equals(type)) {
            this.fType = MIN_ONLY;
            this.fMinValue = in;
        } else {
            this.fType = MAX_ONLY;
            this.fMaxValue = in;
        }
    }

    @Override
    public String isValid(Object value) {
        if (value instanceof Number) {
            // Check for out of bounds Long values
            if (value == LONG_UNDERFLOW || value == LONG_OVERFLOW) {
                if (this.fType == null)
                    return sMinMaxError.format(new Object[] { sNumberFormat.format(this.fMinValue.longValue()), sNumberFormat.format(this.fMaxValue.longValue()) });
                else if (this.fType == MIN_ONLY)
                    return (value != LONG_UNDERFLOW) ? null : sMinError.format(new Object[] { sNumberFormat.format(this.fMinValue.longValue()) });
                else
                    return (value != LONG_OVERFLOW) ? null : sMaxError.format(new Object[] { sNumberFormat.format(this.fMaxValue.longValue()) });
            }
            // We need to know whether a floating or integer because long has
            // more precision
            // than double so we need to do comparisons as either double or
            // longs.
            if (!(value instanceof Double || value instanceof Float)) {
                // It is an integer type value
                long l = ((Number) value).longValue();

                if (this.fType == null)
                    return (this.fMinValue.longValue() <= l && l <= this.fMaxValue.longValue()) ? null : sMinMaxError.format(new Object[] { sNumberFormat.format(this.fMinValue.longValue()), sNumberFormat.format(this.fMaxValue.longValue()) });
                else if (this.fType == MIN_ONLY)
                    return (this.fMinValue.longValue() <= l) ? null : sMinError.format(new Object[] { sNumberFormat.format(this.fMinValue.longValue()) });
                else
                    return (l <= this.fMaxValue.longValue()) ? null : sMaxError.format(new Object[] { sNumberFormat.format(this.fMaxValue.longValue()) });
            }
            // It is a floating type value
            double d = ((Number) value).doubleValue();
            if (this.fType == null)
                return (this.fMinValue.doubleValue() <= d && d <= this.fMaxValue.doubleValue()) ? null : sMinMaxError.format(new Object[] { sNumberFormat.format(this.fMinValue.doubleValue()), sNumberFormat.format(this.fMaxValue.doubleValue()) });
            else if (this.fType == MIN_ONLY)
                return (this.fMinValue.doubleValue() <= d) ? null : sMinError.format(new Object[] { sNumberFormat.format(this.fMinValue.doubleValue()) });
            else
                return (d <= this.fMaxValue.doubleValue()) ? null : sMaxError.format(new Object[] { sNumberFormat.format(this.fMaxValue.doubleValue()) });
        } else if (value != null)
            return sNotNumberError;
        else
            return null;
    }

}