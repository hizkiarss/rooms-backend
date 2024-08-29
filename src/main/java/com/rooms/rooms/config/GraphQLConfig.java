package com.rooms.rooms.config;

import graphql.language.StringValue;
import graphql.scalars.ExtendedScalars;
import graphql.schema.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Configuration
public class GraphQLConfig {

     @Bean
     public RuntimeWiringConfigurer runtimeWiringConfigurer() {
          return wiringBuilder -> wiringBuilder
                  .scalar(ExtendedScalars.DateTime)
                  .scalar(instantScalar())
                  .scalar(dateScalar());
     }

     @Bean
     public GraphQLScalarType instantScalar() {
          return GraphQLScalarType.newScalar()
                  .name("Instant")
                  .description("Java Instant as scalar")
                  .coercing(new Coercing<Instant, String>() {
                       @Override
                       public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                            if (dataFetcherResult instanceof Instant) {
                                 return ((Instant) dataFetcherResult).toString();
                            } else {
                                 throw new CoercingSerializeException("Expected an Instant object.");
                            }
                       }

                       @Override
                       public Instant parseValue(Object input) throws CoercingParseValueException {
                            try {
                                 if (input instanceof String) {
                                      return Instant.parse((String) input);
                                 } else {
                                      throw new CoercingParseValueException("Expected a String.");
                                 }
                            } catch (DateTimeParseException e) {
                                 throw new CoercingParseValueException(String.format("Not a valid Instant: '%s'.", input), e);
                            }
                       }

                       @Override
                       public Instant parseLiteral(Object input) throws CoercingParseLiteralException {
                            if (!(input instanceof StringValue)) {
                                 throw new CoercingParseLiteralException("Expected a StringValue.");
                            }
                            try {
                                 return Instant.parse(((StringValue) input).getValue());
                            } catch (DateTimeParseException e) {
                                 throw new CoercingParseLiteralException(String.format("Not a valid Instant: '%s'.", input));
                            }
                       }
                  }).build();
     }

     @Bean
     public GraphQLScalarType dateScalar() {
          return GraphQLScalarType.newScalar()
                  .name("Date")
                  .description("Java LocalDate as scalar")
                  .coercing(new Coercing<LocalDate, String>() {
                       @Override
                       public String serialize(Object dataFetcherResult) {
                            if (dataFetcherResult instanceof LocalDate) {
                                 return ((LocalDate) dataFetcherResult).format(DateTimeFormatter.ISO_LOCAL_DATE);
                            } else {
                                 throw new CoercingSerializeException("Expected a LocalDate object.");
                            }
                       }

                       @Override
                       public LocalDate parseValue(Object input) {
                            try {
                                 if (input instanceof String) {
                                      return LocalDate.parse((String) input, DateTimeFormatter.ISO_LOCAL_DATE);
                                 } else {
                                      throw new CoercingParseValueException("Expected a String.");
                                 }
                            } catch (DateTimeParseException e) {
                                 throw new CoercingParseValueException(String.format("Not a valid LocalDate: '%s'.", input), e);
                            }
                       }

                       @Override
                       public LocalDate parseLiteral(Object input) {
                            if (input instanceof StringValue) {
                                 try {
                                      return LocalDate.parse(((StringValue) input).getValue(), DateTimeFormatter.ISO_LOCAL_DATE);
                                 } catch (DateTimeParseException e) {
                                      throw new CoercingParseLiteralException(String.format("Not a valid LocalDate: '%s'.", input));
                                 }
                            } else {
                                 throw new CoercingParseLiteralException("Expected a StringValue.");
                            }
                       }
                  }).build();
     }
     @Bean
     public GraphQLScalarType booleanScalar() {
          return GraphQLScalarType.newScalar()
                  .name("Boolean")
                  .description("Custom Boolean scalar for handling boolean values")
                  .coercing(new Coercing<Boolean, Boolean>() {
                       @Override
                       public Boolean serialize(Object dataFetcherResult) {
                            if (dataFetcherResult instanceof Boolean) {
                                 return (Boolean) dataFetcherResult;
                            } else {
                                 throw new CoercingSerializeException("Expected a Boolean object.");
                            }
                       }

                       @Override
                       public Boolean parseValue(Object input) {
                            if (input instanceof Boolean) {
                                 return (Boolean) input;
                            } else if (input instanceof String) {
                                 return Boolean.parseBoolean((String) input);
                            } else {
                                 throw new CoercingParseValueException("Expected a Boolean or a String representing a Boolean.");
                            }
                       }

                       @Override
                       public Boolean parseLiteral(Object input) {
                            if (input instanceof graphql.language.BooleanValue) {
                                 return ((graphql.language.BooleanValue) input).isValue();
                            } else if (input instanceof graphql.language.StringValue) {
                                 return Boolean.parseBoolean(((graphql.language.StringValue) input).getValue());
                            } else {
                                 throw new CoercingParseLiteralException("Expected a BooleanValue or StringValue.");
                            }
                       }
                  })
                  .build();
     }
}
