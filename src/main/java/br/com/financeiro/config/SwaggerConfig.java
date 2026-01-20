package br.com.financeiro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gestão Financeira")
                        .version("1.0.0")
                        .description("Sistema de controle de receitas e despesas pessoais")
                        .contact(new Contact()
                                .name("Fernanda Lima")
                                .email("ffernandalima.ads|@gmail.com")
                                .url("https://github.com/efernandalima"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
