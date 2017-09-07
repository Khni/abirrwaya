package com.khaled.aberrwaya;

/**
 * Created by khaled on 17/08/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by Khaled on 8/17/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ItemClickListener mItemClickListener;
    DB_Sqlite_Favorite db_fav;
    ArrayList<StoryModelM> arrayList = new ArrayList<>();
    public Context context;
    ArrayList<StoryModel> List_favorite = new ArrayList<>();





    MyAdapter(ArrayList<StoryModelM> arrayList){
        this.arrayList=arrayList;
        //db_fav = new DB_Sqlite_Favorite(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

        return new  MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {

        holder.Listindex.setText(arrayList.get(position).getTitleModel());
       holder.ListContent.setText(arrayList.get(position).getContentModel());

        String mTitle =holder.Listindex.getText().toString();

        Context context =holder.itemView.getContext();


        SharedPreferences sharedPreferences = context.getSharedPreferences("tgpref1",Context.MODE_PRIVATE);
        boolean tgpref = sharedPreferences.getBoolean("tgpref"+position,false);


        if (tgpref){
           holder.toggleButton.setChecked(true);
        }else {
           holder.toggleButton.setChecked(false);
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public  class  MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Listindex;
      TextView ListContent;
        ToggleButton toggleButton;






        public MyViewHolder(final View itemView) {
            super(itemView);

            final Context context = itemView.getContext();
            db_fav = new DB_Sqlite_Favorite(context);
            List_favorite = db_fav.getAllFavorite();















                Listindex =(TextView) itemView.findViewById(R.id.TextViewTitleID);
           ListContent = (TextView)itemView.findViewById(R.id.TextViewContentID);
            toggleButton =(ToggleButton)itemView.findViewById(R.id.ToggleHeartID);
            String mTitle = Listindex.getText().toString();

            //not working

            int check = db_fav.get_check_List_Favorite(mTitle);
            if (check>0){
                toggleButton.setChecked(true);
            }else {
                toggleButton.setChecked(false);
            }



            itemView.setOnClickListener(this);
           toggleButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   int position = getAdapterPosition();

                       if (toggleButton.isChecked()) {
                           String mTitle = Listindex.getText().toString();
                           String mContent =ListContent.getText().toString();

                           //Toast.makeText(context, mTitle, Toast.LENGTH_SHORT).show();
                          int check = db_fav.get_check_List_Favorite(mTitle);
                          if (check>0){
                               Toast.makeText(context, "عفوا العنوان موجود بالمفضلة", Toast.LENGTH_SHORT).show();
                           }else {
                               db_fav.Insert_to_favorite(mTitle,mContent,position);
                              Toast.makeText(context, "تم الاضافة الي المفضلة", Toast.LENGTH_SHORT).show();
                           }



                           Animation plus = AnimationUtils.loadAnimation(v.getContext(), R.anim.pulse);
                           toggleButton.startAnimation(plus);
                          SharedPreferences sharedPreferences = context.getSharedPreferences("tgpref1", Context.MODE_PRIVATE);
                           SharedPreferences.Editor editor = sharedPreferences.edit();
                           editor.putBoolean("tgpref"+position, true);

                           editor.commit();



                       }


                       else {

                         /*  int x;
                           for (x = 0; x<List_favorite.size(); x++) {
                               int idint = List_favorite.get(x).getIdrow();
                               String idfinal = String.valueOf(idint+position);

                           }*/

                           db_fav.DeletRow(Listindex.getText().toString());
                           Toast.makeText(context, "تم الازلالة من المفضلة", Toast.LENGTH_SHORT).show();







                           SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("tgpref1", Context.MODE_PRIVATE);
                           SharedPreferences.Editor editor = sharedPreferences.edit();

                           editor.putBoolean("tgpref"+position, false);
                           editor.commit();









                       }


               }
           });





        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener!=null)
                mItemClickListener.onItemClick(itemView, getAdapterPosition(), getItemId());


            }




    }

    public void setFilter(ArrayList<StoryModelM> newList) {

        arrayList= new ArrayList<>();
        arrayList.addAll(newList);
        notifyDataSetChanged();
    }
    public void setmItemClickListener(ItemClickListener itemClickListener ){
        this.mItemClickListener= itemClickListener;
    }




    public interface ItemClickListener {
        void onItemClick(View view, int position , long id );

    }

    public interface ToggleInterface {
        void toggleonClick(View view);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
