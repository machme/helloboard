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

    public List<BoardInfo> selectBoardList(Search search, List<String> orderList){
        AdditionalSQL additionalSQL = new AdditionalSQL(search, orderList);
        SqlParameterSource params = new BeanPropertySqlParameterSource(search);

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
                .append(additionalSQL.makeWhereSQL())
                .append("group by b.idx\n")
                .append(additionalSQL.makeOrderSQL())
                .append(additionalSQL.makeLimitSQL());

        return jdbc.query(sql.toString(), params, boardInfoRowMapper);
    }

    public List<TagInfo> selectTagList(Search search, List<String> orderList) {
        AdditionalSQL additionalSQL = new AdditionalSQL(search, orderList);
        SqlParameterSource params = new BeanPropertySqlParameterSource(search);

        StringBuilder sql = new StringBuilder("select tag.idx, tag.name,\n" +
                "b.idx board_idx\n" +
                "from board b\n" +
                "join topic t on b.topic_idx = t.idx\n" +
                "join category c on t.category_idx = c.idx\n" +
                "join user u on b.user_idx = u.idx\n" +
                "left outer join board_tag bt on b.idx = bt.board_idx\n" +
                "left outer join tag on bt.tag_idx = tag.idx")
                .append(additionalSQL.makeWhereSQL())
                .append(additionalSQL.makeOrderSQL())
                .append(additionalSQL.makeLimitSQL());

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

}
