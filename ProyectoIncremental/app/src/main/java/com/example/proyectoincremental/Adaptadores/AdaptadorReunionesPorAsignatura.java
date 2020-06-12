package com.example.proyectoincremental.Adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.example.proyectoincremental.Utils.Reuniones;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.proyectoincremental.Adaptadores.AdaptadorAsignaturas.URL_FOTO_USRr;

public class AdaptadorReunionesPorAsignatura extends RecyclerView.Adapter<AdaptadorReunionesPorAsignatura.ViewHolder> implements ListAdapter {
    private Context context;
    private List<Reuniones> listaReuniones;

    private int layout;

    private AdaptadorReunionesPorAsignatura.OnItemClickListener itemClickListener;

    public AdaptadorReunionesPorAsignatura(List<Reuniones> listareuniones, int layout, AdaptadorReunionesPorAsignatura.OnItemClickListener itemListener) {
        this.listaReuniones = listareuniones;
        this.layout = layout;
        this.itemClickListener = itemListener;
    }

    @Override
    public AdaptadorReunionesPorAsignatura.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        AdaptadorReunionesPorAsignatura.ViewHolder vh = new AdaptadorReunionesPorAsignatura.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorReunionesPorAsignatura.ViewHolder holder, final int position) {

        Reuniones reunion = listaReuniones.get(position);

        holder.nombre.setText(reunion.getNumeroGrupo());
        holder.curso.setText(reunion.getHora());

        holder.bind(reunion, itemClickListener);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView curso;
        protected TextView nombre;

        public ViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.texviewNombre);
            curso = (TextView) itemView.findViewById(R.id.texviewCurso);
        }

        //clik al item
        public void bind(final Reuniones reuniones, final AdaptadorReunionesPorAsignatura.OnItemClickListener itemListener) {


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //  itemView.setBackgroundColor(Color.parseColor("#000000"));
                    itemListener.onItemClick(reuniones, getAdapterPosition(),itemView);
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return listaReuniones.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    public interface OnItemClickListener {
        void onItemClick(Reuniones reunion, int position, View itemView);
    }


}