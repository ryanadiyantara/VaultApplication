package adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaultapplication.R;
import com.example.vaultapplication.Update;

import java.io.Serializable;
import java.util.ArrayList;

import db.DbHelper;
import model.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.OrderViewHolder> {

    private ArrayList<Item> listItem = new ArrayList<>();
    private Activity activity;
    private DbHelper dbHelper;

    public ItemAdapter(Activity activity) {
        this.activity = activity;
        dbHelper = new DbHelper(activity);
    }

    public ArrayList<Item> getListItem() {
        return listItem;
    }

    public void setListItem(ArrayList<Item> listNotes) {
        if (listNotes.size() > 0) {
            this.listItem.clear();
        }
        this.listItem.addAll(listNotes);
        notifyDataSetChanged();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        final TextView item_name, item_price;
        final ImageView item_pict;
        final Button btn_edit;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.name);
            item_price = itemView.findViewById(R.id.price);
            item_pict = itemView.findViewById(R.id.pict);

            btn_edit = itemView.findViewById(R.id.edit);
        }
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_adapter, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        holder.item_name.setText(listItem.get(position).getItem_name());
        holder.item_price.setText(listItem.get(position).getItem_price());
        byte[] gunImage = listItem.get(position).getItem_pict();
        Bitmap bitmap = BitmapFactory.decodeByteArray(gunImage, 0, gunImage.length);
        holder.item_pict.setImageBitmap(bitmap);

        holder.btn_edit.setOnClickListener((View v) -> {
            Intent intent = new Intent(activity, Update.class);
            intent.putExtra("item", (Serializable) listItem.get(position));
            activity.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
