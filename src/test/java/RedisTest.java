import com.lyz.dao.IUserDao;
import com.lyz.entity.User;
import junit.framework.Assert;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ≤‚ ‘¿‡
 * Created by Administrator on 2018/6/11.
 */
public class RedisTest {
    @Autowired
    private IUserDao userDao;

    public void testAddUser(){
        User user =new User();
        user.setId("user1");
        user.setName("zcc");
        boolean result=userDao.add(user);
        System.out.println(result);
        //Assert.assertTrue(result);
    }

    public static void main(String[] args) {
        RedisTest redisTest=new RedisTest();
        redisTest.testAddUser();
    }
}
