Sample repo to showcase problem with
[KEYCLOAK-11282](https://issues.jboss.org/browse/KEYCLOAK-11282) and Spring Web
MVC
==================================

Using Keycloak spring boot adapter (7.0.0) causes the issue
outlined in [KEYCLOAK-11282](https://issues.jboss.org/browse/KEYCLOAK-11282).
While the work around mentioned in there fixes the `BeanCurrentlyInUse`, another
issue exists (or is caused by this work around) where a race condition is
created where the WebMVC configuration is required by the security configuration
before the servlet is set (manifesting in the resourceHandlerMapping method of
the WebMvcConfigurationSupport class).

This does _not_ exists in 6.0.1 Keycloak. Modifying this code base to _only_
bump the version from 6.0.1 to 7.0.0 produces the IllegalStateException caused
by the race condition.

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.8.RELEASE)

2019-10-02 13:12:32.626  INFO 91916 --- [           main] com.example.demo.DemoApplication         : No active profile set, falling back to default profiles: default
2019-10-02 13:12:33.960  INFO 91916 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2019-10-02 13:12:33.993  INFO 91916 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2019-10-02 13:12:33.993  INFO 91916 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.24]
2019-10-02 13:12:34.108  INFO 91916 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2019-10-02 13:12:34.108  INFO 91916 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1440 ms
2019-10-02 13:12:34.314  INFO 91916 --- [           main] o.s.s.web.DefaultSecurityFilterChain     : Creating filter chain: any request, [org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@4e628b52, org.springframework.security.web.context.SecurityContextPersistenceFilter@67001148, org.springframework.security.web.header.HeaderWriterFilter@963176, org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter@58faa93b, org.springframework.security.web.authentication.logout.LogoutFilter@3a022576, org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter@61942c1, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@31cb96e1, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@66e889df, org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter@6b9c69a9, org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter@6622a690, org.springframework.security.web.session.SessionManagementFilter@4cafa9aa, org.springframework.security.web.access.ExceptionTranslationFilter@3a627c80, org.springframework.security.web.access.intercept.FilterSecurityInterceptor@476ec9d0]
2019-10-02 13:12:34.511  INFO 91916 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2019-10-02 13:12:34.588  WARN 91916 --- [           main] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'resourceHandlerMapping' defined in class path resource [org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration$EnableWebMvcConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.web.servlet.HandlerMapping]: Factory method 'resourceHandlerMapping' threw exception; nested exception is java.lang.IllegalStateException: No ServletContext set
2019-10-02 13:12:34.588  INFO 91916 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
2019-10-02 13:12:34.592  INFO 91916 --- [           main] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2019-10-02 13:12:34.602  INFO 91916 --- [           main] ConditionEvaluationReportLoggingListener : 

Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2019-10-02 13:12:34.610 ERROR 91916 --- [           main] o.s.boot.SpringApplication               : Application run failed

org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'resourceHandlerMapping' defined in class path resource [org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration$EnableWebMvcConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.web.servlet.HandlerMapping]: Factory method 'resourceHandlerMapping' threw exception; nested exception is java.lang.IllegalStateException: No ServletContext set
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:627) ~[spring-beans-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:456) ~[spring-beans-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1321) ~[spring-beans-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1160) ~[spring-beans-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:555) ~[spring-beans-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:515) ~[spring-beans-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:320) ~[spring-beans-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:222) ~[spring-beans-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:318) ~[spring-beans-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) ~[spring-beans-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:845) ~[spring-beans-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:877) ~[spring-context-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:549) ~[spring-context-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:141) ~[spring-boot-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:744) [spring-boot-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:391) [spring-boot-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:312) [spring-boot-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1215) [spring-boot-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1204) [spring-boot-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at com.example.demo.DemoApplication.main(DemoApplication.java:10) [classes/:na]
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.web.servlet.HandlerMapping]: Factory method 'resourceHandlerMapping' threw exception; nested exception is java.lang.IllegalStateException: No ServletContext set
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:185) ~[spring-beans-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:622) ~[spring-beans-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	... 19 common frames omitted
Caused by: java.lang.IllegalStateException: No ServletContext set
	at org.springframework.util.Assert.state(Assert.java:73) ~[spring-core-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport.resourceHandlerMapping(WebMvcConfigurationSupport.java:486) ~[spring-webmvc-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration$EnableWebMvcConfiguration$$EnhancerBySpringCGLIB$$4d84383e.CGLIB$resourceHandlerMapping$39(<generated>) ~[spring-boot-autoconfigure-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration$EnableWebMvcConfiguration$$EnhancerBySpringCGLIB$$4d84383e$$FastClassBySpringCGLIB$$38ff2e41.invoke(<generated>) ~[spring-boot-autoconfigure-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at org.springframework.cglib.proxy.MethodProxy.invokeSuper(MethodProxy.java:244) ~[spring-core-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.context.annotation.ConfigurationClassEnhancer$BeanMethodInterceptor.intercept(ConfigurationClassEnhancer.java:363) ~[spring-context-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration$EnableWebMvcConfiguration$$EnhancerBySpringCGLIB$$4d84383e.resourceHandlerMapping(<generated>) ~[spring-boot-autoconfigure-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_151]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_151]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_151]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_151]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:154) ~[spring-beans-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	... 20 common frames omitted


Process finished with exit code 1
```
