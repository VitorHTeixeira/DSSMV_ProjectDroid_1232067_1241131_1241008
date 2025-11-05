package com.lvhm.covertocover.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserToken {

    @SerializedName("_id")
    private String _id;

    @Expose
    private String token;

    @Expose
    private long createdAt;

    @Expose
    private long lastSync;

    public UserToken() {}

    public UserToken(String token) {
        this.token = token;
        this.createdAt = System.currentTimeMillis();
        this.lastSync = System.currentTimeMillis();
    }

    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getLastSync() { return lastSync; }
    public void setLastSync(long lastSync) { this.lastSync = lastSync; }
}
