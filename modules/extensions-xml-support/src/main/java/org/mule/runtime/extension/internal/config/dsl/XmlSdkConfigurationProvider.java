/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.config.dsl;

import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.extension.api.runtime.config.ConfigurationInstance;
import org.mule.runtime.extension.api.runtime.config.ConfigurationProvider;
import org.mule.runtime.module.extension.internal.runtime.config.LifecycleAwareConfigurationProvider;

import java.util.List;
import java.util.Map;

/**
 * {@link ConfigurationProvider} implementation for Xml-Sdk connectors.
 *
 * @since 4.3
 */
public class XmlSdkConfigurationProvider extends LifecycleAwareConfigurationProvider {

  private final List<ConfigurationProvider> innerConfigProviders;
  private final Map<String, String> parameters;
  private boolean injected = false;

  public XmlSdkConfigurationProvider(String name,
                                     List<ConfigurationProvider> innerConfigProviders,
                                     Map<String, String> parameters,
                                     ExtensionModel extensionModel,
                                     ConfigurationModel configurationModel,
                                     MuleContext muleContext) {
    super(name, extensionModel, configurationModel, muleContext);
    innerConfigProviders.forEach(this::registerConfigurationProvider);
    this.innerConfigProviders = innerConfigProviders;
    this.parameters = parameters;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  @Override
  public ConfigurationInstance get(Event event) {
    return new XmlSdkCompositeConfigurationInstance(getName(), getConfigurationModel(), innerConfigProviders, event);
  }

  @Override
  public boolean isDynamic() {
    return false;
  }


  //TODO: MULE-18253 Remove all this when the issue is properly fixed.

  @Override
  public void initialise() throws InitialisationException {
    if(lazyInit()) {
      super.initialise();
    }
  }

  @Override
  public void start() throws MuleException {
    if(lazyInit()) {
      super.start();
    }
  }

  @Override
  public void stop() throws MuleException {
    if(lazyInit()) {
      super.stop();
    }
  }

  @Override
  public void dispose() {
    if (lazyInit()) {
      super.dispose();
    }
  }

  private boolean lazyInit() {
    return this.muleContext.getConfiguration().isLazyInit();
  }

}
