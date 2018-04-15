package examples.helloboard.dao;

import examples.helloboard.domain.Board;
import examples.helloboard.domain.Search;
import examples.helloboard.domain.Tag;
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
public class TagDao {
    private NamedParameterJdbcTemplate jdbc;
    private SimpleJdbcInsert insertAction;
    private RowMapper<TagInfo> tagInfoRowMapper = BeanPropertyRowMapper.newInstance(TagInfo.class);

    public TagDao(DataSource dataSource) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("TAG")
                .usingGeneratedKeyColumns("idx"); // 자동으로 id가 생성될 경우
    }

    public int insertTag(Tag tag){
        SqlParameterSource params = new BeanPropertySqlParameterSource(tag);
        // 자동으로 id를 생성할 경우에는 아래와 같이 생성된 pk를 반환할 수 있다.
        return insertAction.executeAndReturnKey(params).intValue();
    }

    public int deleteBoard(int tagIdx){
        Map<String, ?> params = Collections.singletonMap("tagIdx", tagIdx);
        return jdbc.update("DELETE FROM tag WHERE idx = :tagIdx", params);
    }

    public int updateBoard(Tag tag){
        SqlParameterSource params = new BeanPropertySqlParameterSource(tag);
        return jdbc.update("UPDATE tag SET idx = :idx , name= :name, ref_count= :refCount", params);
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
