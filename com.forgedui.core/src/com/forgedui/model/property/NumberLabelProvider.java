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

import org.eclipse.jface.viewers.LabelProvider;

/**
 * This will format numbers in locale dependent format. (e.g. 3,000 instead of
 * 3000)
 */
public class NumberLabelProvider extends LabelProvider {
    NumberFormat fFormatter = NumberFormat.getInstance();

    @Override
    public String getText(Object element) {
        if (element instanceof Number) {
            return this.fFormatter.format(element);
        }

        return super.getText(element);
    }

}