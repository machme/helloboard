package examples.helloboard.dao;

import examples.helloboard.domain.Search;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

public class AdditionalSQL {
    private Search search;
    private List<String> orderList;

    public AdditionalSQL(Search search, List<String > orderList){
        this.search = search;
        this.orderList = orderList;
    }

    public String makeWhereSQL(){
        StringBuilder where = new StringBuilder(" WHERE ");

        if (search.getCategory() != null) {
            where.append("category.name= :category AND topic.name= :topic");
            if (search.getSearchStr() != null) {
                where.append(" AND ");
            }
        }

        if (search.getSearchStr() != null) {
            where.append("board.:searchType LIKE CONCAT('%',:searchStr,'%')");
        }

        if (" WHERE ".equals(where)) {
            where = new StringBuilder("");
        }

        return where.toString();
    }

    public String makeOrderSQL(){
        StringBuilder order = new StringBuilder(" ORDER BY ");

        if (orderList.size() > 0) {
            order.append((String) (orderList.get(0)));
        }
        for (int i = 1, size = orderList.size(); i < size; i++) {
            order.append(",").append(orderList.get(i));
        }

        if (" ORDER BY ".equals(order)) {
            order = new StringBuilder("");
        }
        return order.toString();
    }

    public String makeLimitSQL(){
        StringBuilder limit = new StringBuilder(" LIMIT ");
        final Integer listCountForPage = search.getListCountForPage();
        final Integer curPage = search.getCurPage();

        limit.append((curPage - 1)*listCountForPage).append(", ").append(listCountForPage);

        if (" LIMIT ".equals(limit)) {
            limit = new StringBuilder("");
        }
        return limit.toString();
    }
}