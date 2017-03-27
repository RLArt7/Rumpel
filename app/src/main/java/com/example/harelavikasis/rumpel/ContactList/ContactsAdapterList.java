package com.example.harelavikasis.rumpel.ContactList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harelavikasis.rumpel.Listeners.OnContactClicked;
import com.example.harelavikasis.rumpel.Models.Contact;
import com.example.harelavikasis.rumpel.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactsAdapterList  extends RecyclerView.Adapter<ContactsAdapterList.ViewHolder> {

    private Context mContext;
    private List<Contact> contactList = new ArrayList<>();
    private OnContactClicked mlistener;
//    private final View.OnClickListener mOnClickListener = mlistener;


    public ContactsAdapterList(Context context) {
            this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_row, parent, false);
//        View view = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false));
//        view.setOnClickListener(mOnClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            final Contact contact = contactList.get(position);
            holder.contactText.setText(contact.getName());
            holder.contactText.setClickable(true);
            holder.contactText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.notifyThatContactClicked(contact);
                }
            });
    }

    @Override
    public int getItemCount() {
            return contactList.size();
            }

    public void setContacts(List<Contact> contacts , final OnContactClicked listener) {
            this.contactList = contacts;
            this.mlistener = listener;
            notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.contact_list_row)
        TextView contactText;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

