package scheduleapp;

import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    private User user;

    public UserTest(){

    }

    @Before
    public void Setup(){
        user = new User("Test", "Test", 1, 2);
    }
    @After
    public void tearDown() {

    }

    //Test getUserName Method
    @Test
    public void testGetUserName() {
        String expected = "Test";
        String result = user.getUserName();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPassword() {
        String expected = "Test";
        String result = user.getPassword();
        assertEquals(expected, result);
    }

    @Test
    public void testGetActive() {
        int expected = 1;
        int result = user.getActive();
        assertEquals(expected, result);
    }

    @Test
    public void testGetUserId() {
        int expected = 2;
        int result = user.getUserId();
        assertEquals(expected, result);
    }


}
