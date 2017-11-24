package com.mail.secure.securemail;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class User extends RealmObject {
    @PrimaryKey
    private String email;
    private String  password;
    private String status;
    private String phone;
    private RealmList<Emails> emails;
    private RealmList<Emails> drafts;
    private RealmList<Emails> inbox;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RealmList<Emails> getEmails() {
        return emails;
    }

    public void setEmails(RealmList<Emails> emails) {
        this.emails = emails;
    }

    public RealmList<Emails> getDrafts() {   ///////////////
        return drafts;
    }

    public void setDrafts(RealmList<Emails> drafts) {
        this.drafts = drafts;
    }

    public RealmList<Emails> getInbox() {
        return inbox;
    }

    public void setInbox(RealmList<Emails> inbox) {
        this.inbox = inbox;
    }
}
