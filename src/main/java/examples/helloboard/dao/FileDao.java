package examples.helloboard.dao;
import examples.helloboard.domain.File;
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
public class FileDao {
    private NamedParameterJdbcTemplate jdbc;
    private SimpleJdbcInsert insertAction;
    private RowMapper<File> rowMapper = BeanPropertyRowMapper.newInstance(File.class);

    public FileDao(DataSource dataSource) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("FILE")
                .usingGeneratedKeyColumns("idx"); // 자동으로 id가 생성될 경우
    }

//    public List<Role> selectAll() {
//        return jdbc.query("select role_id, description from ROLE order by role_id", Collections.emptyMap(), rowMapper);
//    }

    public int insertFile(File file){
        SqlParameterSource params = new BeanPropertySqlParameterSource(file);
        // 자동으로 id를 생성할 경우에는 아래와 같이 생성된 pk를 반환할 수 있다.
        return insertAction.executeAndReturnKey(params).intValue();
    }

    public int deleteFile(int fileIdx){
        Map<String, ?> params = Collections.singletonMap("fileIdx", fileIdx);
        return jdbc.update("DELETE FROM file WHERE idx = :fileIdx", params);
    }

    public int updateFile(File file){
        StringBuilder sql = new StringBuilder(
                "UPDATE file SET name = :name, path = :path, size = :size, format = :format" +
                " WHERE idx = :idx");
        SqlParameterSource params = new BeanPropertySqlParameterSource(file);
        return jdbc.update(sql.toString(), params);
    }

    public File selectFile(int boardIdx){
        StringBuilder sql = new StringBuilder(
                "SELECT idx, name, path, size, format FROM file WHERE board_idx = :boardIdx"
        );
        Map<String, ?> params = Collections.singletonMap("boardIdx", boardIdx);
        return jdbc.queryForObject(sql.toString(), params, rowMapper);
    }
}
