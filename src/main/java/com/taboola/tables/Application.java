package com.taboola.tables;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.taboola.tables.db.User;
import com.taboola.tables.db.UserRepo;

/**
 * Created by boaz.y on 11/01/2017.
 */
@SpringBootApplication
public class Application {

    @Autowired
    UserRepo repository;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

//            setUpBasicUsers();
//            showFindUsers();



        };
    }

    private void showFindUsers() {
        // fetch all customers
        log.info("users found with findAll():");
        for (User user : repository.findAll()) {
            log.info(user.toString());
        }
        // fetch an individual by ID
        User user = repository.findOne(1L);
        log.info("Customer found with findOne(1L):");
        log.info(user.toString());

        // fetch customers by last name
        log.info("Customer found with findByGmailId('gid3'):");
        /*for (User bauer : repository.findByGmailId("gid3")) {
            log.info(bauer.toString());
        }*/

        log.info("Customer found with findById(4):");
        for (User bauer : repository.findById(3L)) {
            log.info(bauer.toString());
        }
    }

    private void setUpBasicUsers() {
        repository.save(new User("Boaz", "gid1", "1@gmail.com"));
        repository.save(new User("Eyal", "gid2", "2@gmail.com"));
        repository.save(new User("Rami", "gid3", "3@gmail.com"));
        repository.save(new User("Shiran", "gid4", "4@gmail.com"));
    }
}
