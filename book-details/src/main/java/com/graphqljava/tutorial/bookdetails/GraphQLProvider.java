package com.graphqljava.tutorial.bookdetails;

import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {

    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;

    @Value("classpath:schema.graphqls")
    private Resource resource;

    private GraphQL graphQL;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    /**
     * @PostConstruct annotation is used on a method that needs to be executed after dependency injection is
     * done to perform any initialization. This method must be invoked before the class is put into service.
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {
        // URL url = Resources.getResource("schema.graphqls");
        //  String sdl = Resources.toString(url, StandardCharsets.UTF_8);
        // GraphQLSchema graphQLSchema = buildSchema(sdl);
        File file = resource.getFile();
        GraphQLSchema graphQLSchema = buildSchema(file);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    //private GraphQLSchema buildSchema(String sdl) {
    private GraphQLSchema buildSchema(File file) {
        // A TypeDefinitionRegistry contains the set of type definitions that come from compiling a graphql schema
        // definition file via SchemaParser.parse(String)
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(file);

        // A runtime wiring is a specification of data fetchers, type resolvers and custom scalars that are needed
        // to wire together a functional GraphQLSchema
        RuntimeWiring runtimeWiring = buildWiring();

        // This can generate a working runtime schema from a type registry and runtime wiring
        // SchemaGenerator schemaGenerator = new SchemaGenerator();

        // This will take a TypeDefinitionRegistry and a RuntimeWiring and put them together to create a executable
        // schema
        return new SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    /**
     *
     * @return
     */
    private RuntimeWiring buildWiring() {
//        return RuntimeWiring.newRuntimeWiring()
//                .type(newTypeWiring("Query")
//                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
//                .type(newTypeWiring("Book")
//                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
//                .build();

        return RuntimeWiring.newRuntimeWiring()
                .type("Query",  typeWiring -> typeWiring
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
                .type("Book", typeWiring -> typeWiring
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .build();
    }
}