package com.example.proyectoincremental.ui.gestionar.fragmentos;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorAsignaturas extends RecyclerView.Adapter<AdaptadorAsignaturas.ViewHolder> implements ListAdapter {

    private Context context;
    private List<Asignatura> asignaturas;
    private int layout;
    private OnItemClickListener itemClickListener;
    private FirebaseUser firebaseUser;

    //Adaptador para carview eventos con imagen fecha , titulo y numero sitios libres
    public AdaptadorAsignaturas(List<Asignatura> asignaturas, Context context, int layout, OnItemClickListener itemListener) {
        this.asignaturas = asignaturas;
        this.layout = layout;
        this.context = context;
        this.itemClickListener = itemListener;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    //traemos la informacion de la BBD
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Asignatura asignatura = asignaturas.get(position);
        holder.nombre.setText(asignatura.getNombre());
        holder.curso.setText(asignatura.getCurso());

        //preguntar a agus si lo cambiamos a string par atraerlo mas facil
        //holder.plazas.setText(eventoss.getFecha());
        holder.bind(asignatura,itemClickListener);


    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView curso, nombre;
        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.texviewNombre);
            curso = (TextView) itemView.findViewById(R.id.texviewCurso);
            img = (ImageView) itemView.findViewById(R.id.imageViewCity);


        }
        //clik al item
        public void bind(final Asignatura asignatura, final OnItemClickListener itemListener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onItemClick(asignatura,getAdapterPosition());
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return asignaturas.size();
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

    public interface OnItemClickListener {
        void onItemClick(Asignatura asignatura, int position);
    }





}
