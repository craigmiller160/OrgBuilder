package io.craigmiller160.orgbuilder.server.data.jdbc;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 9/22/16.
 */
public class SearchQuery {

    private static final String WHERE_CLAUSE = "WHERE ";
    private static final String AND_CLAUSE = "AND ";
    private static final String IS_NULL_CLAUSE = "IS NULL ";

    public static final int STRING_TYPE = 435;
    public static final int NUMBER_TYPE = 436;
    public static final int BOOLEAN_TYPE = 437;
    public static final int DATE_TYPE = 438;

    private final String query;
    private final List<Pair<String,Object>> parameters;

    private SearchQuery(String query, List<Pair<String,Object>> parameters){
        this.query = query;
        this.parameters = parameters;
    }

    public String getQuery(){
        return query;
    }

    public List<Pair<String,Object>> getParameters(){
        return parameters;
    }

    public Statement createAndParameterizeStatement(Connection connection) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(query);
        for(int i = 0; i < parameters.size(); i++){
            stmt.setObject(i, parameters.get(i).getRight());
        }
        return stmt;
    }

    public static class Builder{

        private final List<Pair<String,Object>> parameters = new ArrayList<>();
        private final String baseQuery;
        private String orderByClause;

        public Builder(String baseQuery){
            this.baseQuery = baseQuery;
        }

        public Builder setOrderByClause(String orderByClause){
            this.orderByClause = orderByClause;
            return this;
        }

        public Builder addParameter(String columnName, Object value){
            parameters.add(ImmutablePair.of(columnName, value));
            return this;
        }

        private String removeTrailingNewline(String text){
            if(text.charAt(text.length() - 1) == '\n'){
                text = text.substring(0, text.length() - 1);
            }

            if(text.charAt(text.length() - 1) == '\r'){
                text = text.substring(0, text.length() - 1);
            }

            return text;
        }

        public SearchQuery build(){
            StrBuilder queryBuilder = new StrBuilder();
            queryBuilder.appendln(removeTrailingNewline(baseQuery));
            if(parameters.size() > 0){
                queryBuilder.append("WHERE ");
                parameters.forEach(p -> appendParameter(queryBuilder, p));
            }

            if(!StringUtils.isEmpty(orderByClause)){
                queryBuilder.append(orderByClause);
            }

            return new SearchQuery(queryBuilder.toString(), parameters);
        }

        private void appendParameter(StrBuilder queryBuilder, Pair<String,Object> parameter){
            if(!queryBuilder.endsWith(WHERE_CLAUSE)){
                queryBuilder.append(AND_CLAUSE);
            }
            queryBuilder.append(parameter.getLeft()).append(" ");
            if(parameter.getRight() == null){
                queryBuilder.appendln(IS_NULL_CLAUSE);
            }
            else{
                queryBuilder.appendln("= ?");
            }
        }

    }

}
