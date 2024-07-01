package com.example.securevault;

public class SavedLoginsRecord
{
    private String username;
    private String password;
    private String url;
    int id;

    public SavedLoginsRecord() {
    }

    public SavedLoginsRecord(String username, String password, String url) {
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
