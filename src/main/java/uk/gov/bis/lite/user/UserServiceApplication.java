package uk.gov.bis.lite.user;

import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.dropwizard.Application;
import io.dropwizard.auth.PolymorphicAuthDynamicFeature;
import io.dropwizard.auth.PolymorphicAuthValueFactoryProvider;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import ru.vyarus.dropwizard.guice.module.installer.feature.ManagedInstaller;
import ru.vyarus.dropwizard.guice.module.installer.feature.jersey.ResourceInstaller;
import uk.gov.bis.lite.common.auth.basic.SimpleAuthenticator;
import uk.gov.bis.lite.common.auth.basic.SimpleAuthorizer;
import uk.gov.bis.lite.common.auth.basic.User;
import uk.gov.bis.lite.common.jersey.filter.ContainerCorrelationIdFilter;
import uk.gov.bis.lite.common.jwt.LiteJwtAuthFilterHelper;
import uk.gov.bis.lite.common.jwt.LiteJwtUser;
import uk.gov.bis.lite.common.metrics.readiness.ReadinessServlet;
import uk.gov.bis.lite.common.paas.db.CloudFoundryEnvironmentSubstitutor;
import uk.gov.bis.lite.user.config.GuiceModule;
import uk.gov.bis.lite.user.config.UserServiceConfiguration;
import uk.gov.bis.lite.user.resource.UserAccountTypeResource;
import uk.gov.bis.lite.user.resource.UserDetailsResource;
import uk.gov.bis.lite.user.resource.UserPrivilegesResource;

public class UserServiceApplication extends Application<UserServiceConfiguration> {

  private GuiceBundle<UserServiceConfiguration> guiceBundle;
  private final Module module;

  public UserServiceApplication() {
    this(new GuiceModule());
  }

  public UserServiceApplication(Module module) {
    super();
    this.module = module;
  }

  public <T> T getInstance(Class<T> type) {
    return getGuiceBundle().getInjector().getInstance(type);
  }

  @Override
  public void initialize(Bootstrap<UserServiceConfiguration> bootstrap) {
    bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
        new ResourceConfigurationSourceProvider(), new CloudFoundryEnvironmentSubstitutor()));

    guiceBundle = new GuiceBundle.Builder<UserServiceConfiguration>()
        .modules(module)
        .installers(ResourceInstaller.class, ManagedInstaller.class)
        .extensions(UserPrivilegesResource.class, UserDetailsResource.class, UserAccountTypeResource.class)
        .build();
    bootstrap.addBundle(guiceBundle);
  }

  @Override
  public void run(UserServiceConfiguration configuration, Environment environment) throws Exception {
    Injector injector = guiceBundle.getInjector();

    ReadinessServlet readinessServlet = injector.getInstance(ReadinessServlet.class);
    environment.admin().addServlet("ready", readinessServlet).addMapping("/ready");

    String jwtSharedSecret = configuration.getJwtSharedSecret();
    JwtAuthFilter<LiteJwtUser> liteJwtUserJwtAuthFilter = LiteJwtAuthFilterHelper.buildAuthFilter(jwtSharedSecret);

    SimpleAuthenticator simpleAuthenticator = new SimpleAuthenticator(configuration.getAdminLogin(),
        configuration.getAdminPassword(),
        configuration.getServiceLogin(),
        configuration.getServicePassword());

    BasicCredentialAuthFilter<User> userBasicCredentialAuthFilter = new BasicCredentialAuthFilter.Builder<User>()
        .setAuthenticator(simpleAuthenticator)
        .setAuthorizer(new SimpleAuthorizer())
        .setRealm("User Service Authentication")
        .buildAuthFilter();

    PolymorphicAuthDynamicFeature authFeature = new PolymorphicAuthDynamicFeature(
        ImmutableMap.of(LiteJwtUser.class, liteJwtUserJwtAuthFilter, User.class, userBasicCredentialAuthFilter)
    );
    environment.jersey().register(authFeature);

    AbstractBinder authBinder = new PolymorphicAuthValueFactoryProvider.Binder<>(ImmutableSet.of(PrincipalImpl.class, LiteJwtUser.class, User.class));
    environment.jersey().register(authBinder);

    environment.jersey().register(RolesAllowedDynamicFeature.class);

    environment.jersey().register(ContainerCorrelationIdFilter.class);
  }

  public GuiceBundle<UserServiceConfiguration> getGuiceBundle() {
    return guiceBundle;
  }

  public static void main(String[] args) throws Exception {
    new UserServiceApplication().run(args);
  }

}