package com.qcadoo.mes.core.data.internal.types;

import java.math.BigDecimal;

import com.qcadoo.mes.core.data.definition.DataFieldDefinition;
import com.qcadoo.mes.core.data.types.FieldType;
import com.qcadoo.mes.core.data.validation.ValidationResults;

public final class DecimalType implements FieldType {

    @Override
    public boolean isSearchable() {
        return true;
    }

    @Override
    public boolean isOrderable() {
        return true;
    }

    @Override
    public boolean isAggregable() {
        return true;
    }

    @Override
    public Class<?> getType() {
        return BigDecimal.class;
    }

    @Override
    public Object toObject(final DataFieldDefinition fieldDefinition, final Object value,
            final ValidationResults validationResults) {
        BigDecimal decimal = null;

        if (value instanceof BigDecimal) {
            decimal = (BigDecimal) value;
        } else {
            try {
                decimal = new BigDecimal(String.valueOf(value));
            } catch (NumberFormatException e) {
                validationResults.addError(fieldDefinition, "commons.validate.field.error.invalidNumericFormat");
                return null;
            }
        }
        if (decimal.precision() > 7 || decimal.scale() > 3) {
            validationResults.addError(fieldDefinition, "commons.validate.field.error.invalidNumericFormat");
            return null;
        }
        return decimal;
    }

    @Override
    public String toString(final Object value) {
        return String.valueOf(value);
    }

}
