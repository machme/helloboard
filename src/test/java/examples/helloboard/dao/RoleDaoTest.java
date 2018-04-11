package examples.helloboard.dao;

import examples.helloboard.config.DbConfig;
import examples.helloboard.domain.Role;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DbConfig.class)
@Transactional
public class RoleDaoTest {
    @Autowired
    DataSource dataSource;
    RoleDao roleDao;

    @Before
    public void setUp() {
        roleDao = new RoleDao(dataSource);
    }

    @Test
    public void config() throws Exception{
        Assert.assertTrue(true);
    }

    @Test
    public void testSelectAll() throws Exception{
        List<Role> roles = roleDao.selectAll();
        Assert.assertEquals(3, roles.size());
        for(Role role: roles){
            System.out.println(role);
        }
    }

    @Test
    public void testInsertRole() throws Exception{
        Role role = new Role();
        role.setRoleId(500);
        role.setDescription("ADMIN");
        int roleId = roleDao.insertRole(role);
        System.out.println(roleId);
    }

    @Test
    public void testUpdateRole() throws Exception{
        // given
        Role role = new Role();
        role.setRoleId(500);
        role.setDescription("ADMIN");
        int roleId = roleDao.insertRole(role);
        Role role1 = roleDao.selectRole(500);

        // when
        role1.setDescription("USER");
        roleDao.updateRole(role1);

        // then
        Role role2 = roleDao.selectRole(500);
        Assert.assertEquals("USER", role2.getDescription());
        System.out.println(roleId);
    }

    @Test
    public void testSelectOne() throws Exception{
        // given
        Role role = new Role();
        role.setRoleId(500);
        role.setDescription("ADMIN");
        int roleId = roleDao.insertRole(role);

        // when
        Role role1 = roleDao.selectRole(500);

        // then
        Assert.assertEquals(role.getRoleId(), role1.getRoleId());
        Assert.assertEquals(role.getDescription(), role1.getDescription());

    }

    @Test
    public void deleteRole() throws Exception{

        // given
        Role role = new Role();
        role.setRoleId(500);
        role.setDescription("ADMIN");
        int roleId = roleDao.insertRole(role);
        List<Role> roles = roleDao.selectAll();
        Assert.assertEquals(4, roles.size());

        // when
        roleDao.deleteRole(500);

        // then
        List<Role> roles2 = roleDao.selectAll();
        Assert.assertEquals(roles2.size(), roles.size() - 1);
    }
}
