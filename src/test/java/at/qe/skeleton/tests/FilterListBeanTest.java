package at.qe.skeleton.tests;

import at.qe.skeleton.internal.model.FavLocation;
import at.qe.skeleton.internal.model.PaymentHistory;
import at.qe.skeleton.internal.model.PaymentStatus;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.ui.beans.FilterListBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WebAppConfiguration
public class FilterListBeanTest {

    @Autowired
    private FilterListBean filterListBean;

    @Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    public void testGetFilteredFavLocations() {
        //create user
        Userx user = new Userx();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        //create Favorite Locations
        FavLocation favLocation1 = new FavLocation();
        FavLocation favLocation2 = new FavLocation();
        FavLocation favLocation3 = new FavLocation();
        favLocation1.setId(1L);
        favLocation1.setUser(user);
        favLocation1.setIndex(0);
        favLocation1.setName("Location1");
        favLocation2.setId(2L);
        favLocation2.setIndex(1);
        favLocation2.setUser(user);
        favLocation2.setName("Location2");
        favLocation3.setId(3L);
        favLocation3.setIndex(2);
        favLocation3.setName("Location3");
        favLocation3.setUser(user);
        List<FavLocation> favLocations = Arrays.asList(favLocation1, favLocation2, favLocation3);

        //Test when filterValue is null
        filterListBean.setFilterLocation(null);
        assertEquals(favLocations, filterListBean.getFilteredFavLocations(favLocations));

        // Test when filterValue is empty
        filterListBean.setFilterLocation("");
        assertEquals(favLocations, filterListBean.getFilteredFavLocations(favLocations));

        // Test when filterValue is not empty
        filterListBean.setFilterLocation("Location");
        List<FavLocation> filteredLocations1 = filterListBean.getFilteredFavLocations(favLocations);
        assertEquals(3, filteredLocations1.size());

        filterListBean.setFilterLocation("Location1");
        List<FavLocation> filteredLocations2 = filterListBean.getFilteredFavLocations(favLocations);
        assertEquals(1, filteredLocations2.size());
        assertEquals("Location1", filteredLocations2.get(0).getName());

    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    public void testGetPaymentHistoryList() {
        Userx user1 = new Userx();
        Userx user2 = new Userx();
        user1.setUsername("user1");
        user2.setUsername("user2");


        PaymentHistory payment1 = new PaymentHistory();
        PaymentHistory payment2 = new PaymentHistory();
        payment1.setUser(user1);
        payment1.setPaymentStatus(PaymentStatus.PAYED);
        payment2.setUser(user2);
        payment2.setPaymentStatus(PaymentStatus.PAYED);

        List<PaymentHistory> paymentHistoryList = Arrays.asList(payment1, payment2);

        // Test without filtering
        filterListBean.setFilterUser(null);
        List<PaymentHistory> result = filterListBean.getPaymentHistoryList(paymentHistoryList);
        assertEquals(paymentHistoryList, result);

        // Test when filterValue is empty
        filterListBean.setFilterUser("");
        result = filterListBean.getPaymentHistoryList(paymentHistoryList);
        assertEquals(paymentHistoryList, result);

        // Test with filtering
        filterListBean.setFilterUser("user1");
        result = filterListBean.getPaymentHistoryList(paymentHistoryList);
        assertEquals(Arrays.asList(payment1), result);

        filterListBean.setFilterUser("paye");
        result = filterListBean.getPaymentHistoryList(paymentHistoryList);
        assertEquals(paymentHistoryList, result);
    }

    @Test
    public void testGetUserList() {
        Userx user1 = new Userx();
        Userx user2 = new Userx();
        user1.setUsername("user1");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user2.setUsername("user2");
        user2.setFirstName("Lisa");
        user2.setLastName("Müller");

        Collection<Userx> userList = Arrays.asList(user1, user2);

        // Test without filtering
        Collection<Userx> result = filterListBean.getUserList(userList);
        assertEquals(userList, result);

        // Test when filterValue is empty
        filterListBean.setFilterUser("");
        result = filterListBean.getUserList(userList);
        assertEquals(userList, result);

        // Test with filtering
        filterListBean.setFilterUser("John");
        result = filterListBean.getUserList(userList);
        assertEquals(Arrays.asList(user1), result);
    }

}
