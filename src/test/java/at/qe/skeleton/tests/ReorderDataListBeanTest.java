package at.qe.skeleton.tests;

import at.qe.skeleton.internal.model.PaymentHistory;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.services.PaymentHistoryService;
import at.qe.skeleton.internal.services.UserxService;
import at.qe.skeleton.internal.ui.beans.DateSelectionBean;
import at.qe.skeleton.internal.ui.beans.FilterListBean;
import at.qe.skeleton.internal.ui.beans.ReorderDataListBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReorderDataListBeanTest {

    @Mock
    private FilterListBean filterListBean;

    @Mock
    private PaymentHistoryService paymentHistoryService;

    @Mock
    private UserxService userService;

    @Mock
    private DateSelectionBean dateSelectionBean;

    @InjectMocks
    private ReorderDataListBean reorderDataListBean;

    @Test
    public void testGetPaymentHistoryList() {
        // Mock data
        List<PaymentHistory> mockPaymentHistoryList = Arrays.asList(/* add sample PaymentHistory objects */);
        when(dateSelectionBean.getSelectedYear()).thenReturn(2023);
        when(dateSelectionBean.getSelectedMonthInt()).thenReturn(5);
        when(paymentHistoryService.getAllByYearAndMonth(2023, 5)).thenReturn(mockPaymentHistoryList);
        when(filterListBean.getPaymentHistoryList(mockPaymentHistoryList)).thenReturn(mockPaymentHistoryList);

        // Call the method
        List<PaymentHistory> result = reorderDataListBean.getPaymentHistoryList();

        // Verify the behavior
        verify(paymentHistoryService, times(1)).getAllByYearAndMonth(2023, 5);
        verify(filterListBean, times(1)).getPaymentHistoryList(mockPaymentHistoryList);
        assertEquals(mockPaymentHistoryList, result);
    }

    @Test
    public void testGetUserList() {
        // Mock data
        Userx user1 = new Userx();
        Userx user2 = new Userx();
        user1.setUsername("user1");
        user2.setUsername("user2");
        Collection<Userx> mockUserList = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(mockUserList);
        when(filterListBean.getUserList(mockUserList)).thenReturn(mockUserList);

        // Call the method
        Collection<Userx> result = reorderDataListBean.getUserList();

        // Verify the behavior
        verify(userService, times(1)).getAllUsers();
        verify(filterListBean, times(1)).getUserList(mockUserList);
        assertEquals(mockUserList, result);
    }
}
