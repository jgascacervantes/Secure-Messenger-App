package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "nftapp-mobilehub-2060986407-Users")

public class UsersDO {
    private String _username;
    private String _deviceID;
    private String _password;
    private Boolean _rights;

    @DynamoDBHashKey(attributeName = "Username")
    @DynamoDBAttribute(attributeName = "Username")
    public String getUsername() {
        return _username;
    }

    public void setUsername(final String _username) {
        this._username = _username;
    }
    //@DynamoDBRangeKey(attributeName = "Device ID")
    @DynamoDBAttribute(attributeName = "Device ID")
    public String getDeviceID() {
        return _deviceID;
    }

    public void setDeviceID(final String _deviceID) {
        this._deviceID = _deviceID;
    }
    @DynamoDBAttribute(attributeName = "Password")
    public String getPassword() {
        return _password;
    }

    public void setPassword(final String _password) {
        this._password = _password;
    }
    @DynamoDBAttribute(attributeName = "Rights")
    public Boolean getRights() {
        return _rights;
    }

    public void setRights(final Boolean _rights) {
        this._rights = _rights;
    }

}
