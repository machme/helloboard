package examples.helloboard.dao;
import examples.helloboard.domain.Board;
import examples.helloboard.domain.Role;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class BoardDao {
    private NamedParameterJdbcTemplate jdbc;
    private SimpleJdbcInsert insertAction;
    private RowMapper<Board> rowMapper = BeanPropertyRowMapper.newInstance(Board.class);

    public BoardDao(DataSource dataSource) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("BOARD");
//                .usingGeneratedKeyColumns("id"); // 자동으로 id가 생성될 경우
    }

    public List<Board> selectAll() {
        return jdbc.query("SELECT idx, title, content, file_idx, writer_idx, date, topic_idx, view, great\n" +
                "FROM board ORDER BY idx asc", Collections.emptyMap(), rowMapper);
    }

    public int insertBoard(Board board){
        SqlParameterSource params = new BeanPropertySqlParameterSource(board);
        // 자동으로 id를 생성할 경우에는 아래와 같이 생성된 pk를 반환할 수 있다.
//        return insertAction.executeAndReturnKey(params).intValue();
        int count = insertAction.execute(params);
        return count;
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

    public Board selectBoard(int boardIdx){
        Map<String, ?> params = Collections.singletonMap("boardIdx", boardIdx);
        return jdbc.queryForObject("SELECT idx, title, content, file_idx, writer_idx, date, topic_idx" +
                ", view, great FROM board ORDER BY idx ASC", params, rowMapper);
    }
}
