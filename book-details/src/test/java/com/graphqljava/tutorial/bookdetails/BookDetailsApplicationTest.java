package com.graphqljava.tutorial.bookdetails;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class BookDetailsApplicationTest {

    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;

    @Test
    public void contextLoads() {
        assertThat(graphQLDataFetchers).isNotNull();
    }

}