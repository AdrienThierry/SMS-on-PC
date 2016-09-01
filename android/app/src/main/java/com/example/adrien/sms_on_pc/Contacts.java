/**
 * Contacts related functions
 */

package com.example.adrien.sms_on_pc;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Contacts {

    private static Context mContext;

    // --------------------------------------------------
    // !! MUST BE CALLED BEFORE ANY OTHER FUNCTION OF
    // THIS CLASS !!
    // --------------------------------------------------
    public static void initialize(Context context) {
        mContext = context;
    }

    // --------------------------------------------------
    // Get HashMap of contacts in phone (id -> display_name)
    // --------------------------------------------------
    public static LinkedHashMap<String, String> getContacts() {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        ContentResolver cr = mContext.getContentResolver();

        Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME},
                null,
                null,
                null); // Sort order

        int totalContacts = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalContacts; i++) {
                result.put(c.getString(0), c.getString(1));
                c.moveToNext();
            }
        } else {
            throw new RuntimeException("Contacts : no match found");
        }
        c.close();

        return result;
    }
}
