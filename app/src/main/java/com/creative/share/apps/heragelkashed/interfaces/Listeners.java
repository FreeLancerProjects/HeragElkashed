package com.creative.share.apps.heragelkashed.interfaces;

public interface Listeners {

    interface LoginListener {
        void checkDataLogin();
    }

    interface SkipListener
    {
        void skip();
    }
    interface CreateAccountListener
    {
        void createNewAccount();
    }

    interface ShowCountryDialogListener
    {
        void showDialog();
    }

    interface SignUpListener
    {
        void checkDataSignUp(String name, String phone_code, String phone, String password);

    }

    interface BackListener
    {
        void back();
    }


    interface SliderActionListener
    {
        void call();
        void message();
        void chat();
        void like();
        void favorite();
        void report();
        void facebook();
        void whatsapp();
        void twitter();
        void instagram();
    }

    interface CompanyActionListener
    {
        void call();
        void sms();
        void location();

    }

    interface AboutListener
    {
        void call();
        void sms();
        void email();
        void website();
        void terms();

    }

    interface ContactListener
    {
        void sendContact(String name, String email, String phone_code, String phone, String message);
    }


}
