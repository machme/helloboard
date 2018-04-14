package examples.helloboard.dao;
import examples.helloboard.domain.Board;
import examples.helloboard.domain.BoardInfo;
import examples.helloboard.domain.Search;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.servlet.jsp.tagext.TagInfo;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class BoardDao {
    private NamedParameterJdbcTemplate jdbc;
    private SimpleJdbcInsert insertAction;
    private RowMapper<Board> boardRowMapper = BeanPropertyRowMapper.newInstance(Board.class);
    private RowMapper<BoardInfo> boardInfoRowMapper = BeanPropertyRowMapper.newInstance(BoardInfo.class);
    private RowMapper<TagInfo> tagInfoRowMapper = BeanPropertyRowMapper.newInstance(TagInfo.class);


    public BoardDao(DataSource dataSource) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("BOARD")
                .usingGeneratedKeyColumns("idx"); // 자동으로 id가 생성될 경우
    }

    public int insertBoard(Board board){
        SqlParameterSource params = new BeanPropertySqlParameterSource(board);
        // 자동으로 id를 생성할 경우에는 아래와 같이 생성된 pk를 반환할 수 있다.
        return insertAction.executeAndReturnKey(params).intValue();
//        int count = insertAction.execute(params);
//        return count;
    }

    public int deleteBoard(int boardIdx){
        Map<String, ?> params = Collections.singletonMap("boardIdx", boardIdx);
        return jdbc.update("DELETE FROM board WHERE idx = :boardIdx", params);
    }

    public int updateBoard(Board board){
        SqlParameterSource params = new BeanPropertySqlParameterSource(board);
        return jdbc.update("UPDATE board SET title = :title , content= :content, file_idx= :fileIdx" +
                ", writer_idx= :writerIdx, date= :date, topic_idx= :topicIdx, view= :view, great= :great " +
                "WHERE idx= :idx", params);
    }

    // 홈 화면에서 공지 최신 몇 개 select
//    public List<Board> selectBoardLimit(int limitCount){
//        Map<String, ?> params = Collections.singletonMap("limitCount", limitCount);
//        return jdbc.query(
//                "SELECT  board.idx, board.title " +
//                        "FROM helloboard.board INNER JOIN helloboard.topic ON helloboard.board.topic_idx = helloboard.topic.idx " +
//                        "WHERE helloboard.topic.name = 'notice' ORDER BY date DESC LIMIT :limitCount"
//                , params, boardRowMapper);
//    }
    // 홈 화면에서 베스트 게시물 몇 개 select (카테고리 무관)
//    public List<Board> selectBoardOrderGreatViewDateLimit(int limitCount){
//        Map<String, ?> params = Collections.singletonMap("limitCount", limitCount);
//        return jdbc.query(
//                "SELECT board.idx, board.title " +
//                        "FROM board ORDER BY great DESC, view DESC, date DESC LIMIT 0, :limitCount "
//                , params, boardRowMapper);
//    }

    public List<BoardInfo> selectBoardList(Search search, List<String> orderList){
        MakeSQL madeSQL = new MakeSQL(search, orderList).invoke();
        String where = madeSQL.getWhere().toString();
        String order = madeSQL.getOrder().toString();
        String limit = madeSQL.getLimit().toString();
        SqlParameterSource params = madeSQL.getParams();

        StringBuilder sql = new StringBuilder("select b.idx, b.title, b.date, b.view, b.great,\n" +
                "t.idx topic_idx, t.name topic_name,\n" +
                "c.idx category_idx, c.name category_name,\n" +
                "u.idx user_idx, u.id user_id,\n" +
                "count(r.content) reply_count\n" +
                "from board b\n" +
                "join topic t on b.topic_idx = t.idx\n" +
                "join category c on t.category_idx = c.idx\n" +
                "join user u on b.user_idx = u.idx\n" +
                "join reply r on r.board_idx = b.idx\n")
                .append(where)
                .append("group by b.idx\n")
                .append(order)
                .append(limit);

        return jdbc.query(sql.toString(), params, boardInfoRowMapper);
    }

    public List<TagInfo> selectTagList(Search search, List<String> orderList) {
        MakeSQL madeSQL = new MakeSQL(search, orderList).invoke();
        String where = madeSQL.getWhere().toString();
        String order = madeSQL.getOrder().toString();
        String limit = madeSQL.getLimit().toString();
        SqlParameterSource params = madeSQL.getParams();

        StringBuilder sql = new StringBuilder("select tag.idx, tag.name,\n" +
                "b.idx board_idx\n" +
                "from board b\n" +
                "join topic t on b.topic_idx = t.idx\n" +
                "join category c on t.category_idx = c.idx\n" +
                "join user u on b.user_idx = u.idx\n" +
                "left outer join board_tag bt on b.idx = bt.board_idx\n" +
                "left outer join tag on bt.tag_idx = tag.idx")
                .append(where)
                .append(order)
                .append(limit);

        return jdbc.query(sql.toString(), params, tagInfoRowMapper);
    }

    public BoardInfo selectBoardDetail(int boardID) {
        Map<String, ?> params = Collections.singletonMap("boardId", boardID);

        StringBuilder sql = new StringBuilder("select b.idx, b.title, b.content, b.date, b.view, b.great, b.file_idx,\n" +
                "t.idx topic_idx, t.name topic_name,\n" +
                "c.idx category_idx, c.name category_name,\n" +
                "u.idx user_idx, u.id user_id,\n" +
                "count(r.content) reply_count\n" +
                "from board b\n" +
                "join topic t on b.topic_idx = t.idx\n" +
                "join category c on t.category_idx = c.idx\n" +
                "join user u on b.user_idx = u.idx\n" +
                "join reply r on r.board_idx = b.idx\n")
                .append("where board_id = :boardId\n")
                .append("group by b.idx");

        return jdbc.queryForObject(sql.toString(), params, boardInfoRowMapper);
    }

    public List<TagInfo> selectTagDetail(int boardId) {
        Map<String, ?> params = Collections.singletonMap("boardId", boardId);

        StringBuilder sql = new StringBuilder("select tag.idx, tag.name,\n" +
                "b.idx board_idx\n" +
                "from board_tag bt\n" +
                "left outer join board b on b.idx = bt.board_idx\n" +
                "left outer join tag on bt.tag_idx = tag.idx\n")
                .append("where b.idx = :boardID\n")
                .append("order by tag.idx asc");

        return jdbc.query(sql.toString(), params, tagInfoRowMapper);
    }

    private class MakeSQL {
        private Search search;
        private List<String> orderList;
        private StringBuilder where;
        private StringBuilder order;
        private StringBuilder limit;
        private SqlParameterSource params;

        public MakeSQL(Search search, List<String> orderList) {
            this.search = search;
            this.orderList = orderList;
        }

        public StringBuilder getWhere() {
            return where;
        }

        public StringBuilder getOrder() {
            return order;
        }

        public StringBuilder getLimit() {
            return limit;
        }

        public SqlParameterSource getParams() {
            return params;
        }

        public MakeSQL invoke() {
            where = new StringBuilder(" WHERE ");
            order = new StringBuilder(" ORDER BY ");
            limit = new StringBuilder(" LIMIT :curPage :pageNum");
            final String category = search.getCategory();
            final String topic = search.getTopic();
            final String searchStr = search.getSearchStr();
            final String searchType = search.getSearchType();
            //        final Integer curPage = ( (search.getCurPage()==null) ? 1 : search.getCurPage() ) - 1;
            //        final Integer pageNum = search.getPageNum();

            if(category!=null){
                where.append("category.name=").append(category);
                if(topic != null){
                    where.append(" AND topic.name=").append(topic);
                }
                if(searchStr!=null){
                    where.append(" AND ");
                }
            }

            if(searchStr != null){
                where.append("board.").append(searchType).append(" LIKE CONCAT('%',:searchStr,'%')");
            }

            if(" WHERE ".equals(where)){
                where = new StringBuilder("");
            }

            if(orderList.size()>0){
                order.append((String)(orderList.get(0)));
            }
            for (int i=1, size=orderList.size(); i < size ; i++ ){
                order.append(",").append(orderList.get(i));
            }

            if(" ORDER BY ".equals(order)){
                order = new StringBuilder("");
            }

            System.out.println("where= "+where.toString());
            System.out.println("order= "+order.toString());
            System.out.println("limit= "+limit.toString());
            System.out.println("search= "+search);
            //        Map<String, ?> params = Collections.singletonMap("limitCount", limitCount);
            params = new BeanPropertySqlParameterSource(search);
            return this;


            //        "SELECT board.date, board.great, board.view, board.idx, board.title" +
            //                "   , GROUP_CONCAT(tag.name SEPARATOR '/') AS ctagname" +
            //                "FROM board_tag AS bt RIGHT OUTER JOIN board ON bt.board_idx = board.idx" +
            //                "LEFT OUTER JOIN tag ON bt.tag_idx = tag.idx" +
            //                "INNER JOIN topic ON board.topic_idx = topic.idx" +
            //                "INNER JOIN category ON topic.category_idx = category.idx" +
            //                where +
            ////                    "WHERE category.name='qna' AND topic.name='java' AND board.title LIKE CONCAT('%','제목','%')" +
            //                "GROUP BY board.idx" +
            //                order +
            ////                    "ORDER BY board.date DESC"
            //                limit
        }
    }
}
