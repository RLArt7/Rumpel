package com.example.harelavikasis.rumpel.Listeners;

import android.view.View;

import com.example.harelavikasis.rumpel.Models.Contact;

/**
 * Created by harelavikasis on 27/03/2017.
 */

public interface OnContactClicked {//extends View.OnClickListener{
    void notifyThatContactClicked(Contact contact);
}
