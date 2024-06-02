package br.com.projeta.gestor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

import br.com.projeta.gestor.infra.AuthFeing;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "my-app")
@EnableFeignClients( basePackageClasses = {AuthFeing.class})
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // @Bean
    // SqlDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(DataSource dataSource,
    //         SqlInitializationProperties properties, SamplePersonRepository repository) {
    //     // This bean ensures the database is only initialized when empty
    //     return new SqlDataSourceScriptDatabaseInitializer(dataSource, properties) {
    //         @Override
    //         public boolean initializeDatabase() {
    //             if (repository.count() == 0L) {
    //                 return super.initializeDatabase();
    //             }
    //             return false;
    //         }
    //     };
    // }
}
