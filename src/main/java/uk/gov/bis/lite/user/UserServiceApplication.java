package uk.gov.bis.lite.user;

import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import ru.vyarus.dropwizard.guice.module.installer.feature.ManagedInstaller;
import ru.vyarus.dropwizard.guice.module.installer.feature.jersey.ResourceInstaller;
import uk.gov.bis.lite.common.jersey.filter.ContainerCorrelationIdFilter;
import uk.gov.bis.lite.common.jwt.LiteJwtAuthFilterHelper;
import uk.gov.bis.lite.common.jwt.LiteJwtUser;
import uk.gov.bis.lite.common.metrics.readiness.ReadinessServlet;
import uk.gov.bis.lite.common.paas.db.CloudFoundryEnvironmentSubstitutor;
import uk.gov.bis.lite.user.config.GuiceModule;
import uk.gov.bis.lite.user.config.UserServiceConfiguration;
import uk.gov.bis.lite.user.config.auth.SimpleAuthenticator;
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
        .extensions(UserPrivilegesResource.class)
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

    environment.jersey().register(new AuthDynamicFeature(liteJwtUserJwtAuthFilter));

    environment.jersey().register(new AuthValueFactoryProvider.Binder<>(LiteJwtUser.class));

    environment.jersey().register(new AuthDynamicFeature(
        new BasicCredentialAuthFilter.Builder<PrincipalImpl>()
            .setAuthenticator(new SimpleAuthenticator(configuration.getAdminUsername(), configuration.getAdminPassword()))
            .setRealm("User Service Admin Authentication")
            .buildAuthFilter()));

    environment.jersey().register(ContainerCorrelationIdFilter.class);
  }

  public GuiceBundle<UserServiceConfiguration> getGuiceBundle() {
    return guiceBundle;
  }

  public static void main(String[] args) throws Exception {
    new UserServiceApplication().run(args);
  }

}