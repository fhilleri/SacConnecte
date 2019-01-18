package com.saint_gab.sacconnecte;

public class Equipment
{

    private String mName;
    private String mId;

    public Equipment(String name, String id)
    {
        mName = name;
        mId = id;
    }

    public Equipment(String str)
    {
        String[] arguments = str.split(":");
        mName = arguments[0];
        mId = arguments[1];
    }

    public String getName()
    {
        return mName;
    }

    public String getId()
    {
        return mId;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public void setId(String id)
    {
        mId = id;
    }
}
