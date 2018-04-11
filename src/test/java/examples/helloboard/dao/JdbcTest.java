package examples.helloboard.dao;

import examples.helloboard.config.DbConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DbConfig.class)
@Transactional
public class JdbcTest {
    @Autowired
    DataSource dataSource;

    @Test
    public void testDataSource(){
        Assert.assertNotNull(dataSource);
    }

    @Test
    public void jdbcInsertTest(){
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "insert into ROLE(role_id,description) values(?, ?)";

        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 500);
            ps.setString(2, "ADMIN");
            int count = ps.executeUpdate();
            System.out.println("count : " + count);
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    } // insert test

    @Test
    public void jdbcUpdateTest(){
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "update ROLE set description = ? where role_id = ?";

        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);

            ps.setString(1, "ADMIN");
            ps.setInt(2, 100);
            int count = ps.executeUpdate();
            System.out.println("update count : " + count);
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    } // update test

    @Test
    public void jdbcDelteTest(){
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "delete from ROLE where role_id = ?";

        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 100);

            int count = ps.executeUpdate();
            System.out.println("delete count : " + count);
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    } // delete test

    @Test
    public void jdbcSelectOneTest(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select role_id,description from ROLE where role_id = ?";

        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 100);

            rs = ps.executeQuery();
            if(rs.next()){
                int roleId = rs.getInt(1);
                String description = rs.getString(2);
                System.out.println(roleId);
                System.out.println(description);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    } // select one test

    @Test
    public void jdbcSelectAllTest(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select role_id,description from ROLE order by role_id";

        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();
            while(rs.next()){
                int roleId = rs.getInt(1);
                String description = rs.getString(2);
                System.out.println(roleId);
                System.out.println(description);
                System.out.println("----------------------------------------");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    } // select all
}
