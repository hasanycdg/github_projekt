package at.qe.skeleton.internal.ui.controllers;

import at.qe.skeleton.internal.model.RolChangeLog;
import at.qe.skeleton.internal.services.UserxService;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the user list view.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("view")
public class UserListController implements Serializable {

    @Autowired
    private UserxService userService;

    public List<Long> getIdInRoleChaneLog() {
        return userService.getIdInRoleChaneLog();
    }

    public RolChangeLog getByIdInRoleChaneLog(Long id) {
        return userService.getByIdInRoleChaneLog(id);
    }
}
