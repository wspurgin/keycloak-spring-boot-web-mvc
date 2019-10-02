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
