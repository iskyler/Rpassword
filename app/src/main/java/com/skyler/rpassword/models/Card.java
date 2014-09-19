package com.skyler.rpassword.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by skyler on 2014/9/16.
 */

@Table(name = "card", id = BaseColumns._ID)
public class Card extends Model {
    @Column(name = "account_name")
    public String account_name;

    @Column(name = "account_username")
    public String account_username;

    @Column(name = "account_password")
    public String account_password;

    @Column(name = "salt")
    public String salt;

    public Card() {
        super();
    }

    public Card(String account_name, String account_username,
                String account_password, String salt) {
        super();
        this.account_name = account_name;
        this.account_username = account_username;
        this.account_password = account_password;
        this.salt = salt;
    }
}