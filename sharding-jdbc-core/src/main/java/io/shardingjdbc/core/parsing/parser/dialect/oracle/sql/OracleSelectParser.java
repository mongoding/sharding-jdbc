/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingjdbc.core.parsing.parser.dialect.oracle.sql;

import io.shardingjdbc.core.rule.ShardingRule;
import io.shardingjdbc.core.parsing.lexer.LexerEngine;
import io.shardingjdbc.core.parsing.parser.dialect.oracle.clause.OracleForClauseParser;
import io.shardingjdbc.core.parsing.parser.dialect.oracle.clause.OracleHierarchicalQueryClauseParser;
import io.shardingjdbc.core.parsing.parser.dialect.oracle.clause.OracleModelClauseParser;
import io.shardingjdbc.core.parsing.parser.dialect.oracle.clause.facade.OracleSelectClauseParserFacade;
import io.shardingjdbc.core.parsing.parser.sql.dql.select.AbstractSelectParser;
import io.shardingjdbc.core.parsing.parser.sql.dql.select.SelectStatement;

/**
 * Select parser for Oracle.
 *
 * @author zhangliang
 */
public final class OracleSelectParser extends AbstractSelectParser {
    
    private final OracleHierarchicalQueryClauseParser hierarchicalQueryClauseParser;
    
    private final OracleModelClauseParser modelClauseParser;
    
    private final OracleForClauseParser forClauseParser;
    
    public OracleSelectParser(final ShardingRule shardingRule, final LexerEngine lexerEngine) {
        super(shardingRule, lexerEngine, new OracleSelectClauseParserFacade(shardingRule, lexerEngine));
        hierarchicalQueryClauseParser = new OracleHierarchicalQueryClauseParser(lexerEngine);
        modelClauseParser = new OracleModelClauseParser(lexerEngine);
        forClauseParser = new OracleForClauseParser(lexerEngine);
    }
    
    @Override
    protected void parseInternal(final SelectStatement selectStatement) {
        parseDistinct();
        parseSelectList(selectStatement, getItems());
        parseFrom(selectStatement);
        parseWhere(getShardingRule(), selectStatement, getItems());
        parseHierarchicalQueryClause();
        parseGroupBy(selectStatement);
        parseHaving();
        parseModelClause();
        parseOrderBy(selectStatement);
        parseFor(selectStatement);
        parseSelectRest();
    }
    
    private void parseHierarchicalQueryClause() {
        hierarchicalQueryClauseParser.parse();
    }
    
    private void parseModelClause() {
        modelClauseParser.parse();
    }
    
    private void parseFor(final SelectStatement selectStatement) {
        forClauseParser.parse(selectStatement);
    }
}
