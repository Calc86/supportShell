package com.user;

import com.ui.gui.UserForm;

import javax.swing.*;
import java.awt.*;

/**
 * Created by calc on 02.04.14.
 *
 */
public class User {
    private static final User instance = new User();
    private Client client;
    static UserForm form;
    static JFrame frame;

    public void setClient(Client client) {
        this.client = client;
    }

    public static User getInstance() {
        return instance;
    }

    public Client getClient() {
        return client;
    }

    public static void main(String[] args) {
        crateUserForm();

        User.getInstance().setClient(new Client(form));
        User.getInstance().getClient().work();
        frame.setVisible(false);
        frame.dispose();
    }

    private static void crateUserForm() {
        frame = new JFrame("UserForm");
        form = new UserForm();
        frame.setContentPane(form.getPanel());
        frame.setPreferredSize(new Dimension(500,400));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
