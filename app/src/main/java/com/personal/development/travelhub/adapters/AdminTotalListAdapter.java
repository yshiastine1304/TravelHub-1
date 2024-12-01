package com.personal.development.travelhub.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.personal.development.travelhub.R;
import com.personal.development.travelhub.models.User;

import java.util.ArrayList;
import java.util.List;

public class AdminTotalListAdapter extends RecyclerView.Adapter<AdminTotalListAdapter.AdminViewHolder> implements Filterable {
    private Context context;
    private List<User> userList;
    private List<User> userListFull;

    private FirebaseFirestore firestore;
    public AdminTotalListAdapter(Context context){
        this.context = context;
        this.userList = new ArrayList<>();
        this.userListFull = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        retrieveUsers();
    }

    public void retrieveUsers() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("access_type", Context.MODE_PRIVATE);
        String access = sharedPreferences.getString("getAcces", "Unknown");

        Query usersRef; // Change the type to Query
        if ("agency".equals(access)) {
            usersRef = firestore.collection("users").whereEqualTo("access", "agency");
        } else {
            usersRef = firestore.collection("users").whereEqualTo("access", "user");
        }

        Toast.makeText(context, "user type: "+ access, Toast.LENGTH_SHORT).show();

        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();
                if (snapshots != null) {
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        User user = doc.toObject(User.class);
                        userList.add(user);
                        userListFull.add(user);
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_userlist_layout, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        User user = userList.get(position);
        holder.fullNameTextView.setText(user.getFullName());
        holder.emailTextView.setText(user.getEmail());
        holder.contactTextView.setText(user.getContactNumber());
        holder.interestTextView.setText(user.getInterest());
    }

    @Override
    public int getItemCount() {
       return userList.size();
    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView fullNameTextView, emailTextView, contactTextView, interestTextView;

        public AdminViewHolder(@NonNull View itemView){
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.fullname_user);
            emailTextView = itemView.findViewById(R.id.email_user);
            contactTextView = itemView.findViewById(R.id.contact_user);
            interestTextView = itemView.findViewById(R.id.interest_user);
        }
    }

    @Override
    public Filter getFilter() {
       return userFilter;
    }

    private final Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           List<User> filteredList = new ArrayList<>();

           if (constraint == null || constraint.length() == 0){
               filteredList.addAll(userListFull);
           } else {
               String filterPattern = constraint.toString().toLowerCase().trim();

               for (User user : userListFull){
                   if (user.getFullName().toLowerCase().contains(filterPattern) ||
                           user.getEmail().toLowerCase().contains(filterPattern) ||
                           user.getContactNumber().toLowerCase().contains(filterPattern) ||
                           user.getInterest().toLowerCase().contains(filterPattern)) {
                       filteredList.add(user);
                   }
               }

           }

           FilterResults results = new FilterResults();
           results.values = filteredList;
           return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userList.clear();
            userList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
