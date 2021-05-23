package fr.test.equisign;

import fr.test.equisign.dao.UploadedFileDAO;
import fr.test.equisign.entity.UploadedFile;
import fr.test.equisign.health.TemplateHealthCheck;
import fr.test.equisign.resources.UploadedFileResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class EquisignChallengeApplication extends Application<EquisignChallengeConfiguration> {
    public static void main(String[] args) throws Exception {
        new EquisignChallengeApplication().run(args);
    }

    private final HibernateBundle<EquisignChallengeConfiguration> hibernate = new HibernateBundle<EquisignChallengeConfiguration>(UploadedFile.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(EquisignChallengeConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<EquisignChallengeConfiguration> bootstrap) {
//        bootstrap.addBundle(new ViewBundle<EquisignChallengeConfiguration>());
        bootstrap.addBundle(new MultiPartBundle());
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(EquisignChallengeConfiguration configuration,
                    Environment environment) {

        final UploadedFileDAO dao = new UploadedFileDAO(hibernate.getSessionFactory());

        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(new UploadedFileResource(dao));
    }

}