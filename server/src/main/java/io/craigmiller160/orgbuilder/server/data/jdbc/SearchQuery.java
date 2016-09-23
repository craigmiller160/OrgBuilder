package io.craigmiller160.orgbuilder.server.data.jdbc;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

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

    private SearchQuery(String query){
        this.query = query;
    }

    public String getQuery(){
        return query;
    }

    public static class Builder{

        private final List<Triple<String,Object,Integer>> parameters = new ArrayList<>();
        private final String baseQuery;
        private String orderByClause;

        public Builder(String baseQuery){
            this.baseQuery = baseQuery;
        }

        public Builder setOrderByClause(String orderByClause){
            this.orderByClause = orderByClause;
            return this;
        }

        public Builder addParameter(String columnName, Object value, int type){
            parameters.add(ImmutableTriple.of(columnName, value, type));
            return this;
        }

        public SearchQuery build(){
            StrBuilder queryBuilder = new StrBuilder();
            queryBuilder.appendln(baseQuery);
            if(parameters.size() > 0){
                queryBuilder.append("WHERE ");
                parameters.forEach((p) -> appendParameter(queryBuilder, p));
            }

            if(!StringUtils.isEmpty(orderByClause)){
                queryBuilder.append(orderByClause).append(";");
            }

            return new SearchQuery(queryBuilder.toString());
        }

        private void appendParameter(StrBuilder queryBuilder, Triple<String,Object,Integer> parameter){
            if(!queryBuilder.endsWith(WHERE_CLAUSE)){
                queryBuilder.append(AND_CLAUSE);
            }
            queryBuilder.append(parameter.getLeft()).append(" ");
            if(parameter.getMiddle() == null){
                queryBuilder.append(IS_NULL_CLAUSE);
            }
            else{
                queryBuilder.append("= ");
                Object value = parameter.getMiddle();
                if(parameter.getRight() == STRING_TYPE){
                    String escapedValue = value.toString().replaceAll("'","''");
                    queryBuilder.append("'").append(escapedValue).append("'");
                }
                else if(parameter.getRight() == NUMBER_TYPE){
                    if(!StringUtils.isNumeric(value.toString())){
                        throw new IllegalArgumentException("Query Parameter is not numeric. Column: " + parameter.getLeft() + " Value: " + value.toString());
                    }
                    queryBuilder.append(value.toString());
                }
                else if(parameter.getRight() == BOOLEAN_TYPE){
                    if(!"true".equalsIgnoreCase(value.toString()) && !"false".equalsIgnoreCase(value.toString()) &&
                            !"0".equalsIgnoreCase(value.toString()) && !"1".equalsIgnoreCase(value.toString())){
                        throw new IllegalArgumentException("Query Parameter is not a boolean. Column: " + parameter.getLeft() + " Value: " + value.toString());
                    }
                    queryBuilder.append(value.toString());
                }
                else if(parameter.getRight() == DATE_TYPE){
                    throw new UnsupportedOperationException("Date type is not currently supported by SearchQuery.Builder");
                }
            }
            queryBuilder.appendNewLine();
        }

    }

}
