import com.github.cookiesjuice.service.TagLocalizationService;
import com.github.cookiesjuice.service.impl.TagLocalizationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LocalizationServiceUnitTest {

    private TagLocalizationService tagLocalizationService;


    @Before
    @Test
    public void testInitialize(){
        try{
            tagLocalizationService = new TagLocalizationServiceImpl();
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void testTranslateExist(){
        assertEquals("1男", tagLocalizationService.translate("1boy"));
    }

    @Test
    public void testTranslateNotExist(){
        assertEquals("1koma", tagLocalizationService.translate("1koma"));
    }
}
