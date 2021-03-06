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

    public static int LIKE_OPERATOR = 579;
    public static int EQUALS_OPERATOR = 580;

    private static final String WHERE_CLAUSE = "WHERE ";
    private static final String AND_CLAUSE = "AND ";
    private static final String IS_NULL_CLAUSE = "IS NULL ";
    private static final String LIMIT_CLAUSE = "LIMIT ?,?";

    private final String query;
    private final List<Triple<String,Object,Integer>> parameters;
    private final Pair<Long,Long> limit;

    private SearchQuery(String query, List<Triple<String,Object,Integer>> parameters, Pair<Long,Long> limit){
        this.query = query;
        this.parameters = parameters;
        this.limit = limit;
    }

    public String getQuery(){
        return query;
    }

    public List<Triple<String,Object,Integer>> getParameters(){
        return parameters;
    }

    public Pair<Long,Long> getLimit(){
        return limit;
    }

    public PreparedStatement createAndParameterizeStatement(Connection connection) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(query);
        for(int i = 0; i < parameters.size(); i++){
            if(parameters.get(i).getRight() == LIKE_OPERATOR){
                stmt.setObject((i + 1), "%" + parameters.get(i).getMiddle() + "%");
            }
            else{
                stmt.setObject((i + 1), parameters.get(i).getMiddle());
            }
        }

        if(limit != null){
            stmt.setLong(parameters.size() + 1, limit.getLeft());
            stmt.setLong(parameters.size() + 2, limit.getRight());
        }

        return stmt;
    }

    public static class Builder{

        private final List<Triple<String,Object,Integer>> parameters = new ArrayList<>();
        private final String baseQuery;
        private String orderByClause;
        private Pair<Long,Long> limit;

        public Builder(String baseQuery){
            this.baseQuery = baseQuery;
        }

        public Builder setOrderByClause(String orderByClause){
            this.orderByClause = orderByClause;
            return this;
        }

        public Builder setLimit(long offset, long size){
            this.limit = ImmutablePair.of(offset, size);
            return this;
        }

        public Builder addParameter(String columnName, Object value, int operator){
            if(operator != EQUALS_OPERATOR && operator != LIKE_OPERATOR){
                throw new IllegalArgumentException("Operator argument invalid: " + operator);
            }

            parameters.add(ImmutableTriple.of(columnName, value, operator));
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
                queryBuilder.appendln(orderByClause);
            }

            if(limit != null){
                queryBuilder.append(LIMIT_CLAUSE);
            }

            return new SearchQuery(queryBuilder.toString(), parameters, limit);
        }

        private void appendParameter(StrBuilder queryBuilder, Triple<String,Object,Integer> parameter){
            if(!queryBuilder.endsWith(WHERE_CLAUSE)){
                queryBuilder.append(AND_CLAUSE);
            }
            queryBuilder.append(parameter.getLeft()).append(" ");
            if(parameter.getMiddle() == null){
                queryBuilder.appendln(IS_NULL_CLAUSE);
            }
            else if(parameter.getRight() == LIKE_OPERATOR){
                queryBuilder.appendln("LIKE ?");
            }
            else{
                queryBuilder.appendln("= ?");
            }
        }

    }

}
