package at.qe.skeleton.tests;

import at.qe.skeleton.internal.model.PremiumHistory;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.services.PremiumHistoryService;
import at.qe.skeleton.internal.services.UserxService;
import at.qe.skeleton.internal.ui.controllers.PremiumStatusListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest
@WebAppConfiguration
public class PremiumHistoryTest {


    @Autowired
    private PremiumHistoryService premiumHistoryService;

    @Autowired
    private UserxService userxService;

    @Autowired
    private PremiumStatusListener premiumStatusListener;

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    @DirtiesContext
    public void testPremStatusAendernDbEintrag() {
        String testUser = "testUserPrem";
        Userx user = new Userx();
        user.setUsername(testUser);
        user.setPassword("passwd");
        user.setRoles(Set.of(UserxRole.USER));
        user.setPremium(false);
        userxService.saveUser(user);

        List<PremiumHistory> firstResult = premiumHistoryService.getPremiumChangedByName(testUser);
        Assertions.assertNull(firstResult.isEmpty() ? null : firstResult);

        premiumHistoryService.savePremiumHistory(user, true);

        List<PremiumHistory> secondResult = premiumHistoryService.getPremiumChangedByName(testUser);
        Assertions.assertNotNull(secondResult.isEmpty() ? null : secondResult);
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    @DirtiesContext
    public void testPremStatusAendernChangeDate() {
        String testUser = "testUserPrem";
        Userx user = new Userx();
        user.setUsername(testUser);
        user.setPassword("passwd");
        user.setRoles(Set.of(UserxRole.USER));
        user.setPremium(false);
        userxService.saveUser(user);

        premiumHistoryService.savePremiumHistory(user, true);

        List<PremiumHistory> firstResult = premiumHistoryService.getPremiumChangedByName(testUser);
        Assertions.assertNotNull(firstResult.isEmpty() ? null : firstResult);

        Duration duration = Duration.between(firstResult.get(0).getChangeDate(), LocalDateTime.now());
        Assertions.assertTrue(duration.toSeconds() < 0.1);
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    @DirtiesContext
    public void testPremStatusAendernNewStaus() {
        String testUser = "testUserPrem";
        Boolean newStatus = false;
        Userx user = new Userx();
        user.setUsername(testUser);
        user.setPassword("passwd");
        user.setRoles(Set.of(UserxRole.USER));
        user.setPremium(newStatus);
        userxService.saveUser(user);

        premiumHistoryService.savePremiumHistory(user, !newStatus);
        premiumHistoryService.savePremiumHistory(user, newStatus);

        List<PremiumHistory> firstResult = premiumHistoryService.getPremiumChangedByName(testUser);
        Assertions.assertNotNull(firstResult.isEmpty() ? null : firstResult);
        Assertions.assertTrue(firstResult.get(0).getNewPremiumStatus());
        Assertions.assertFalse(firstResult.get(1).getNewPremiumStatus());
    }

    /*@Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    @DirtiesContext
    public void testUserCascadeDeletion() {
        String testUser = "testUserPrem";
        Boolean newStatus = false;
        Userx user = new Userx();
        user.setUsername(testUser);
        user.setPassword("passwd");
        user.setRoles(Set.of(UserxRole.USER));
        user.setPremium(newStatus);
        userxService.saveUser(user);

        premiumHistoryService.savePremiumHistory(user, !newStatus);
        premiumHistoryService.savePremiumHistory(user, newStatus);
        premiumHistoryService.savePremiumHistory(user, !newStatus);
        premiumHistoryService.savePremiumHistory(user, newStatus);
        Assertions.assertEquals(4, premiumHistoryService.getPremiumChangedByName(testUser).toArray().length);

        userxService.deleteUser(user);

        Assertions.assertEquals(0, premiumHistoryService.getPremiumChangedByName(testUser).toArray().length);
    }*/

    private static Stream<Arguments> provideSleepDurations() {
        //all in Seconds because the time Metric is in Seconds as well
        return Stream.of(
                Arguments.of(1000, 2000),
                Arguments.of(3400, 1500),
                Arguments.of(1500, 3000),
                Arguments.of(2500, 4500),
                Arguments.of(3400, 6500),
                Arguments.of(1200, 4500)
        );
    }

    @ParameterizedTest
    @MethodSource("provideSleepDurations")
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    @DirtiesContext
    public void testPremiumIntervallLaenge(int sleep1, int sleep2) {
        try {
            String testUser = "testUserPrem";
            Userx user = new Userx();
            user.setUsername(testUser);
            user.setPassword("passwd");
            user.setRoles(Set.of(UserxRole.USER));
            user.setPremium(true);
            LocalDateTime time1 = LocalDateTime.now();
            userxService.saveUser(user);

            Thread.sleep(sleep1);

            user.setPremium(false);
            LocalDateTime time2 = LocalDateTime.now();
            userxService.saveUser(user);

            user.setPremium(true);
            LocalDateTime time3 = LocalDateTime.now();
            userxService.saveUser(user);

            Thread.sleep(sleep2);

            user.setPremium(false);
            LocalDateTime time4 = LocalDateTime.now();
            userxService.saveUser(user);


            Duration duration1 = Duration.between(time1, time2);

            Duration duration2 = Duration.between(time3, time4);

            List<Integer> timeList = premiumStatusListener.getTimePremiumInterval(premiumStatusListener.getPremiumIntervalByName(user));
            if (timeList.isEmpty()) {
                Assertions.fail("Not enough in Data in user.");
            }
            Assertions.assertEquals(duration1.toSeconds(), (long) timeList.get(0));
            Assertions.assertEquals(duration2.toSeconds(), (long) timeList.get(1));

        } catch (InterruptedException e) {
            // Handle interrupted exception if needed
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @MethodSource("provideSleepDurations")
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    @DirtiesContext
    public void testTotalPremiumTime(int sleep1, int sleep2) {
        try {
            String testUser = "testUserPrem";
            Userx user = new Userx();
            user.setUsername(testUser);
            user.setPassword("passwd");
            user.setRoles(Set.of(UserxRole.USER));
            user.setPremium(true);
            LocalDateTime time1 = LocalDateTime.now();
            userxService.saveUser(user);

            Thread.sleep(sleep1);

            user.setPremium(false);
            LocalDateTime time2 = LocalDateTime.now();
            userxService.saveUser(user);

            user.setPremium(true);
            LocalDateTime time3 = LocalDateTime.now();
            userxService.saveUser(user);

            Thread.sleep(sleep2);

            user.setPremium(false);
            LocalDateTime time4 = LocalDateTime.now();
            userxService.saveUser(user);

            Duration duration1 = Duration.between(time1, time2);
            Duration duration2 = Duration.between(time3, time4);

            Integer totalTime = premiumStatusListener.getTotalPremiumTimeByName(user);
            Assertions.assertEquals((long) totalTime, duration1.toSeconds() + duration2.toSeconds());

        } catch (InterruptedException e) {
            // Handle interrupted exception if needed
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    @DirtiesContext
    public void testTotalPremiumTimeOverMonth() {
        String testUser = "testUserPrem";
        Userx user = new Userx();
        user.setUsername(testUser);
        user.setPassword("passwd");
        user.setRoles(Set.of(UserxRole.USER));
        user.setPremium(true);
        userxService.saveUser(user);
        user.setPremium(false);
        userxService.saveUser(user);
        user.setPremium(true);
        userxService.saveUser(user);
        user.setPremium(false);
        userxService.saveUser(user);
        user.setPremium(true);
        userxService.saveUser(user);
        user.setPremium(false);
        userxService.saveUser(user);

        //write Random dates spanning over multiple months in the Premium History's
        List<PremiumHistory> datesList = premiumStatusListener.getPremiumIntervalByName(user);
        List<PremiumHistory> newDates = changeTheChangeDatesToMultipelMonths(datesList);

        Integer chargedDays = premiumStatusListener.chargedDaysFromStartToEndCurrentMonth(newDates);

        List<Integer> timeIntervals = premiumStatusListener.getTimePremiumInterval(newDates);
        if (timeIntervals.isEmpty()) {
            Assertions.fail("Not enough in Data in newDates.");
        }
        Assertions.assertEquals(timeIntervals.stream().mapToInt(Integer::intValue).sum(), chargedDays);
    }

    private List<PremiumHistory> changeTheChangeDatesToMultipelMonths(List<PremiumHistory> datesList) {
        List<LocalDateTime> dateTimeRange = new ArrayList<>();
        Random random = new Random();

        LocalDateTime startDateTime = LocalDateTime.of(1950, 8, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 12, 1, 0, 0);

        while (!startDateTime.isAfter(endDateTime)) {
            // Add a random time within the day
            int hour = random.nextInt(24);
            int minute = random.nextInt(60);
            int second = random.nextInt(60);

            LocalDateTime randomDateTime = startDateTime.withHour(hour).withMinute(minute).withSecond(second);
            dateTimeRange.add(randomDateTime);

            startDateTime = startDateTime.plusMonths(random.nextInt(2, 25));
        }

        int i = 0;
        for (PremiumHistory premHist : datesList) {
            premHist.setChangeDate(dateTimeRange.get(i));
            i++;
        }
        return datesList;
    }

}
