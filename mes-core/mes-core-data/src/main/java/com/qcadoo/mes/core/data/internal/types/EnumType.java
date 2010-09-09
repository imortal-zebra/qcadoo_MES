package com.qcadoo.mes.core.data.internal.types;

import java.util.Arrays;
import java.util.List;

import com.qcadoo.mes.core.data.definition.DataFieldDefinition;
import com.qcadoo.mes.core.data.types.EnumeratedFieldType;
import com.qcadoo.mes.core.data.validation.ValidationResults;

public final class EnumType implements EnumeratedFieldType {

    private final List<String> values;

    public EnumType(final String... values) {
        this.values = Arrays.asList(values);
    }

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
        return false;
    }

    @Override
    public List<String> values() {
        return values;
    }

    @Override
    public Class<?> getType() {
        return String.class;
    }

    @Override
    public Object toObject(final DataFieldDefinition fieldDefinition, final Object value,
            final ValidationResults validationResults) {
        String stringValue = String.valueOf(value);
        if (!values().contains(stringValue)) {
            validationResults.addError(fieldDefinition, "commons.validate.field.error.invalidDictionaryItem",
                    String.valueOf(values()));
            return null;
        }
        return stringValue;
    }

    @Override
    public String toString(final Object value) {
        return String.valueOf(value);
    }

}
