package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.internal.model.FavLocation;
import at.qe.skeleton.internal.model.PaymentHistory;
import at.qe.skeleton.internal.model.Userx;
import jakarta.inject.Named;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
@Named
@Scope("session")
public class FilterListBean implements Serializable {
    private String filterUser;
    private String filterLocation;

    public String getFilterUser() {
        return filterUser;
    }

    public void setFilterUser(String filterUser) {
        this.filterUser = filterUser;
    }

    public String getFilterLocation() {
        return filterLocation;
    }

    public void setFilterLocation(String filterLocation) {
        this.filterLocation = filterLocation;
    }

    public List<FavLocation> getFilteredFavLocations(List<FavLocation> favLocations) {
        if (filterLocation == null || filterLocation.isEmpty()) {
            return favLocations;
        } else {
            return favLocations.stream()
                    .filter(location -> location.getName().toLowerCase().contains(filterLocation.toLowerCase()))
                    .collect(Collectors.toList());
        }
    }

    public List<PaymentHistory> getPaymentHistoryList(List<PaymentHistory> paymentHistoryList) {
        if (filterUser == null || filterUser.isEmpty()) {
            return paymentHistoryList;
        } else {
            return paymentHistoryList.stream()
                    .filter(paymentHistory -> paymentHistory.getUser().getUsername().toLowerCase().contains(filterUser.toLowerCase()) ||
                            paymentHistory.getPaymentStatus().toString().toLowerCase().contains(filterUser.toLowerCase()))
                    .collect(Collectors.toList());
        }
    }

    public Collection<Userx> getUserList(Collection<Userx> userList) {
        if (filterUser == null || filterUser.isEmpty()) {
            return userList;
        } else {
            return userList.stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(filterUser.toLowerCase()) ||
                            user.getFirstName().toLowerCase().contains(filterUser.toLowerCase()) ||
                            user.getLastName().toLowerCase().contains(filterUser.toLowerCase()))
                    .collect(Collectors.toList());
        }
    }
}
