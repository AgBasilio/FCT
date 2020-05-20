package com.example.proyectoincremental.ui.gestionar.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoincremental.Activity.EditarAsignaturasActivity;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.example.proyectoincremental.Utils.Grupos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorGrupos extends RecyclerView.Adapter<AdaptadorGrupos.ViewHolder> implements ListAdapter {
    private DatabaseReference refBBD, refBBD2;
    FirebaseDatabase database;
    private Context context;
    private List<Grupos> grupos;
    private int layout;
    private AdaptadorGrupos.OnItemClickListener itemClickListener;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    Grupos grupo;
    private String id;

    //Adaptador para carview eventos con imagen fecha , titulo y numero sitios libres
    public AdaptadorGrupos(List<Grupos> asignaturas, Context context, int layout, AdaptadorGrupos.OnItemClickListener itemListener) {
        this.grupos = asignaturas;
        this.layout = layout;
        this.context = context;
        this.itemClickListener = itemListener;


    }

    @Override
    public AdaptadorGrupos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        database = FirebaseDatabase.getInstance();

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        auth = FirebaseAuth.getInstance();

        AdaptadorGrupos.ViewHolder vh = new AdaptadorGrupos.ViewHolder(v);
        return vh;
    }

    //traemos la informacion de la BBD
    @Override
    public void onBindViewHolder(@NonNull AdaptadorGrupos.ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        grupo = grupos.get(position);
        holder.nombre.setText(grupo.getNombreGrupo());
        holder.curso.setText(grupo.getNumeroGrupo());

        //preguntar a agus si lo cambiamos a string par atraerlo mas facil
        //holder.plazas.setText(eventoss.getFecha());
        holder.bind(grupo, itemClickListener);


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {


        public TextView curso, nombre;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.texviewNombregrupo);
            curso = (TextView) itemView.findViewById(R.id.texviewnumeroCurso);
            itemView.setOnCreateContextMenuListener(this);


        }

        //clik al item
        public void bind(final Grupos grupos, final AdaptadorGrupos.OnItemClickListener itemListener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onItemClick(grupos, getAdapterPosition());
                }
            });

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem Delete = menu.add(Menu.NONE, 2, 2, "Delete");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 1:
                        //final DatabaseReference refBBD0 = database.getReference("Grupos");
                        database.getReference("Grupos").child(grupos.get(getAdapterPosition()).getId()).removeValue();
//                        refBBD0.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                                    id = dataSnapshot1.getKey();
//                                    refBBD = database.getReference("Grupos").child(id);
//                                    refBBD.removeValue();
//                                    break;
//                                }
//
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });


                        break;

                    case 2:
                        Intent intent = new Intent(context, EditarAsignaturasActivity.class);
                        intent.putExtra("NombreLocal", grupo.getNombreGrupo());
                        intent.putExtra("Contenido", grupo.getNumeroGrupo());
                        context.startActivity(intent);
                        break;
                }
                return true;
            }
        };
    }

    @Override
    public int getItemCount() {
        return grupos.size();
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
        void onItemClick(Grupos asignatura, int position);
    }


}
