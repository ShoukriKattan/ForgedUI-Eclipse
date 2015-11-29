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

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.swt.widgets.Composite;

/**
 * Number Celleditor that formats according to the current locale. It can also
 * handle a null by allowing null to come in. It will be considered invalid but
 * it won't bomb.
 * 
 * It will format in locale dependent format (e.g. 3,000 instead of 3000).
 * 
 * Note: Big... (e.g. BigInteger) is not supported by this editor.
 */
public class NumberCellEditor extends ObjectCellEditor implements IExecutableExtension {

    private static final String NULL_VALUE_ERROR = "Null value is not allowed";

	protected static final Short MAX_SHORT = new Short(Short.MAX_VALUE);

    protected static final Short MIN_SHORT = new Short(Short.MIN_VALUE);

    protected static final Long MAX_LONG = new Long(Long.MAX_VALUE);

    protected static final Long MIN_LONG = new Long(Long.MIN_VALUE);

    protected static final Integer MAX_INTEGER = new Integer(Integer.MAX_VALUE);

    protected static final Integer MIN_INTEGER = new Integer(Integer.MIN_VALUE);

    protected static final Float MAX_FLOAT = new Float(Float.MAX_VALUE);

    protected static final Float MIN_FLOAT = new Float(Float.MIN_VALUE);

    protected static final Double MAX_DOUBLE = new Double(Double.MAX_VALUE);

    protected static final Double MIN_DOUBLE = new Double(Double.MIN_VALUE);

    protected static final Byte MAX_BYTE = new Byte(Byte.MAX_VALUE);

    protected static final Byte MIN_BYTE = new Byte(Byte.MIN_VALUE);

    public static final int
    // Type of number to be returned.
            NUMBER = 0, // Whatever it produces
            BYTE = 1, DOUBLE = 2, FLOAT = 3, INTEGER = 4, LONG = 5, SHORT = 6;

    protected static final MinmaxValidator[] sMinMaxValidators = { null, new MinmaxValidator(MIN_BYTE, MAX_BYTE), new MinmaxValidator(MIN_DOUBLE, MAX_DOUBLE), new MinmaxValidator(MIN_FLOAT, MAX_FLOAT), new MinmaxValidator(MIN_INTEGER, MAX_INTEGER), new MinmaxValidator(MIN_LONG, MAX_LONG), new MinmaxValidator(MIN_SHORT, MAX_SHORT) };

    // TODO I18N
    protected static final String sNotNumberError = "The value is not a number.";
    protected static final String sNotIntegerError = "The value is not an integer.";
    protected static final String sMinValue = "Min";
    protected static final String sMaxValue = "Max";

    // Used to parse numbers in scientific notation as the default float and
    // double parsers do not do a very good job of it.
    protected NumberFormat fFormatter = NumberFormat.getInstance(Locale.US);

    protected int fNumberType = NUMBER;
    private boolean allowNull;
    
    public NumberCellEditor(Composite parent, boolean allowNull) {
        super(parent);
        this.allowNull = allowNull;
        this.fFormatter.setMaximumFractionDigits(20);
        this.fFormatter.setMaximumIntegerDigits(20);
    }

    /**
     * This will only expect initData to be a string. The string should be the
     * type, a) integer b) long c) etc.
     * 
     * number is the default.
     */
    @Override
    public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
        if (initData instanceof String) {
            String type = ((String) initData).trim();
            if ("byte".equalsIgnoreCase(type)) { //$NON-NLS-1$
                setType(BYTE);
            } else if ("double".equalsIgnoreCase(type)) { //$NON-NLS-1$
                setType(DOUBLE);
            } else if ("float".equalsIgnoreCase(type)) { //$NON-NLS-1$
                setType(FLOAT);
            } else if ("integer".equalsIgnoreCase(type)) { //$NON-NLS-1$
                setType(INTEGER);
            } else if ("long".equalsIgnoreCase(type)) { //$NON-NLS-1$
                setType(LONG);
            } else if ("short".equalsIgnoreCase(type)) { //$NON-NLS-1$
                setType(SHORT);
            }
        }
    }

    public void setType(int type) {
        switch (type) {
        case NUMBER:
        case DOUBLE:
        case FLOAT:
            this.fFormatter.setParseIntegerOnly(false);
            break;
        case BYTE:
        case INTEGER:
        case LONG:
        case SHORT:
            this.fFormatter.setParseIntegerOnly(true);
            break;
        default:
            return; // Invalid type, do nothing
        }

        this.fNumberType = type;
    }

    @Override
    protected String isCorrectObject(Object value) {
    	if (value instanceof Number){
    		return null;
    	} else if (value == null){
    		return this.allowNull ? null : NULL_VALUE_ERROR;
    	} else {
    		return sNotNumberError;
    	}
    }

    @Override
    protected String isCorrectString(String value) {
        String text = value.trim();
        
        if (text.length() == 0){
        	return this.allowNull ? null : NULL_VALUE_ERROR;
        }

        if (sMinValue.equalsIgnoreCase(text) || sMaxValue.equalsIgnoreCase(text))
            return null;

        Number result = null;
        if ((this.fNumberType == DOUBLE || this.fNumberType == FLOAT) && (text.indexOf('e') != -1 || text.indexOf('E') != -1)) {
            // We have a double/float with an exponent. This is scientific
            // notation. Formatter handles them badly, so use parse instead.
            try {
                if (this.fNumberType == DOUBLE) {
                    result = new Double(Double.parseDouble(text));
                } else {
                    result = new Float(Float.parseFloat(text));
                }
            } catch (NumberFormatException e) {
            }
        } else {
            // integral or not scientific notation. Let formatter handle it.
            ParsePosition parsePosition = new ParsePosition(0);
            result = this.fFormatter.parse(text, parsePosition);
            if (parsePosition.getErrorIndex() != -1 || parsePosition.getIndex() != text.length())
                result = null; // Some error
            // Check for out of bounds with long type
            if (this.fNumberType == LONG && result instanceof Double) {
                result = (result.doubleValue() < 0) ? MinmaxValidator.LONG_UNDERFLOW : MinmaxValidator.LONG_OVERFLOW;
            }
        }

        if (result != null) {
            // Now see if it is valid for the requested type.
            MinmaxValidator v = sMinMaxValidators[this.fNumberType];
            // Double/Float are special because the min/MIN are on the absolute
            // value, not signed value.
            if (this.fNumberType == DOUBLE || this.fNumberType == FLOAT) {
                double d = result.doubleValue();
                if (d == 0.0 || d == -0.0)
                    return null; // +/- zero are valid values.
                result = new Double(Math.abs(d));
            }
            if (v != null) {
                String e = v.isValid(result);
                if (e == null || e.length() == 0)
                    return null;
                return e; // It didn't fit in a the number type.
            }
        }
        return (this.fFormatter.isParseIntegerOnly() ? sNotIntegerError : sNotNumberError);
    }

    /**
     * Return the object that the string represents.
     */
    @Override
    protected Object doGetObject(String v) {
        if (v == null || v.length() == 0) {
            return null;
        }
        if (sMaxValue.equalsIgnoreCase(v)) {
            switch (this.fNumberType) {
            case BYTE:
                return MAX_BYTE;
            case DOUBLE:
                return MAX_DOUBLE;
            case FLOAT:
                return MAX_FLOAT;
            case INTEGER:
                return MAX_INTEGER;
            case LONG:
                return MAX_LONG;
            case SHORT:
                return MAX_SHORT;
            default:
                return null;
            }
        } else if (sMinValue.equalsIgnoreCase(v)) {
            switch (this.fNumberType) {
            case BYTE:
                return MIN_BYTE;
            case DOUBLE:
                return MIN_DOUBLE;
            case FLOAT:
                return MIN_FLOAT;
            case INTEGER:
                return MIN_INTEGER;
            case LONG:
                return MIN_LONG;
            case SHORT:
                return MIN_SHORT;
            default:
                return null;
            }
        }
        try {
            // Float and Double are done separately because parseFloat and
            // parseDouble can result in different values when casting a
            // double back to a float.
            switch (this.fNumberType) {
            case BYTE:
                return this.fFormatter.parse(v).byteValue();
            case DOUBLE:
                // Check for scientific notation.
                return v.indexOf('E') == -1 && v.indexOf('e') == -1 ? this.fFormatter.parse(v).doubleValue() : Double.parseDouble(v);
            case FLOAT:
                // Check for scientific notation.
                return v.indexOf('E') == -1 && v.indexOf('e') == -1 ? this.fFormatter.parse(v).floatValue() : Float.parseFloat(v);
            case INTEGER:
                return this.fFormatter.parse(v).intValue();
            case LONG:
                return this.fFormatter.parse(v).longValue();
            case SHORT:
                return this.fFormatter.parse(v).shortValue();
            default:
                return null;
            }
        } catch (ParseException e) {
            // We should never get here because we have already tested for
            // validity.
            throw new IllegalArgumentException();
        }
    }

    /**
     * Return the string for the object passed in.
     */
    @SuppressWarnings("fallthrough")
    @Override
    protected String doGetString(Object value) {
        if (!(value instanceof Number)) {
            if (value instanceof String) {
            	return (String)value;
            }
            return null;
        }
        Number num = (Number) value;
        switch (this.fNumberType) {
        case BYTE:
            if (num.byteValue() == Byte.MAX_VALUE) {
                return sMaxValue;
            }
            if (num.byteValue() == Byte.MIN_VALUE) {
                return sMinValue;
            }
            break;
        case DOUBLE:
            if (num.doubleValue() == Double.MAX_VALUE) {
                return sMaxValue;
            }
            if (num.doubleValue() == Double.MIN_VALUE) {
                return sMinValue;
            }
            // intentionally fall through
        case FLOAT:
            if (this.fNumberType == FLOAT) {
                if (num.floatValue() == Float.MAX_VALUE) {
                    return sMaxValue;
                }
                if (num.floatValue() == Float.MIN_VALUE) {
                    return sMinValue;
                }
            }
            // The formatter doesn't handle big/small floats. (i.e. more than
            // the MIN digits we set).
            // It doesn't go to scientific notation as necessary. The only way
            // to test this is to
            // roundtrip the number. If they come up to be the same, then it is
            // ok. Else format using
            // toString.
            try {
                String s = this.fFormatter.format(value);
                return this.fFormatter.parse(s).doubleValue() != num.doubleValue() ? value.toString() : s;
            } catch (ParseException e) {
                return value.toString();
            }
        case INTEGER:
            if (num.intValue() == Integer.MAX_VALUE) {
                return sMaxValue;
            }
            if (num.intValue() == Integer.MIN_VALUE) {
                return sMinValue;
            }
            break;
        case LONG:
            if (num.longValue() == Long.MAX_VALUE) {
                return sMaxValue;
            }
            if (num.longValue() == Long.MIN_VALUE) {
                return sMinValue;
            }
            break;
        case SHORT:
            if (num.shortValue() == Short.MAX_VALUE) {
                return sMaxValue;
            }
            if (num.shortValue() == Short.MIN_VALUE) {
                return sMinValue;
            }
            break;
        default:
            break;
        }
        return this.fFormatter.format(value);
    }

}