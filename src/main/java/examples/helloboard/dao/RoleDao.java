package examples.helloboard.dao;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RoleDao {
    private NamedParameterJdbcTemplate jdbc;
    private SimpleJdbcInsert insertAction;
    private RowMapper<Role> rowMapper = BeanPropertyRowMapper.newInstance(Role.class);

    public RoleDao(DataSource dataSource) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("ROLE");
//                .usingGeneratedKeyColumns("id"); // 자동으로 id가 생성될 경우
    }

    public List<Role> selectAll() {
        return jdbc.query("select role_id, description from ROLE order by role_id", Collections.emptyMap(), rowMapper);
    }

    public int insertRole(Role role){
        SqlParameterSource params = new BeanPropertySqlParameterSource(role);
        // 자동으로 id를 생성할 경우에는 아래와 같이 생성된 pk를 반환할 수 있다.
//        return insertAction.executeAndReturnKey(params).intValue();
        int count = insertAction.execute(params);
        return role.getRoleId();
    }

    public int deleteRole(int roleId){
        Map<String, ?> params = Collections.singletonMap("roleId", roleId);
        return jdbc.update("delete from ROLE where role_id = :roleId", params);
    }

    public int updateRole(Role role){
        SqlParameterSource params = new BeanPropertySqlParameterSource(role);
        return jdbc.update("update ROLE set description = :description where role_id = :roleId", params);
    }

    public Role selectRole(int roleId){
        Map<String, ?> params = Collections.singletonMap("roleId", roleId);
        return jdbc.queryForObject("select role_id, description from ROLE where role_id = :roleId", params, rowMapper);
    }
}
