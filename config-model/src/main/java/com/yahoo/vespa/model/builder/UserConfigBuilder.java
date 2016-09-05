// Copyright 2016 Yahoo Inc. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.vespa.model.builder;

import com.yahoo.config.model.deploy.ConfigDefinitionStore;
import com.yahoo.config.application.api.DeployLogger;
import com.yahoo.config.model.producer.UserConfigRepo;
import com.yahoo.log.LogLevel;
import com.yahoo.text.XML;
import com.yahoo.vespa.config.*;
import com.yahoo.vespa.model.builder.xml.dom.DomConfigPayloadBuilder;
import org.w3c.dom.Element;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author lulf
 * @since 5.1
 */
public class UserConfigBuilder {

    public static final Logger log = Logger.getLogger(UserConfigBuilder.class.getPackage().toString());

    public static UserConfigRepo build(Element producerSpec, ConfigDefinitionStore configDefinitionStore, DeployLogger deployLogger) {
        final Map<ConfigDefinitionKey, ConfigPayloadBuilder> builderMap = new LinkedHashMap<>();
        if (producerSpec == null) {
            log.log(LogLevel.SPAM, "In getUserConfigs. producerSpec is null");
        }
        log.log(LogLevel.DEBUG, "getUserConfigs for " + producerSpec);
        for (Element configE : XML.getChildren(producerSpec, "config")) {
            buildElement(configE, builderMap, configDefinitionStore, deployLogger);
        }
        return new UserConfigRepo(builderMap);
    }


    private static void buildElement(Element element, Map<ConfigDefinitionKey, ConfigPayloadBuilder> builderMap, ConfigDefinitionStore configDefinitionStore, DeployLogger logger) {
        ConfigDefinitionKey key = DomConfigPayloadBuilder.parseConfigName(element);
        log.log(LogLevel.SPAM, "Looking at " + key);

        ConfigDefinition def = getConfigDef(key, configDefinitionStore);
        // TODO: Fail here unless deploying with :force true
        if (def == null) {
            logger.log(LogLevel.WARNING, "Unable to find config definition for config '" + key.getNamespace() + "." + key.getName() +
                                         "'. Please ensure that the name is spelled correctly, and that the def file is included in a bundle.");
        }
        List<String> issuedWarnings = new ArrayList<>();
        for (String warning : issuedWarnings) {
            logger.log(LogLevel.WARNING, warning);
        }
        ConfigPayloadBuilder payloadBuilder = new DomConfigPayloadBuilder(def).build(element, issuedWarnings);
        log.log(LogLevel.SPAM, "configvalue=" + ConfigPayload.fromBuilder(payloadBuilder).toString());
        log.log(LogLevel.DEBUG, "Looking up key: " + key.toString());
        ConfigPayloadBuilder old = builderMap.get(key);
        if (old != null) {
            logger.log(LogLevel.WARNING, "Multiple overrides for " + key + " found. Applying in the order they are discovered");
            log.log(LogLevel.DEBUG, "old configvalue=" + old);
            old.override(payloadBuilder);
        } else {
            builderMap.put(key, payloadBuilder);
        }
    }

    /**
     * Returns the config definition matching the given name, or null if not found.
     */
    private static ConfigDefinition getConfigDef(ConfigDefinitionKey configDefinitionKey, ConfigDefinitionStore configDefinitionStore) {
        return configDefinitionStore.getConfigDefinition(configDefinitionKey).orElse(null);
    }

}

