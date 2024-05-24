package com.kerem.Constant;

public class EndPoints {
    public static final String VERSION = "/v1";

    //profiller:
    public static final String API = "/api";
    public static final String DEV = "/dev";
    public static final String TEST = "/test";
    public static final String ROOT = API + VERSION;

    //entityler:
    public static final String AUTH = "/auth";
    public static final String YARISMA = "/yarisma";
    public static final String USER = "/user";
    public static final String SORU = "/soru";
    public static final String USERPROFILE = "/userprofile";

    //Methods:

    public static final String SAVE = "/save";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";
    public static final String FINDALL = "/findall";
    public static final String FINDBYID = "/findbyid";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String ACTIVATE = "/activate";
    public static final String FINDIDBYAUTHID = "/findidbyauthid";

    public static final String FINDBYUSERNAMEREDIS = "/findbyusernameredis";
    public static final String FINDBYSTATUS = "/findbystatus";
}
