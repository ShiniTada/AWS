package com.epam.esm.controller.init;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class WebAppInitializer implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) {

    AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
    ctx.register(WebConfiguration.class);
    ctx.setServletContext(servletContext);
    servletContext.addListener(new ContextLoaderListener(ctx));

    DispatcherServlet dispatcherServlet = new DispatcherServlet(ctx);
    dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

    ServletRegistration.Dynamic servlet =
        servletContext.addServlet("dispatcher", dispatcherServlet);
    servlet.setLoadOnStartup(1);
    servlet.addMapping("/");
    servlet.setMultipartConfig(getMultipartConfigElement());
  }

  private MultipartConfigElement getMultipartConfigElement() {
    return new MultipartConfigElement("", 1024 * 1024 * 5, 1024 * 1024 * 5 * 5, 1024 * 1024);
  }
}
