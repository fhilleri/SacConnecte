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
