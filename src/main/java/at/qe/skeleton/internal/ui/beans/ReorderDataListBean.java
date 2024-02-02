package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.internal.model.PaymentHistory;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.services.PaymentHistoryService;
import at.qe.skeleton.internal.services.UserxService;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.Collection;
import java.util.List;
@Named
@Scope("session")
public class ReorderDataListBean {
    @Autowired
    private FilterListBean filterListBean;
    @Autowired
    private PaymentHistoryService paymentHistoryService;
    @Autowired
    private UserxService userService;
    @Autowired
    private DateSelectionBean dateSelectionBean;
    private List<PaymentHistory> paymentHistoryList;
    private Collection<Userx> userList;
    public List<PaymentHistory> getPaymentHistoryList() {
        if (paymentHistoryList == null){
            paymentHistoryList = paymentHistoryService.getAllByYearAndMonth(dateSelectionBean.getSelectedYear(), dateSelectionBean.getSelectedMonthInt());
        }
        return filterListBean.getPaymentHistoryList(paymentHistoryList);
    }
    public Collection<Userx> getUserList() {
        if (userList == null){
            userList = userService.getAllUsers();
        }
        return filterListBean.getUserList(userList);
    }
}
