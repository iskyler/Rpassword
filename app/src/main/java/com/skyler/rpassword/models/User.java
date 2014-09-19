package com.skyler.rpassword.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by skyler on 2014/9/17.
 */
@Table(name = "user")
public class User extends Model {
    @Column(name = "username")
    public String username;

    @Column(name = "root")
    public String root;

    public User() {
        super();
    }

    public User(String username, String root) {
        super();
        this.username = username;
        this.root = root;
    }
}
