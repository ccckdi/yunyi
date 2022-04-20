import com.cy.yunyi.portal.PortalApplication;
import com.cy.yunyi.portal.dto.ItemPreferencesDto;
import com.cy.yunyi.portal.service.ItemBaseCacheService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author caihx
 * @Description: 推荐服务测试类
 * @Date 2022/2/14
 */
@SpringBootTest(classes = PortalApplication.class)
public class RecommendTest {

    @Autowired
    private ItemBaseCacheService itemBaseCacheService;

    @Test
    public void testScore(){
        ItemPreferencesDto itemPreferencesDto1 = new ItemPreferencesDto();
        itemPreferencesDto1.setUserId(1l);
        itemPreferencesDto1.setItemId(2l);
        itemPreferencesDto1.setScore(98);
        itemBaseCacheService.putScore(itemPreferencesDto1);
    }

}
