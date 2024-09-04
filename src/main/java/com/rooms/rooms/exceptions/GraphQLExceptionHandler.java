package com.rooms.rooms.exceptions;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

     @Override
     protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
          if (ex instanceof DataNotFoundException) {
               return GraphqlErrorBuilder.newError(env)
                       .message(ex.getMessage())
                       .errorType(ErrorType.NOT_FOUND)
                       .locations(Collections.singletonList(env.getField().getSourceLocation()))
                       .path(env.getExecutionStepInfo().getPath())
                       .build();
          }

          if (ex instanceof AlreadyExistException) {
               return GraphqlErrorBuilder.newError(env)
                       .message(ex.getMessage())
                       .errorType(ErrorType.BAD_REQUEST)
                       .locations(Collections.singletonList(env.getField().getSourceLocation()))
                       .path(env.getExecutionStepInfo().getPath())
                       .build();
          }

          if (ex instanceof OrderNotCompletedException) {
               return GraphqlErrorBuilder.newError(env)
                       .message(ex.getMessage())
                       .errorType(ErrorType.BAD_REQUEST)
                       .locations(Collections.singletonList(env.getField().getSourceLocation()))
                       .path(env.getExecutionStepInfo().getPath())
                       .build();
          }
          return super.resolveToSingleError(ex, env);
     }
}
