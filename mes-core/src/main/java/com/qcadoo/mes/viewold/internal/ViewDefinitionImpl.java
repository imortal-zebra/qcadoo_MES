/**
 * ********************************************************************
 * Code developed by amazing QCADOO developers team.
 * Copyright (c) Qcadoo Limited sp. z o.o. (2010)
 * ********************************************************************
 */

package com.qcadoo.mes.viewold.internal;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.qcadoo.mes.api.Entity;
import com.qcadoo.mes.model.DataDefinition;
import com.qcadoo.mes.model.HookDefinition;
import com.qcadoo.mes.viewold.Component;
import com.qcadoo.mes.viewold.RootComponent;
import com.qcadoo.mes.viewold.ViewDefinition;
import com.qcadoo.mes.viewold.ViewValue;

public final class ViewDefinitionImpl implements ViewDefinition {

    private final String pluginIdentifier;

    private final String name;

    private HookDefinition viewHook;

    private RootComponent root;

    private boolean menuable = false;

    public ViewDefinitionImpl(final String pluginIdentifier, final String name) {
        this.name = name;
        this.pluginIdentifier = pluginIdentifier;
    }

    @Override
    public String getPluginIdentifier() {
        return pluginIdentifier;
    }

    public void setRoot(final RootComponent root) {
        this.root = root;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setViewHook(final HookDefinition viewHook) {
        this.viewHook = viewHook;
    }

    @Override
    public ViewValue<Long> castValue(final Map<String, Entity> selectedEntities, final JSONObject viewObject) {
        try {
            return wrapIntoViewValue(root.castValue(selectedEntities,
                    viewObject != null ? viewObject.getJSONObject(root.getName()) : null));
        } catch (JSONException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void cleanSelectedEntities(final Map<String, Entity> selectedEntities, final Set<String> pathsToUpdate) {
        if (selectedEntities != null) {
            for (String pathToUpdate : pathsToUpdate) {
                selectedEntities.remove(pathToUpdate);
            }
        }
    }

    @Override
    public ViewValue<Long> getValue(final Entity entity, final Map<String, Entity> selectedEntities,
            final ViewValue<Long> globalViewValue, final String triggerComponentName, final boolean saveOrDelete,
            final Locale locale) {

        Set<String> pathsToUpdate = null;

        if (triggerComponentName != null) {
            pathsToUpdate = root.lookupListeners(triggerComponentName);
            cleanSelectedEntities(selectedEntities, pathsToUpdate);
            if (saveOrDelete || pathsToUpdate.isEmpty()) {
                pathsToUpdate.add(triggerComponentName);
            }
        } else {
            pathsToUpdate = new HashSet<String>();
        }

        ViewValue<Long> value = wrapIntoViewValue(root.getValue(entity, selectedEntities,
                globalViewValue != null ? globalViewValue.getComponent(root.getName()) : null, pathsToUpdate, locale));
        if (value != null) {
            callOnViewHook(value, triggerComponentName, entity, locale);
        }
        if (entity != null) {
            value.setValue(entity.getId());
        }
        return value;
    }

    private void callOnViewHook(final ViewValue<Long> value, final String triggerComponentName, final Entity entity,
            final Locale locale) {
        if (viewHook != null) {
            // viewHook.callWithViewState(value, triggerComponentName, entity, locale);
        }
    }

    @Override
    public void updateTranslations(final Map<String, String> translationsMap, final Locale locale) {
        root.updateTranslations(translationsMap, locale);
    }

    private ViewValue<Long> wrapIntoViewValue(final ViewValue<?> viewValue) {
        ViewValue<Long> value = new ViewValue<Long>();
        value.addComponent(root.getName(), viewValue);
        return value;
    }

    @Override
    public DataDefinition getDataDefinition() {
        return root.getDataDefinition();
    }

    @Override
    public Component<?> lookupComponent(final String path) {
        return root.lookupComponent(path);
    }

    @Override
    public RootComponent getRoot() {
        return root;
    }

    @Override
    public boolean isMenuable() {
        return menuable;
    }

    public void setMenuable(final boolean menuable) {
        this.menuable = menuable;
    }

}
