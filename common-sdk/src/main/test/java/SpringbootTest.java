import com.roy.common.sdk.CommonSdkApplication;
import com.roy.common.sdk.redis.RedisOperationHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(classes = CommonSdkApplication.class)
public class SpringbootTest {

    @Autowired
    public RedisOperationHelper redisOperationHelper;

    @Test
    public void test(){
        redisOperationHelper.set("mzr","chenlin");
        System.out.println(redisOperationHelper.get("mzr"));
    }
}
